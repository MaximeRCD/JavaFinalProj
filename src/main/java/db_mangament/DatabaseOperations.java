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

}
