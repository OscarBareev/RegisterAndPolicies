import walkFiles.FilesWalker;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String path = "D:\\Требования кредиторов\\Требования пропущенные";

        FilesWalker filesWalker = new FilesWalker();

        filesWalker.checkPdf(path);
        filesWalker.createTable(path);


    }
}
