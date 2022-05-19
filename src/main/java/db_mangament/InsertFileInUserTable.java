package db_mangament;

import file_management.FileMover;
import file_management.FolderScanner;
import org.json.JSONObject;
import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;

public class InsertFileInUserTable {

    private FolderScanner FolderPath;

    public String getFolderPath() {
        return FolderPath.getFolder_name();
    }

    public String[] getFolderPathScan() {
        return FolderPath.list_dir();
    }

    public InsertFileInUserTable(String filePath) {
        this.FolderPath = new FolderScanner(filePath);
    }

    private void execSqlFile(DatabaseOperations con) throws FileNotFoundException {
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(con.getConnection());
        //Creating a reader object
        Reader reader = new BufferedReader(new FileReader(".\\my_sql_db\\user_db.sql"));
        //Running the script
        sr.runScript(reader);
    }

    private String checkIfInsertOrUpdate(DatabaseOperations con, String[] dataToCompare) throws SQLException {
        String sqlSelect = "SELECT * FROM User;";

        Statement stmt = con.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sqlSelect);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            if (rs.getInt("id_remboursement") == Integer.valueOf(dataToCompare[6])) {
                return "update";
            }
        }
        return "notUpdate";
    }

    public void InsertInUserTable() {
        String fp = this.getFolderPath();
        String[] fpScan = this.getFolderPathScan();

        try {
            // Connexion to database
            DatabaseOperations db_check = new DatabaseOperations("localhost:3306", "", "root", "");
            execSqlFile(db_check);
            DatabaseOperations db_ops = new DatabaseOperations("localhost:3306", "users_db", "root", "");

            String sqlUpdate = "UPDATE User SET ssn = ?, nom = ?, prenom = ?, date_naissance = ?, email = ?, tel_num = ?, code_soin = ?, montant_remboursement = ?, timestamp= ? WHERE id_remboursement = ?;";
            String sqlInsert = "INSERT INTO User (ssn, nom, prenom, date_naissance, email, tel_num, id_remboursement, code_soin, montant_remboursement, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmtInsert = db_ops.getConnection().prepareStatement(sqlInsert);
            PreparedStatement stmtUpdate = db_ops.getConnection().prepareStatement(sqlUpdate);

            InputStream inputStream = null;

            for (String file :
                    fpScan) {
                try {
                    inputStream = new FileInputStream(fp + file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (file.split("\\.")[1].equals("json")) {
                    ReadJsonFile(fp, file, inputStream, fp + file, db_ops, stmtUpdate, stmtInsert);
                } else {
                    ReadCsvFile(fp, file, inputStream, db_ops, stmtUpdate, stmtInsert);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void execStatementInsert(PreparedStatement statement, String ssn, String nom, String prenom, String
            date_naissance,
                                    String email, String tel_num, String code_soin, Integer id_remboursement,
                                    Float montant_remboursement, String fp, String file) {
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
            getTimeStamp(statement, fp, file, 10);

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
    }

    public void execStatementUpdate(PreparedStatement statement, String ssn, String nom, String prenom, String
            date_naissance,
                                    String email, String tel_num, String code_soin, Integer id_remboursement,
                                    Float montant_remboursement, String fp, String file) {
        try {
            statement.setString(1, ssn);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setString(4, date_naissance);
            statement.setString(5, email);
            statement.setString(6, tel_num);
            statement.setInt(10, id_remboursement);
            statement.setString(7, code_soin);
            statement.setFloat(8, montant_remboursement);

            getTimeStamp(statement, fp, file, 9);

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
            System.out.println("update");
        } else {
            System.out.println("failed to update");
        }
    }


    private void getTimeStamp(PreparedStatement statement, String fp, String file, int index) throws SQLException {
        TimeStampFinder t = new TimeStampFinder(fp + file);
        StringToUnixTmstp stu = new StringToUnixTmstp(t.finder());
        try {
            String tmstp = stu.convert().toString();
            statement.setString(index, tmstp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void ReadJsonFile(String fp, String file, InputStream inputStream, String filePath, DatabaseOperations db_ops, PreparedStatement stmtUpdate, PreparedStatement stmtInsert) throws IOException {
        File files = new File(filePath);
        String content = new String(Files.readAllBytes(Paths.get(files.toURI())));
        JSONObject jsonContent = new JSONObject(content);
        String[] tab = jsonContent.toString().split("},");
        for (int i = 0; i < tab.length; i++) {
            try {
                JSONObject jsonItem = jsonContent.getJSONObject(String.valueOf(i));
                String[] data = new String[9];
                String ssn = jsonItem.getString("ssn");
                data[0] = jsonItem.getString("ssn");
                String nom = jsonItem.getString("nom");
                data[1] = jsonItem.getString("nom");
                String prenom = jsonItem.getString("prenom");
                data[2] = jsonItem.getString("prenom");
                String date_naissance = jsonItem.getString("date_naissance");
                data[3] = jsonItem.getString("date_naissance");
                String email = jsonItem.getString("email");
                data[4] = jsonItem.getString("email");
                String tel_num = jsonItem.getString("numero_telephone");
                data[5] = jsonItem.getString("numero_telephone");
                Integer id_remboursement = jsonItem.getInt("id_remboursement");
                data[6] = String.valueOf(jsonItem.getInt("id_remboursement"));
                String code_soin = jsonItem.getString("code_soin");
                data[7] = jsonItem.getString("code_soin");
                Float montant_remboursement = jsonItem.getFloat("montant_remboursement");
                data[8] = String.valueOf(jsonItem.getFloat("montant_remboursement"));
                String result = "";

                try {
                    result = checkIfInsertOrUpdate(db_ops, data);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (result == "update") {
                    execStatementUpdate(stmtUpdate, ssn, nom, prenom, date_naissance, email, tel_num, code_soin,
                            id_remboursement, montant_remboursement, fp, file);
                } else {
                    execStatementInsert(stmtInsert, ssn, nom, prenom, date_naissance, email, tel_num, code_soin,
                            id_remboursement, montant_remboursement, fp, file);
                }
            } catch (Exception ex) {
                inputStream.close();
                System.out.println("error catched in JSON file");
                File path_to_file = new File(filePath);
                FileMover fmv = new FileMover(path_to_file.getName(),
                        path_to_file.getPath(),
                        ".\\ressources\\problematicData\\"
                );
                fmv.move();
                System.out.println(ex);
                return;
            }
        }
        inputStream.close();
        File path_to_file = new File(fp + file);
        FileMover fmv = new FileMover(path_to_file.getName(),
                path_to_file.getPath(),
                ".\\ressources\\computedData\\"
        );
        fmv.move();
    }

    private void ReadCsvFile(String fp, String file, InputStream inputStream, DatabaseOperations db_ops, PreparedStatement stmtUpdate, PreparedStatement stmtInsert) throws IOException {
        assert inputStream != null;
        InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        try {
            br.lines().skip(1).forEach(line -> {
                String result = "";
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
                    result = checkIfInsertOrUpdate(db_ops, data);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (result == "update") {
                    execStatementUpdate(stmtUpdate, ssn, nom, prenom, date_naissance, email, tel_num, code_soin,
                            id_remboursement, montant_remboursement, fp, file);
                } else {
                    execStatementInsert(stmtInsert, ssn, nom, prenom, date_naissance, email, tel_num, code_soin,
                            id_remboursement, montant_remboursement, fp, file);
                }


            });
            inputStream.close();
            File path_to_file = new File(fp + file);
            FileMover fmv = new FileMover(path_to_file.getName(),
                    path_to_file.getPath(),
                    ".\\ressources\\computedData\\"
            );
            fmv.move();
        } catch (NumberFormatException | IOException ex) {
            inputStream.close();
            System.out.println("error catched");
            File path_to_file = new File(fp + file);
            FileMover fmv = new FileMover(path_to_file.getName(),
                    path_to_file.getPath(),
                    ".\\ressources\\problematicData\\"
            );
            fmv.move();
            System.out.println(ex);
        }
    }
}