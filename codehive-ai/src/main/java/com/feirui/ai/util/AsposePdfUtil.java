package com.feirui.ai.util;

import com.aspose.pdf.Document;
import com.aspose.pdf.License;
import com.aspose.pdf.TextAbsorber;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

@Slf4j
public class AsposePdfUtil {

    static {
        getPdfLicense();
    }

    private static void getPdfLicense() {
        InputStream is = null;
        try {
            is = AsposePdfUtil.class.getResourceAsStream("/Aspose.Pdf.lic");
            License asposeLic = new License();
            asposeLic.setLicense(is);
        } catch (Exception e) {
            log.error("pdf module start error:", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("closeInputStream.error: {}", e.getMessage(), e);
                }
            }
        }
    }

    public static String extractPdfTextFromNet(String pdfUrl) {
        Document document = null;
        try (InputStream in = downloadPdf(pdfUrl)) {
            document = new Document(in);
            int pageNum = document.getPages().size();
            String[] allPageText = new String[pageNum];
            for (int i = 1; i <= pageNum; i++) {
                allPageText[i] = pdfToText(i, document);
            }
            System.out.println(Arrays.toString(allPageText));
            return String.join("\n", allPageText);
        } catch (Exception e) {
            log.error("aspose pdf2Text error, aspose转text失败", e);
            return null;
        } finally {
            closePdfDocument(document);
        }
    }

    private static InputStream downloadPdf(String pdfUrl) throws URISyntaxException, IOException {
        URI uri = new URI(pdfUrl);
        URL encodedUrl = uri.toURL();
        HttpURLConnection con = (HttpURLConnection) encodedUrl.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        return con.getInputStream();
    }

    private static String pdfToText(int page, Document pdfDocument) {
        try {
            TextAbsorber textAbsorber = new TextAbsorber();
            pdfDocument.getPages().get_Item(page).accept(textAbsorber);
            return textAbsorber.getText();
        } catch (Exception e) {
            log.error("pdfToText error", e);
        } finally {
            closePdfDocument(pdfDocument);
        }
        return null;
    }

    private static void closePdfDocument(Document document) {
        if (document != null) {
            try {
                document.close();
            } catch (Exception e) {
                log.error("closePdfDocument.error: {}", e.getMessage(), e);
            }
        }
    }

}
