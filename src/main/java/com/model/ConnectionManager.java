package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection connection;
    private static String connectionString = "jdbc:postgresql://dumbo.db.elephantsql.com/xvwxelvu";
    private static String username = "xvwxelvu";
    private static String password = "PHm0jpvwUdwMtElhSwmHCA85Ucl9G6l8";

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(connectionString, username, password);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("ConnectionManager-error: " + e);
        } catch (SQLException e) {
            System.out.println("ConnectionManager-error: " + e);
        }
        return null;
    }

    @Override
    public void finalize() {
        try {
            ConnectionManager.getConnection().close();
        } catch (SQLException e) {
            System.out.println("ConnectionManager-error: " + e);
        }
    }
}
