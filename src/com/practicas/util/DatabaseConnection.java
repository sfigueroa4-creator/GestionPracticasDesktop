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

    /** Conexion usando el usuario Oracle correspondiente al rol de la aplicacion. */
    public static Connection getConnectionPorRol(RolUsuario rol) throws SQLException {
        String[] credenciales = credencialesPorRol(rol);
        return getConnection(credenciales[0], credenciales[1]);
    }

    /**
     * Retorna {usuario, contrasena} Oracle para cada rol de la aplicacion.
     * Estas credenciales corresponden a los usuarios creados por DatabaseInstaller.
     */
    public static String[] credencialesPorRol(RolUsuario rol) {
        switch (rol) {
            case ADMIN:        return new String[]{"USR_ADMIN",       "Adm1nGP#"};
            case DIRECTOR:     return new String[]{"USR_DIRECTOR",    "Dir3ctGP#"};
            case COORDINADOR:  return new String[]{"USR_COORDINADOR", "Coord3GP#"};
            case DOCENTE:      return new String[]{"USR_DOCENTE",     "Doc3ntGP#"};
            case ESTUDIANTE:   return new String[]{"USR_ESTUDIANTE",  "Est4dGP#"};
            case INSTITUCION:  return new String[]{"USR_INSTITUCION", "Inst1tGP#"};
            default:           return new String[]{"GestionP",        "GestionP"};
        }
    }
}
