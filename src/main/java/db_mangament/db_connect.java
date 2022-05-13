package db_mangament;

import file_management.FileMover;
import file_management.FolderScanner;
import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class db_connect {
    public static void main(String[] args) throws SQLException {
        /* Test connexion */

//        DatabaseOperations a = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
//        DatabaseMetaData rst = a.displayElementsInDB("users_db");


        /*
        Test the function insert on a file which is  well formatted
        It is then placed in computed data folder
        */

        InsertFileInUserTable ifut1 = new InsertFileInUserTable(".\\ressources\\inputData\\user_20200125021721.csv");
        ifut1.InsertInUserTable();


        /*
        Test the function insert on a file which is not well formatted
        It is then placed in problematic data folder
        */

//        InsertFileInUserTable ifut2 = new InsertFileInUserTable(".\\ressources\\inputData\\user_20210606220136.csv");
//        ifut2.InsertInUserTable();

        /* Test folder scanner class */

//        FolderScanner fs = new FolderScanner(".\\ressources\\inputData\\");
//        String test[] = fs.list_dir();
//        Arrays.stream(test).toList().forEach(System.out::println);
//        System.out.println(test[0]);

    }
}


