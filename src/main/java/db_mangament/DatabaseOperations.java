package db_mangament;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
    private String server;
    private String database;
    private String User;
    private String Password;
    private Statement statement;
    private Connection connection;

    public java.sql.Statement getStatement() {
        return statement;
    }

    public DatabaseOperations(String server, String database, String user, String password) throws SQLException {
        this.server = server;
        this.database = database;
        User = user;
        Password = password;

        this.connection = DriverManager.getConnection(
                "jdbc:mysql://" + this.server + "/" + this.database, this.User, this.Password);

    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return User;
    }

    public String getPassword() {
        return Password;
    }

    public ResultSet ExecuteQuery(String Query) throws SQLException {
        this.statement = this.connection.createStatement();
        ResultSet result = this.statement.executeQuery(Query);
        this.connection.close();
        return result;
    }

    public ResultSet GetDepartmentsWithCommission() throws SQLException {
        if (this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.server + "/" + this.database, this.User, this.Password);
            this.statement = this.connection.createStatement();
        }
        ResultSet result = this.ExecuteQuery("SELECT COUNT(DISTINCT DEPARTMENT_ID) from employees where COMMISSION_PCT != 0");
        while (result.next()) {
            System.out.println(result.getInt(1));
        }
        this.connection.close();
        return result;
    }

    public ResultSet countEmployees(int DeptId) throws SQLException {
        ResultSet result = null;

        String sql = "SELECT COUNT(*) FROM employees WHERE DEPARTMENT_ID = ?";

        this.statement = this.connection.createStatement();
        PreparedStatement pstmt = this.connection.prepareStatement(sql);
        pstmt.setInt(1, DeptId);
        boolean status = pstmt.execute();

        if (status) {
            result = pstmt.getResultSet();
        }

        return result;
    }

    public ResultSet countRowsInTable(String tabName) throws SQLException {
        ResultSet result = null;

        String sql = "SELECT COUNT(*) FROM ? ";

        this.statement = this.connection.createStatement();
        result = this.statement.executeQuery(sql.replace("?",tabName));

        while (result.next()) {
            System.out.println(result.getInt(1));
        }

        return result;
    }

    public DatabaseMetaData displayElementsInDB(String DbName) throws SQLException {
        DatabaseMetaData db = this.connection.getMetaData();
        ResultSet rs = db.getTables(DbName, null, "%", null);
        List<String> TabNames = new ArrayList<String>();
        while (rs.next()) {
            TabNames.add(rs.getString(3));
        }
        for(int i = 0; i < TabNames.size();i ++){
            this.countRowsInTable(TabNames.get(i));
        }
        return db;
    }

}
