package ou.acs.service.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Date;

public class PdfPageXofYEventHelper extends PdfPageEventHelper {
    public PdfTemplate total;

    public BaseFont baseFont;

    private final Font normal;
    private final Font normalSmall;

    public PdfPageXofYEventHelper() throws IOException, DocumentException {
        ClassPathResource cpr = new ClassPathResource("/fonts/tnr.ttf");
        this.normal = new Font(BaseFont.createFont(cpr.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 8);
        this.normalSmall = new Font(BaseFont.createFont(cpr.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 6);
    }

    @SneakyThrows
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(500, 500);
        try {
            ClassPathResource cpr = new ClassPathResource("/fonts/tnr.ttf");
            baseFont = BaseFont.createFont(cpr.getPath(),BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }

    }

    public void onEndPage(PdfWriter writer, Document document) {
        String WATERMARK = "classpath:/img/WATERMARK.png";
        PdfContentByte waterMar = writer.getDirectContentUnder();
        waterMar.beginText();
        PdfGState gs = new PdfGState();
        gs.setStrokeOpacity(0.1f);
        gs.setFillOpacity(0.03f);
        try {
            Image image = Image.getInstance(WATERMARK);
            image.setAbsolutePosition(200, 300);
            image.setRotation(0);
            image.setRotationDegrees(0);
            image.scalePercent(20);
            waterMar.setGState(gs);
            waterMar.addImage(image);
            waterMar.setGState(gs);
            waterMar.setColorFill(BaseColor.GRAY);
            waterMar.setFontAndSize(baseFont, 18);
            gs.setFillOpacity(0.15f);
            waterMar.setGState(gs);
            int position=10;
            for(int m=0; m<10; m++){
                waterMar.showTextAligned(Element.ALIGN_LEFT, "Проверено",position, (float) position + m * 100, 40);
                waterMar.showTextAligned(Element.ALIGN_LEFT, "Проверено",(float) position + 200, (float) position + m * 100, 40);
                waterMar.showTextAligned(Element.ALIGN_LEFT, "Проверено",(float) position + 400, (float) position + m * 100, 40);
            }
            waterMar.endText();
            waterMar.stroke();
        } catch (IOException | BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{24, 24, 2});
            table.getDefaultCell().setFixedHeight(10);
            table.getDefaultCell().setBorder(Rectangle.TOP);
            PdfPCell cell = new PdfPCell();

            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPhrase(new Phrase("Время генерации: " + new Date(), normalSmall));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPhrase(new Phrase(String.format("Страница %d", writer.getPageNumber()), normal));
            table.addCell(cell);

            cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()
                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(),
                    document.bottomMargin() - 10, writer.getDirectContent());

        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @SneakyThrows
    public void onCloseDocument(PdfWriter writer, Document document) {
    }
}
