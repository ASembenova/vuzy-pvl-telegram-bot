package kz.saa.vuzypvltelegrambot.service.report;

import com.lowagie.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

@Service
public class PDFService {
    @Autowired
    private SpringTemplateEngine templateEngine;
    private static final String EXTERNAL_FILE_PATH = "/storage/";

    public File generatePDF(Map<String, Object> variables, String templateName) throws Exception {
        Context context = createContext(variables);
        String html = templateEngine.process(templateName, context);
        return renderPDF(html);
    }

    private File renderPDF(String html) throws Exception{
        File file = File.createTempFile("report", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        ITextFontResolver resolver = renderer.getFontResolver();
        resolver.addFont("/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        resolver.addFont("/fonts/arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        resolver.addFont("/fonts/consola.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        resolver.addFont("/fonts/consolab.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        resolver.addFont("/fonts/consolai.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        resolver.addFont("/fonts/consolaz.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.setDocumentFromString(html, EXTERNAL_FILE_PATH);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    private Context createContext(Map<String, Object> variables){
        Context context = new Context();
        for(Map.Entry<String,Object> entry: variables.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return context;
    }




}

