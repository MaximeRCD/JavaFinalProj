package db_mangament;

import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;


public class JdbcInsertFileOne {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\maxim\\TélécomST\\Cours\\DE2\\DEV\\Final_Project\\ressources\\inputData\\user_20200125021721.csv";

        try {
            DatabaseOperations db_ops = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
            String sql = "INSERT INTO User (ssn, nom, prenom, date_naissance, email, tel_num, id_remboursement, code_soin, montant_remboursement, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = db_ops.getConnection().prepareStatement(sql);
            try (
                    InputStream inputStream = new FileInputStream(filePath);
                    InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr)
            ){
                br.lines().skip(1).forEach(line -> {
                    System.out.println(line);
                    String[] data = line.split(",");

                    String ssn = data[0];
                    String nom = data[1];
                    String prenom = data[2];
                    String date_naissance = data[3];
                    String email = data[4];
                    String tel_num = data[5];
                    String id_remboursement = data[6];
                    String code_soin = data[7];
                    String montant_remboursement = data[8];

                    try {
                        statement.setString(1, ssn);
                        statement.setString(2, nom);
                        statement.setString(3, prenom);
                        statement.setString(4, date_naissance);
                        statement.setString(5, email);
                        statement.setString(6, tel_num);
                        statement.setString(7, id_remboursement);
                        statement.setString(8, code_soin);
                        statement.setString(9, montant_remboursement);

                        TimeStampFinder t = new TimeStampFinder(filePath);
                        StringToUnixTmstp stu = new StringToUnixTmstp(t.finder());
                        try {
                            String tmstp = stu.convert().toString();
                            statement.setString(10, tmstp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    int i = 0;
                    try {
                        i = statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if(i!=0){
                        System.out.println("added");
                    }
                    else{
                        System.out.println("failed to add");
                    }
                });
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}