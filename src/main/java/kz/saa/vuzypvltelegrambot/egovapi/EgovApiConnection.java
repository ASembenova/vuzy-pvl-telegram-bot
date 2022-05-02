package kz.saa.vuzypvltelegrambot.egovapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@PropertySource("classpath:application.properties")
public class EgovApiConnection {
    @Value("https://data.egov.kz/api/v4/zhogary_oku_oryndarynyn_tizbes/v5?apiKey=${dataegov.apikey}")
    private String urlDataset;
    private final String urlMetadata = "https://data.egov.kz/meta/zhogary_oku_oryndarynyn_tizbes/v5?pretty";
    private final String urlPassport = "https://data.egov.kz/datasets/view?index=zhogary_oku_oryndarynyn_tizbes";

    public String getUrlDataset() {
        return urlDataset;
    }

    private String getResponseByURL(URL url){
        String result = "";
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            result = response.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    public Vuz[] createVuzObjects(){
        Vuz[] vuzy = null;
        try {
            String response = getResponseByURL(new URL(getUrlDataset()));
            Gson gson = new GsonBuilder().create();
            vuzy = gson.fromJson(response, Vuz[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vuzy;
    }

    public Metadata createMetadataObject(){
        Metadata metadata = null;
        try {
            URL url = new URL(urlMetadata);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Gson gson = new GsonBuilder().create();
            metadata = (Metadata) gson.fromJson(response.toString(), Metadata.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
    }


    public String[][] getPassport(){
        try {
            String response = getResponseByURL(new URL(urlPassport));
            Document doc = Jsoup.parse(response);
            Element table = doc.select("table").get(1);
            //Element table = doc.getElementsByClass("table table-striped table-bordered table-advance table-hover").first();
            Elements rows = table.select("tbody > tr");
            String[][] array = new String[rows.size()][2];
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");
                array[i][0] = cols.get(0).text();
                array[i][1] = cols.get(1).text();
            }
            return array;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

