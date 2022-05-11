package file_management;

import java.io.File;

public class FolderScanner {
    private String folder_name;

    public FolderScanner(String folder_name) {
        this.folder_name = folder_name;
    }

    public String getFolder_name() {
        return folder_name;
    }

    public String[] list_dir() {
        File directoryPath = new File(this.getFolder_name());
        //List of all files and directories
        String contents[] = directoryPath.list();
        return contents;
    }
}
