package ou.acs.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ou.acs.constants.AccessConstants;
import ou.acs.entity.AccessDocument;
import ou.acs.entity.AccessObject;
import ou.acs.entity.Comment;
import ou.acs.entity.Person;
import ou.acs.qr.QRCodeUtil;
import ou.acs.repository.AccessDocumentRepository;
import ou.acs.service.PDF.PdfPageXofYEventHelper;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {
    private final AccessDocumentRepository accessDocumentRepository;
    private final DataService dataService;

    @Autowired
    public ExportService(AccessDocumentRepository accessDocumentRepository,
                         DataService dataService) {
        this.accessDocumentRepository = accessDocumentRepository;
        this.dataService = dataService;
    }

    public static final String FONT = "/fonts/tnr.ttf";
    public static final String WATERMARK = "/img/WATERMARK.png";

    public void exportToPDF(Long id,
                            HttpServletResponse response,
                            Principal principal) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        AccessDocument accessDocument = accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new);
        List<AccessObject> accessObjects = dataService.getObjectsByAccessDocumentId(principal, id);

        List<Person> personList = accessDocument.getPeoples()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        int accepted = (int) personList
                .stream()
                .filter(p -> p.getReasonResult().equals("+")).count();

        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, response.getOutputStream());
        pdfWriter.setPageEvent(new PdfPageXofYEventHelper());
        document.open();
        BaseFont baseFont= BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontTitle = new Font(baseFont, 16, Font.NORMAL);
        String organization = personList.get(0).getAccessDocument().getCompanyName();
        String number = personList.get(0).getAccessDocument().getSubscriptionNumber();
        String date = personList.get(0).getAccessDocument().getSubscriptionDate();
        BufferedImage bufferedImage = QRCodeUtil.encode("Заявка: №" +
                        accessDocument.getSubscriptionNumber() + " от " + accessDocument.getSubscriptionDate() + ". Время: " + new Date() +
                " Всего допущено: " + accepted + ".",
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + WATERMARK, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());
        document.add(iTextImage);

        StringBuilder header = new StringBuilder("Список сотрудников № " +
                accessDocument.getDocumentId() + "-ОУ от " + accessDocument.getPerformerFinishedDatetime().substring(0,10) + "\n"
                + "организации " + organization + "\n"
                + (accessDocument.getCompanyOgrnIp() != null ? "[ОГРНИП: " + accessDocument.getCompanyOgrnIp() + "]\n" :
                        accessDocument.getCompanyInn() != null ?
                        "[ИНН: " + accessDocument.getCompanyInn() + ", ОГРН: " + accessDocument.getCompanyOgrn() + "]\n" : "") +
                "[заявка № "  + number + " от " + date +
                        "], \nдопущенных на объекты:\n [");

        for (AccessObject accessObject : accessObjects)
        {
            header.append(AccessConstants.getTitle(accessObject.getTitle()));
            header.append("\n");
        }
        header = new StringBuilder(header.substring(0, header.length() - 1));
        header.append("]\n\n");
        Paragraph paragraph = new Paragraph(header.toString(), fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Font fontTableHeader = new Font(baseFont, 10, Font.BOLD);
        Font fontTable = new Font(baseFont, 10, Font.NORMAL);
        PdfPTable table = new PdfPTable(5);
        float[] columnWidths = new float[]{7f, 50f, 20f, 15f, 50f};
        table.setWidths(columnWidths);
        table.setTotalWidth(100);
        PdfPCell cellH1 = new PdfPCell(new Phrase("№ п/п", fontTableHeader));
        PdfPCell cellH2 = new PdfPCell(new Phrase("ФИО", fontTableHeader));
        PdfPCell cellH3 = new PdfPCell(new Phrase("Дата рождения", fontTableHeader));
        PdfPCell cellH4 = new PdfPCell(new Phrase("Паспорт", fontTableHeader));
        PdfPCell cellH5 = new PdfPCell(new Phrase("Место рождения", fontTableHeader));
        cellH1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellH1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellH2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellH2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellH3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellH3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellH4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellH4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellH5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellH5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cellH1);
        table.addCell(cellH2);
        table.addCell(cellH3);
        table.addCell(cellH4);
        table.addCell(cellH5);
        int i = 1;
        for (Person person : personList)
            if (person.getReasonResult().equals("+"))
            {
                PdfPCell cellB1 = new PdfPCell(new Phrase(Integer.toString(i++), fontTable));
                cellB1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellB1.setHorizontalAlignment((Element.ALIGN_CENTER));
                table.addCell(cellB1);

                PdfPCell cellB2 = new PdfPCell(new Phrase(person.getName(), fontTable));
                cellB2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellB2.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellB2);

                PdfPCell cellB3 = new PdfPCell(new Phrase(person.getBirthday(), fontTable));
                cellB3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellB3.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellB3);

                PdfPCell cellB4 = new PdfPCell(new Phrase(person.getPassport() == null ?
                        person.getForeignPassport() : person.getPassport(), fontTable));
                cellB4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellB4.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellB4);

                PdfPCell cellB5 = new PdfPCell(new Phrase(person.getBirthName(), fontTable));
                cellB5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellB5.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellB5);
            }
        document.add(table);
        List<Comment> comments = accessDocument.getComments();
        if (comments.size() > 0) {
            Paragraph paragraphComments = new Paragraph("Комментарии комендатуры:", fontTable);
            document.add(paragraphComments);
            int iter = 1;
            for (Comment comment : comments)
                document.add(new Paragraph(iter++ + "." + comment.getText(), fontTable));
        }
        Paragraph paragraph1 = new Paragraph(new Phrase("Руководитель подразделения:\n" + "_________________________________________________________________\n" + dateFormat.format(new Date()) + " г.", fontTitle));
        document.add(paragraph1);
        document.close();
    }

    public void exportToExcel(Long id,
                              HttpServletResponse response) throws Exception {
        AccessDocument accessDocument = accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new);
        List<Person> personList = new ArrayList<>(accessDocument.getPeoples()).stream()
                .sorted(Comparator.comparing(Person::getName))
                .collect(Collectors.toList());
        int allowed = personList.stream()
                .filter(p -> p.getReasonResult().equals("+")).collect(Collectors.toList()).size();
        int notAllowed = personList.stream()
                .filter(p -> p.getReasonResult().equals("-")).collect(Collectors.toList()).size();
        int error = personList.stream()
                .filter(p -> p.getReasonResult().equals("?")).collect(Collectors.toList()).size();
        ServletOutputStream outputStream = response.getOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet allowedSheet = workbook.createSheet("Допущенные (" + allowed + ")");
        XSSFSheet notAllowedSheet = workbook.createSheet("Отведенные (" + notAllowed + ")");
        XSSFSheet errorSheet = workbook.createSheet("Ошибка установочных данных (" + error + ")");
        setHead(workbook, allowedSheet);
        setHead(workbook,  notAllowedSheet);
        setHead(workbook,  errorSheet);
        int allowedCount = 1;
        int notAllowedCount = 1;
        int errorCount = 1;
        for (Person person : personList)
        {
            if (person.getReasonResult().equals("+")) {
                Row row = allowedSheet.createRow(allowedCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell organization = row.createCell(6);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                organization.setCellValue(accessDocument.getCompanyName());

            } else if (person.getReasonResult().equals("-")){
                Row row = notAllowedSheet.createRow(notAllowedCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell organization = row.createCell(6);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                organization.setCellValue(accessDocument.getCompanyName());

            } else {
                Row row = errorSheet.createRow(errorCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell organization = row.createCell(6);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                organization.setCellValue(accessDocument.getCompanyName());
            }
        }
        allowedSheet.autoSizeColumn(0);
        allowedSheet.autoSizeColumn(1);
        allowedSheet.autoSizeColumn(2);
        allowedSheet.autoSizeColumn(3);
        allowedSheet.autoSizeColumn(6);

        notAllowedSheet.autoSizeColumn(0);
        notAllowedSheet.autoSizeColumn(1);
        notAllowedSheet.autoSizeColumn(2);
        notAllowedSheet.autoSizeColumn(3);
        notAllowedSheet.autoSizeColumn(6);

        errorSheet.autoSizeColumn(0);
        errorSheet.autoSizeColumn(1);
        errorSheet.autoSizeColumn(2);
        errorSheet.autoSizeColumn(3);
        errorSheet.autoSizeColumn(6);

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void setHead(XSSFWorkbook workbook, XSSFSheet sheet) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
        Row headRow = sheet.createRow(0);
        Cell lastName = headRow.createCell(0);
        Cell firstname = headRow.createCell(1);
        Cell patronymic = headRow.createCell(2);
        Cell birthday = headRow.createCell(3);
        Cell organization = headRow.createCell(6);
        lastName.setCellValue("Фамилия");
        firstname.setCellValue("Имя");
        patronymic.setCellValue("Отчество");
        birthday.setCellValue("Дата рождения");
        organization.setCellValue("Организация");
        lastName.setCellStyle(style);
        firstname.setCellStyle(style);
        patronymic.setCellStyle(style);
        birthday.setCellStyle(style);
        organization.setCellStyle(style);
    }

    private void setHeadWithPassport(XSSFWorkbook workbook, XSSFSheet sheet) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(12);
        style.setFont(font);
        Row headRow = sheet.createRow(0);
        Cell lastName = headRow.createCell(0);
        Cell firstname = headRow.createCell(1);
        Cell patronymic = headRow.createCell(2);
        Cell birthday = headRow.createCell(3);
        Cell passport = headRow.createCell(4);
        Cell organization = headRow.createCell(5);
        lastName.setCellValue("Фамилия");
        firstname.setCellValue("Имя");
        patronymic.setCellValue("Отчество");
        birthday.setCellValue("Дата рождения");
        passport.setCellValue("Паспорт");
        organization.setCellValue("Организация");
        lastName.setCellStyle(style);
        firstname.setCellStyle(style);
        patronymic.setCellStyle(style);
        birthday.setCellStyle(style);
        passport.setCellStyle(style);
        organization.setCellStyle(style);
    }

    public void exportToExcelWithPassport(Long id, HttpServletResponse response) throws IOException {
        AccessDocument accessDocument = accessDocumentRepository.findById(id).orElseThrow(NoSuchElementException::new);
        List<Person> personList = new ArrayList<>(accessDocument.getPeoples()).stream()
                .sorted(Comparator.comparing(Person::getName))
                .collect(Collectors.toList());
        int allowed = personList.stream()
                .filter(p -> p.getReasonResult().equals("+")).collect(Collectors.toList()).size();
        int notAllowed = personList.stream()
                .filter(p -> p.getReasonResult().equals("-")).collect(Collectors.toList()).size();
        int error = personList.stream()
                .filter(p -> p.getReasonResult().equals("?")).collect(Collectors.toList()).size();
        ServletOutputStream outputStream = response.getOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet allowedSheet = workbook.createSheet("Допущенные (" + allowed + ")");
        XSSFSheet notAllowedSheet = workbook.createSheet("Отведенные (" + notAllowed + ")");
        XSSFSheet errorSheet = workbook.createSheet("Ошибка установочных данных (" + error + ")");
        setHeadWithPassport(workbook, allowedSheet);
        setHeadWithPassport(workbook,  notAllowedSheet);
        setHeadWithPassport(workbook,  errorSheet);
        int allowedCount = 1;
        int notAllowedCount = 1;
        int errorCount = 1;
        for (Person person : personList)
        {
            if (person.getReasonResult().equals("+")) {
                Row row = allowedSheet.createRow(allowedCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell passport = row.createCell(4);
                Cell organization = row.createCell(5);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                passport.setCellValue(person.getPassport());
                organization.setCellValue(accessDocument.getCompanyName());

            } else if (person.getReasonResult().equals("-")){
                Row row = notAllowedSheet.createRow(notAllowedCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell passport = row.createCell(4);
                Cell organization = row.createCell(5);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                passport.setCellValue(person.getPassport());
                organization.setCellValue(accessDocument.getCompanyName());

            } else {
                Row row = errorSheet.createRow(errorCount++);
                Cell lastName = row.createCell(0);
                Cell firstName = row.createCell(1);
                Cell patronymic = row.createCell(2);
                Cell birthday = row.createCell(3);
                Cell passport = row.createCell(4);
                Cell organization = row.createCell(5);

                lastName.setCellValue(person.getLastname());
                firstName.setCellValue(person.getFirstname());
                patronymic.setCellValue(person.getPatronymic());
                birthday.setCellValue(person.getBirthday());
                passport.setCellValue(person.getPassport());
                organization.setCellValue(accessDocument.getCompanyName());
            }
        }
        allowedSheet.autoSizeColumn(0);
        allowedSheet.autoSizeColumn(1);
        allowedSheet.autoSizeColumn(2);
        allowedSheet.autoSizeColumn(3);
        allowedSheet.autoSizeColumn(4);
        allowedSheet.autoSizeColumn(5);

        notAllowedSheet.autoSizeColumn(0);
        notAllowedSheet.autoSizeColumn(1);
        notAllowedSheet.autoSizeColumn(2);
        notAllowedSheet.autoSizeColumn(3);
        notAllowedSheet.autoSizeColumn(4);
        notAllowedSheet.autoSizeColumn(5);

        errorSheet.autoSizeColumn(0);
        errorSheet.autoSizeColumn(1);
        errorSheet.autoSizeColumn(2);
        errorSheet.autoSizeColumn(3);
        errorSheet.autoSizeColumn(4);
        errorSheet.autoSizeColumn(5);

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
