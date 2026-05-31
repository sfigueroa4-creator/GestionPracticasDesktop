package com.practicas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection(String usuario, String password) throws SQLException {
        String url = "jdbc:oracle:thin:@//"
            + DatabaseConfig.getHost() + ":"
            + DatabaseConfig.getPuerto() + "/"
            + DatabaseConfig.getServicio();
        return DriverManager.getConnection(url, usuario, password);
    }
}
