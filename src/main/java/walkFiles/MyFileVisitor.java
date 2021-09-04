package walkFiles;

import dataClass.Info;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


public class MyFileVisitor extends SimpleFileVisitor<Path> {

    private List<Info> list;



    private int indCount = 0;//!!!Нужно сделать корректное отображение индексов




    public MyFileVisitor(List<Info> list) {
        this.list = list;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {



        return FileVisitResult.CONTINUE;
    }



    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){


        //Сохраняем пути к файлам
        Path pdfPath = null;
        Path rtfPath = null;
        ArrayList<Path> jpgList = new ArrayList<>();
        String creditorName = "";
        //String index = "AAA" + indCount++;

        if (file.toString().endsWith("pdf")) {
            pdfPath = file;
        }

        if (file.toString().endsWith("rtf")) {
            rtfPath = file;
        }

        if (file.toString().endsWith("jpg")) {
            jpgList.add(file);
        }

        if (rtfPath != null && !rtfPath.toString().toLowerCase().contains("расчет")) {
            creditorName = rtfPath.getFileName().toString()
                    .replace("_", " ")
                    .replace("Справка", "")
                    .replace("Материалы", "")
                    .replace("материалы", "")
                    .replace("справка", "")
                    .replace(".rtf", "")
                    .replace("Справка по установлению требований кредиторов", "")
                    .trim();
        }

        if (pdfPath != null){

            try {


                PDDocument doc = PDDocument.load(pdfPath.toFile());
                int count = doc.getNumberOfPages();
                doc.close();

                indCount++;
                String index = "AAA" + indCount++;

                String newFileName = "Копия требования кредитора с приложениями на " + count + "л. (" +
                        index + " " + shortName(creditorName) + ").pdf";


                String rootPath = file.getRoot().toString();
                String newFolder = rootPath + "Требования кредиторов (отредактированные)";

                if (!Files.exists(Paths.get(newFolder))){
                    Files.createDirectory(Paths.get(newFolder));
                }


                Files.copy(pdfPath, Paths.get(newFolder + "\\" + newFileName));

                list.add(new Info(index, creditorName));

            } catch (IOException e) {
                e.printStackTrace();
            }


        }



        //Нужно, чтобы все работало, в том числе обработка jpeg


        return FileVisitResult.CONTINUE;
    }


    private String shortName(String name){

        StringBuilder changedName = new StringBuilder(name);

        String[] nameArr = name.split(" ");

        if (!name.toLowerCase().contains("ооо")){
            if (nameArr.length >= 3){

                changedName = new StringBuilder(nameArr[0].trim() + " ");

               for (int i = 1; i <= nameArr.length; i++){
                   changedName.append(nameArr[i].charAt(0)).append(".");
                }
            }
        }

        return changedName.toString();

    }


}
