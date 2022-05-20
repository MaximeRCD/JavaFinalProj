import db_mangament.DatabaseOperations;
import db_mangament.InsertFileInUserTable;
import environment_setter.PropertyLoader;
import file_management.FileMover;
import file_management.FolderScanner;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) throws IOException {
        String relativePathToFolderContainingData = ".\\ressources\\inputData\\";
//        InsertFileInUserTable ifut1 = new InsertFileInUserTable(relativePathToFolderContainingData);
//        ifut1.InsertInUserTable();


        // Put Data back to input data folder
        FolderScanner computedDataFolderScanner = new FolderScanner(".\\ressources\\computedData\\");
        FolderScanner problematicDataFolderScanner = new FolderScanner(".\\ressources\\problematicData\\");

        for (String fileName:
                computedDataFolderScanner.list_dir()) {
            File path_to_file = new File(computedDataFolderScanner.getFolder_name() + fileName);
            FileMover fileMover = new FileMover(path_to_file.getName(),
                    path_to_file.getPath(),
                    ".\\ressources\\inputData\\"
            );
            fileMover.move();
        }
        for (String fileName:
                problematicDataFolderScanner.list_dir()) {
            File path_to_file = new File(problematicDataFolderScanner.getFolder_name() + fileName);
            FileMover fileMover = new FileMover(path_to_file.getName(),
                    path_to_file.getPath(),
                    ".\\ressources\\inputData\\"
            );
            fileMover.move();
        }
    }
}


