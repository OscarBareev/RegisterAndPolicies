package walkFiles;

import dataClass.Info;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesWalker {

    private List<Info> pathList = new ArrayList<>();


    public void doWork(String path) throws IOException {

        Files.walkFileTree(Paths.get(path),new MyFileVisitor(pathList));


    }

}
