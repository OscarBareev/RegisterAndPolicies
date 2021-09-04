import walkFiles.FilesWalker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        String path = "D:\\ideProjects\\parser\\Требования кредиторов";

        FilesWalker filesWalker = new FilesWalker();

        filesWalker.doWork(path);


    }
}
