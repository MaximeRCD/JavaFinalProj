package db_mangament;

import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.sql.*;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class db_connect {
    public static void main(String[] args) throws SQLException {
//        DatabaseOperations a = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
//        DatabaseMetaData rst = a.displayElementsInDB("users_db");
//        String filePath = "Cuser_ 2 0200125021721.csv";
//
//        Pattern pattern = Pattern.compile("2");
//        Matcher matcher = pattern.matcher(filePath);
//        System.out.println(matcher.group());
        String stringToDecode =
                "C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_20200125021721.csv";

//        Matcher m = Pattern.compile("[0-9]{14}").matcher(stringToDecode);
//
//        if (!m.find())
//            System.out.println("not found");
//
//        String ticketReference = m.group();
//        System.out.println(ticketReference);
        TimeStampFinder t = new TimeStampFinder(stringToDecode);
        StringToUnixTmstp stu = new StringToUnixTmstp(t.finder());
        try {
            System.out.println(stu.convert());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        InsertFileInUserTable ifut2 = new InsertFileInUserTable("C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_20200125021721.csv");
        ifut2.InsertInUserTable();

        InsertFileInUserTable ifut1 = new InsertFileInUserTable("C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_20210606220136.csv");
        ifut1.InsertInUserTable();




    }
}


