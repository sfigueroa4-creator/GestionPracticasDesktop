/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection(
            String usuario,
            String password,
            String servicio) throws SQLException {

        String url = "jdbc:oracle:thin:@//localhost:1521/"+servicio;

        return DriverManager.getConnection(url, usuario, password);
    }
}