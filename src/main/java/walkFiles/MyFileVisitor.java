package walkFiles;

import dataClass.Info;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class MyFileVisitor extends SimpleFileVisitor<Path> {

    private List<Info> list;

    private int indCount = 1726;


    public MyFileVisitor(List<Info> list) {
        this.list = list;
    }


    Path pdfPath = null;
    Path rtfPath = null;
    String creditorName = "";
    ArrayList<Path> jpgList = new ArrayList<>();

    private ArrayList<Path> checkArr = new ArrayList<>();

    public ArrayList<Path> getCheckArr() {
        return checkArr;
    }


    int stepsCount = 0;


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {


        jpgList.clear();

        creditorName = "";
        pdfPath = null;
        rtfPath = null;

        String rootPath = dir.getRoot().toString();
        String newFolder = rootPath + "Требования кредиторов (отредактированные)";
        if (!Files.exists(Paths.get(newFolder))) {
            Files.createDirectory(Paths.get(newFolder));
        }

        if (stepsCount != 0) {
            Stream<Path> filePathStream = Files.walk(dir);
            filePathStream
                    .filter(Files::isRegularFile)
                    .forEach(file -> {

                        if (!checkArr.contains(file)) {
                            if (file.toString().endsWith("rtf") && !file.toString().toLowerCase().contains("расчет")) {
                                rtfPath = file;
                            }

                            if (file.toString().toLowerCase().endsWith("pdf") && !file.toString().toLowerCase().contains("запрос")) {
                                pdfPath = file;
                            }

                            if (file.toString().endsWith("jpg")) {
                                jpgList.add(file);
                            }
                        }

                        checkArr.add(file);
                    });
        }


        if (rtfPath != null) {
            creditorName = rtfPath.getFileName().toString()
                    .replace("_", " ")
                    .replace("Справка", "")
                    .replace("Материалы", "")
                    .replace("материалы", "")
                    .replace("Материалы", "")
                    .replace("справка", "")
                    .replace(".rtf", "")
                    .replace("по установлению требований кредиторов", "")
                    .trim();
        }


        if (pdfPath != null) {

            if (creditorName.equals("")) {
                String[] pdfNameArr = pdfPath.getFileName().toString().split(" ");
                creditorName = pdfNameArr[1].trim();
            }

            try {


                PDDocument doc = PDDocument.load(pdfPath.toFile());
                int count = doc.getNumberOfPages();
                doc.close();



                String index = indVisual(indCount++);
                String newFileName = "Копия требования кредитора с приложениями (" +
                        index + ", " + creditorName + ") на " + count + "л..pdf";


                if (!Files.exists(Paths.get(newFolder + "\\" + newFileName))) {
                    Files.copy(pdfPath, Paths.get(newFolder + "\\" + newFileName));
                }

                list.add(new Info(index, creditorName));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (jpgList.size() > 0) {



            String index = indVisual(indCount++);
            String newFileName = "Копия требования кредитора с приложениями (" +
                    index + ", " + creditorName + ") на " + jpgList.size() + "л..pdf";

            try {
                PDDocument doc = new PDDocument();

                for (Path imgPass : jpgList) {

                    InputStream in = new FileInputStream(imgPass.toString());
                    BufferedImage bimg = ImageIO.read(in);

                    float width = bimg.getWidth();
                    float height = bimg.getHeight();
                    PDPage page = new PDPage(new PDRectangle(width, height));
                    doc.addPage(page);
                    PDImageXObject pdImageXObject = JPEGFactory.createFromImage(doc, bimg, 0.5f, 10);

                    PDPageContentStream contentStream = new PDPageContentStream(doc, page);
                    contentStream.drawImage(pdImageXObject, 0, 0);
                    contentStream.close();
                }

                if (!Files.exists(Paths.get(newFolder + "\\" + newFileName))) {
                    doc.save(newFolder + "\\" + newFileName);
                }


                doc.close();

                list.add(new Info(index, creditorName));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }





            stepsCount++;
        return FileVisitResult.CONTINUE;
    }


    private String indVisual(int indCount) {

        String indStr = indCount + "";
        int intSize = indStr.length();

        StringBuilder result = new StringBuilder();

        result.append("0".repeat(Math.max(0, 5 - intSize)));
        result = new StringBuilder("AAA" + result + indCount);

        return result.toString();
    }
}
