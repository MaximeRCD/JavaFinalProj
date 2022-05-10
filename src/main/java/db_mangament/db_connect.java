package db_mangament;

import java.sql.*;

public class db_connect {
    public static void main(String[] args) throws SQLException {
        DatabaseOperations a = new DatabaseOperations("localhost:3306", "users_db", "root", "root");
        DatabaseMetaData rst = a.displayElementsInDB("users_db");

    }
}


