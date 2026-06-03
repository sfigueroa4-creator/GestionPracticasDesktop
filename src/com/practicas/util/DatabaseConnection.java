package com.practicas.util;

import com.practicas.model.RolUsuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    /** Conexion directa con usuario y contrasena explícitos. Usada internamente. */
    public static Connection getConnection(String usuario, String password) throws SQLException {
        String url = "jdbc:oracle:thin:@//"
            + DatabaseConfig.getHost() + ":"
            + DatabaseConfig.getPuerto() + "/"
            + DatabaseConfig.getServicio();
        return DriverManager.getConnection(url, usuario, password);
    }

    /**
     * Conexion para la sesion activa de la aplicacion.
     * Usa el usuario owner (GestionP) para todos los roles; el control de acceso
     * se aplica a nivel de aplicacion segun el RolUsuario del usuario autenticado.
     */
    public static Connection getConnectionPorRol(RolUsuario rol) throws SQLException {
        return getConnection("GestionP", "GestionP");
    }
}
