import db_mangament.InsertFileInUserTable;

import java.sql.*;

public class Main {
    public static void main(String[] args){
        String relativePathToFolderContainingData = ".\\ressources\\inputData\\";
        InsertFileInUserTable ifut1 = new InsertFileInUserTable(relativePathToFolderContainingData);
        ifut1.InsertInUserTable();

    }
}


