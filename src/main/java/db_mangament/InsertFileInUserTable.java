package db_mangament;

import environment_setter.EnvironmentReader;
import environment_setter.PropertyLoader;
import file_management.FileMover;
import file_management.FolderScanner;
import utils.StringToUnixTmstp;
import utils.TimeStampFinder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;
/**
 * <b>InsertFileInUserTable est la classe qui a pour objectif de gérer l'insertion de fichier dans la base de données.
 * @author mrichaudeau
 */
public class InsertFileInUserTable {

    private FolderScanner folderScanner;

    public String getScannedFolderPath() {
        return folderScanner.getFolder_name();
    }

    public String[] getScannedFolderContent() {
        return folderScanner.list_dir();
    }

    public InsertFileInUserTable(String filePath) {
        this.folderScanner = new FolderScanner(filePath);
    }
    /**
     * Execute le fichier de création de la base de données et de la table si nécessaire.
     * @return void
     */
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
        while (rs.next()) {
            if (rs.getInt("id_remboursement") == Integer.valueOf(dataToCompare[6])) {
                return "update";
            }
        }
        return "notUpdate";
    }
    /**
     * Est la méthode principale de la classe qui s'occupe de créer une connexion à la base.<br>
     * Vérifie que la base et la table sont créées sinon éxécute le fichier sql dans le dossier my_sql_db
     * Prépare les statements pour un insert et un update.
     * Scan le dossier où sont présents les fichiers.
     * Lit chaque fichier ligne par ligne.
     * Vérifie s'il s'agit d'un insert ou d'un update par rapport à l'id_remboursement.
     * Redirige les fichiers soit vers le dossier computed data ou problematic data.
     * @return void.
     */
    public void InsertInUserTable() {
        String folderPath = this.getScannedFolderPath();
        String[] scannedContent = this.getScannedFolderContent();


        try {
            // Lecture du fichier properties

            PropertyLoader propertyLoader = new PropertyLoader();
            Properties properties = propertyLoader.getDbConnexionDetails();

            EnvironmentReader environmentReader = new EnvironmentReader(
                    properties.getProperty("environment"),
                    properties.getProperty("server"),
                    properties.getProperty("db_name"),
                    properties.getProperty("user_name"),
                    properties.getProperty("password"));

            // Test de l'existence de la base et création de la base et de la sinon.


            DatabaseOperations db_check = new DatabaseOperations(
                    environmentReader.getServer(),
                    "",
                    environmentReader.getUser_name(),
                    environmentReader.getPassword());
            execSqlFile(db_check);

            // Connexion à la base.
            DatabaseOperations db_ops = new DatabaseOperations(
                    environmentReader.getServer(),
                    environmentReader.getDb_name(),
                    environmentReader.getUser_name(),
                    environmentReader.getPassword());

            String sqlUpdate = "UPDATE User SET ssn = ?, nom = ?, prenom = ?, date_naissance = ?, email = ?, tel_num = ?, code_soin = ?, montant_remboursement = ?, timestamp= ? WHERE id_remboursement = ?;";
            String sqlInsert = "INSERT INTO User (ssn, nom, prenom, date_naissance, email, tel_num, id_remboursement, code_soin, montant_remboursement, timestamp) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmtInsert = db_ops.getConnection().prepareStatement(sqlInsert);
            PreparedStatement stmtUpdate = db_ops.getConnection().prepareStatement(sqlUpdate);

            InputStream inputStreamFile = null;

            for (String file :
                    scannedContent) {
                try {
                    inputStreamFile = new FileInputStream(folderPath + file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (file.split("\\.")[1].equals("json")) {
                    ReadJsonFile(folderPath, file, inputStreamFile, folderPath + file, db_ops, stmtUpdate, stmtInsert);
                } else {
                    ReadCsvFile(folderPath, file, inputStreamFile, db_ops, stmtUpdate, stmtInsert);
                }
            }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    /**
     * Prépare la prepared statement si la ligne concernée est à insérer dans la BDD.
     * Affiche dans la console, added si la ligne à été mise à jour dans la base. Affiche failed to add s'il y a eu u problème entre temps.
     * @return void
     */
    public void execStatementInsert (PreparedStatement statement, String ssn, String nom, String prenom, String date_naissance,
                              String email, String tel_num, String code_soin, Integer id_remboursement,
                               Float montant_remboursement, String fp, String file){
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
    /**
     * Prépare la prepared statement si la ligne concernée est à mettre à jour dans la BDD.
     * Affiche dans la console, updated si la ligne à été mise à jour dans la base. Affiche failed to update s'il y a eu u problème entre temps.
     * @return void
     */
    public void execStatementUpdate (PreparedStatement statement, String ssn, String nom, String prenom, String date_naissance,
                                     String email, String tel_num, String code_soin, Integer id_remboursement,
                                     Float montant_remboursement, String fp, String file){
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
    /**
     * Appelle la classe TimeStampFinder sur le fichier dont le folder path et le file_name sont passé en argument.<br>
     * Puis s'occupe de mettre à jour la prepared statement.
     * @return void une fois que la prepared statement à été mise à jour.
     */
    private void getTimeStamp(PreparedStatement statement, String fp, String file, int index) throws SQLException {
        TimeStampFinder timeStampFinder = new TimeStampFinder(fp + file);
        StringToUnixTmstp stringToUnixTmstp = new StringToUnixTmstp(timeStampFinder.finder());
        try {
            String timestamp = stringToUnixTmstp.convert().toString();
            statement.setString(index, timestamp);
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