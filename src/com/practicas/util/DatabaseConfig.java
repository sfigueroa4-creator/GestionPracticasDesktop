/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.util;

import java.io.*;
import java.util.Properties;

public class DatabaseConfig {

    private static final String FILE =
        "config.properties";

    public static String getHost() {

        Properties p = load();

        return p.getProperty(
            "host",
            "localhost"
        );
    }

    public static String getPuerto() {

        Properties p = load();

        return p.getProperty(
            "port",
            "1521"
        );
    }

    public static String getServicio() {

        Properties p = load();

        return p.getProperty(
            "service",
            "XEPDB1"
        );
    }

    public static void guardar(
        String host,
        String puerto,
        String servicio
    ) {

        try {

            Properties p =
                new Properties();

            p.setProperty(
                "host",
                host
            );

            p.setProperty(
                "port",
                puerto
            );

            p.setProperty(
                "service",
                servicio
            );

            FileOutputStream out =
                new FileOutputStream(FILE);

            p.store(
                out,
                "Configuracion Oracle"
            );

            out.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static Properties load() {

        Properties p =
            new Properties();

        try {

            File f =
                new File(FILE);

            if (f.exists()) {

                FileInputStream in =
                    new FileInputStream(f);

                p.load(in);

                in.close();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return p;
    }
}