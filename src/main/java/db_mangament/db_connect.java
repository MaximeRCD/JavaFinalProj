package db_mangament;

import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonParser;
import file_management.FileMover;
import file_management.FolderScanner;
import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class db_connect {
    public static void main(String[] args) throws SQLException, IOException, ParseException {
        /* Test connexion */

//        DatabaseOperations a = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
//        DatabaseMetaData rst = a.displayElementsInDB("users_db");


        /*
        Test the function insert on a file which is  well formatted
        It is then placed in computed data folder
        */

        InsertFileInUserTable ifut1 = new InsertFileInUserTable(".\\ressources\\inputData\\");
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

        /* Test JSON Read file*/
//        File file = new File(".\\ressources\\inputData\\user_20210219092753.json");
//        String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
//        JSONObject jsonContent = new JSONObject(content);
//        String[] tab = jsonContent.toString().split("},");
//        for(int i = 0; i<tab.length;i++){
//            JSONObject jsonBackground = jsonContent.getJSONObject(String.valueOf(9));
//            System.out.println(Float.valueOf(jsonBackground.get("montant_remboursement").toString()));

 //       }
    }
}



