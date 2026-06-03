package com.practicas.util;

import java.io.*;
import java.util.Properties;

public class DatabaseConfig {

    private static final String FILE = resolverRuta("config.properties");

    private static String resolverRuta(String nombreArchivo) {
        try {
            java.net.URL url = DatabaseConfig.class.getProtectionDomain().getCodeSource().getLocation();
            java.io.File jar = new java.io.File(url.toURI());
            return jar.getParentFile().getAbsolutePath() + java.io.File.separator + nombreArchivo;
        } catch (Exception e) {
            return nombreArchivo;
        }
    }

    public static String getHost() {
        return load().getProperty("host", "192.168.254.215");
    }

    public static String getPuerto() {
        return load().getProperty("port", "1521");
    }

    public static String getServicio() {
        return load().getProperty("service", "orcl");
    }

    public static void guardar(String host, String puerto, String servicio) {
        try {
            Properties p = new Properties();
            p.setProperty("host", host);
            p.setProperty("port", puerto);
            p.setProperty("service", servicio);
            FileOutputStream out = new FileOutputStream(FILE);
            p.store(out, "Configuracion Oracle");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties load() {
        Properties p = new Properties();
        try {
            File f = new File(FILE);
            if (f.exists()) {
                FileInputStream in = new FileInputStream(f);
                p.load(in);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
