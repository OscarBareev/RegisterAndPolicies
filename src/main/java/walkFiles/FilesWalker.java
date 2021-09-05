package walkFiles;

import dataClass.Info;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesWalker {

    private List<Info> infoList = new ArrayList<>();


    public void checkPdf(String path) throws IOException {

        MyFileVisitor visitor = new MyFileVisitor(infoList);
        Files.walkFileTree(Paths.get(path), visitor);
        visitor.getCheckArr().clear();
    }

    public void createTable(String path) throws IOException {

        List<String> clmTitles = new ArrayList<>();
        clmTitles.add("Индекс");
        clmTitles.add("Фамилия Имя Отечество срахователя");
        clmTitles.add("Дата получения заявления");
        clmTitles.add("Полис ОСАГО (ХХХ00000000)");
        clmTitles.add("Начало действия договора");
        clmTitles.add("Окончание действия договора");
        clmTitles.add("Нач. периода 1");
        clmTitles.add("Ок. перод 1");
        clmTitles.add("Нач. периода 2");
        clmTitles.add("Ок. перод 2");
        clmTitles.add("Нач. периода 3");
        clmTitles.add("Ок. перод 3");
        clmTitles.add("Сумма страховой премии");
        clmTitles.add("Квитанция об оплате премии (да/нет)");
        clmTitles.add("Документ дающий основание для расторжения договора (договор купли-продажи ТС, иное)");
        clmTitles.add("Полис КАСКО (ХХХ00000)");
        clmTitles.add("Начало действия договора");
        clmTitles.add("Окончание действия договора");
        clmTitles.add("Сумма страховой премии");
        clmTitles.add("Телефон страхователя");
        clmTitles.add("Адрес эл. почты");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Кредиторы");

        int cols = clmTitles.size();
        int rows = infoList.size() + 1;

        XSSFRow row = sheet.createRow(0);


        for (int i = 0; i < cols; i++) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(clmTitles.get(i));
        }


        for (int i = 0; i < infoList.size(); i++) {
            row = sheet.createRow(i + 1);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(infoList.get(i).getIndex());
            cell = row.createCell(1);
            cell.setCellValue(infoList.get(i).getName());
        }


        Path p = Paths.get(path).getRoot();
        String filePath = p.toString() + "\\Требования кредиторов (отредактированные)\\Требования кредиторов.xlsx";
        FileOutputStream outstream = new FileOutputStream(filePath);
        workbook.write(outstream);
        outstream.close();
        infoList.clear();
    }

}
