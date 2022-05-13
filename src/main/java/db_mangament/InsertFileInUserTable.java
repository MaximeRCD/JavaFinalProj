package db_mangament;

import file_management.FileMover;
import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;

public class InsertFileInUserTable {

    private String FilePath;

    public String getFilePath() {
        return FilePath;
    }

    public InsertFileInUserTable(String filePath) {
        FilePath = filePath;
    }

    public void InsertInUserTable(){
        String fp = this.getFilePath();
        try {
            // Connexion to database
            DatabaseOperations db_ops = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
            String sql = "INSERT INTO User (ssn, nom, prenom, date_naissance, email, tel_num, id_remboursement, code_soin, montant_remboursement, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = db_ops.getConnection().prepareStatement(sql);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(fp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert inputStream != null;
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            try {

                    br.lines().skip(1).forEach(line -> {
                    //System.out.println(line);
                    String[] data = line.split(",");
                    String ssn = data[0];
                    String nom = data[1];
                    String prenom = data[2];
                    String date_naissance = data[3];
                    String email = data[4];
                    String tel_num = data[5];
                    Integer id_remboursement = Integer.valueOf(data[6]);
                    String code_soin = data[7];
                    Float montant_remboursement = Float.valueOf(data[8]);

                    try {
                        statement.setString(1, ssn);
                        statement.setString(2, nom);
                        statement.setString(3, prenom);
                        statement.setString(4, date_naissance);
                        statement.setString(5, email);
                        statement.setString(6, tel_num);
                        statement.setInt(7, id_remboursement);
                        statement.setString(8, code_soin);
                        statement.setFloat(9, montant_remboursement);

                        TimeStampFinder t = new TimeStampFinder(fp);
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
                    if (i != 0) {
                        System.out.println("added");
                    } else {
                        System.out.println("failed to add");
                    }
            });
                inputStream.close();
                File path_to_file = new File(fp);
                FileMover fmv = new FileMover(path_to_file.getName(),
                        path_to_file.getPath(),
                        ".\\ressources\\computedData\\"
                );
                fmv.move();
            }catch (NumberFormatException ex){
                inputStream.close();
                System.out.println("error catched");
                File path_to_file = new File(fp);
                FileMover fmv = new FileMover(path_to_file.getName(),
                        path_to_file.getPath(),
                        ".\\ressources\\problematicData\\"
                );
                fmv.move();
                System.out.println(ex);
                return;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}