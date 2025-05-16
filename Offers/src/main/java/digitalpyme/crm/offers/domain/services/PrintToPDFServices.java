package digitalpyme.crm.offers.domain.services;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.lowagie.text.DocumentException;
import digitalpyme.crm.offers.application.rest.response.OfferExtendResponse;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrintToPDFServices {

    private final SpringTemplateEngine springTemplateEngine;

    public PrintToPDFServices(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    public byte[] generatePdf(OfferExtendResponse offer) throws DocumentException {
        if (offer.getItems() == null) {
            offer.setItems(java.util.Collections.emptyList());
        }

        List<OfferExtendResponse> offerPages = new ArrayList<>();

        for (int i = 0; i < offer.getItems().size(); i += 13) {
            int end = Math.min(i + 13, offer.getItems().size());
            OfferExtendResponse offerSubset = new OfferExtendResponse(
                    offer.getIdOffer(),
                    offer.getName(),
                    offer.getDate(),
                    offer.getClient(),
                    offer.getStatus(),
                    offer.getPrices(),
                    offer.getItems().subList(i, end),
                    offer.getTotalItems()
            );
            offerPages.add(offerSubset);
        }

        Context context = new Context();
        byte[] htmlPages = null;

        for (int i = 0; i < offerPages.size(); i++) {
            context.setVariable("offer", offerPages.get(i));

            String tempHtmlPageNext = null;
            if (i == offerPages.size() - 1) {
                tempHtmlPageNext = springTemplateEngine.process("Template-PrintOffer_end", context);
            } else {
                tempHtmlPageNext = springTemplateEngine.process("Template-PrintOffer", context);
            }

            byte[] tempHtmlPagesBytes = createPDFFromHtml(tempHtmlPageNext);
            if (htmlPages == null) {
                htmlPages = tempHtmlPagesBytes;
            } else {
                htmlPages = mergePDFPages(tempHtmlPagesBytes, htmlPages);
            }
        }

        return htmlPages;
    }

    private byte[] createPDFFromHtml(String content) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(content);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    private byte[] mergePDFPages(byte[] page1, byte[] page2) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfCopy pdfCopy = new PdfCopy(document, outputStream);
            document.open();

            if (page2 != null && page2.length > 0) {
                PdfReader reader2 = new PdfReader(page2);
                for (int i = 1; i <= reader2.getNumberOfPages(); i++) {
                    PdfImportedPage page = pdfCopy.getImportedPage(reader2, i);
                    pdfCopy.addPage(page);
                }
            }

            if (page1 != null && page1.length > 0) {
                PdfReader reader1 = new PdfReader(page1);
                for (int i = 1; i <= reader1.getNumberOfPages(); i++) {
                    PdfImportedPage page = pdfCopy.getImportedPage(reader1, i);
                    pdfCopy.addPage(page);
                }
            }

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }
}