/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import com.practicas.util.DatabaseConfig;
import com.practicas.util.DatabaseInstaller;
import com.practicas.util.DatabaseConnection;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

public class FrmConfiguracionBD
extends JDialog {

    private JTextField txtHost;
    private JTextField txtPuerto;
    private JTextField txtServicio;

    public FrmConfiguracionBD(
        Frame parent
    ) {

        super(
            parent,
            true
        );

        setTitle(
            "Configuración BD"
        );

        setSize(
            400,
            300
        );

        setLocationRelativeTo(
            parent
        );

        setLayout(
            new GridLayout(
                5,
                2,
                5,
                5
            )
        );

        txtHost =
            new JTextField(
                DatabaseConfig.getHost()
            );

        txtPuerto =
            new JTextField(
                DatabaseConfig.getPuerto()
            );

        txtServicio =
            new JTextField(
                DatabaseConfig.getServicio()
            );

        add(
            new JLabel("Host")
        );

        add(txtHost);

        add(
            new JLabel("Puerto")
        );

        add(txtPuerto);

        add(
            new JLabel("Servicio")
        );

        add(txtServicio);

        JButton btnGuardar =
            new JButton(
                "Guardar"
            );

        JButton btnCancelar =
            new JButton(
                "Cancelar"
            );

        JButton btnCrear =
            new JButton(
                "Crear tablas"
            );

        JButton btnBorrar =
            new JButton(
                "Borrar tablas"
            );

        add(btnGuardar);
        add(btnCancelar);

        add(btnCrear);
        add(btnBorrar);

        btnGuardar.addActionListener(
            e -> guardar()
        );

        btnCancelar.addActionListener(
            e -> dispose()
        );

        btnCrear.addActionListener(
            e -> crearTablas()
        );

        btnBorrar.addActionListener(
            e -> borrarTablas()
        );
    }

    private void guardar() {

        DatabaseConfig.guardar(
            txtHost.getText(),
            txtPuerto.getText(),
            txtServicio.getText()
        );

        JOptionPane.showMessageDialog(
            this,
            "Configuración guardada"
        );
    }

    private void crearTablas() {

        try {

            Connection con =
                DatabaseConnection
                    .getConnection(
                        "GestionP",
                        "GestionP"
                    );

            DatabaseInstaller installer =
                new DatabaseInstaller(
                    con
                );

            installer.crearTablas();

            JOptionPane.showMessageDialog(
                this,
                "Tablas creadas correctamente"
            );

            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: "
                + e.getMessage()
            );
        }
    }

    private void borrarTablas() {

        int r1 =
            JOptionPane.showConfirmDialog(
                this,
                "Te recomendamos hacer una copia de seguridad antes de continuar.\n¿Desea continuar?",
                "Advertencia",
                JOptionPane.YES_NO_OPTION
            );

        if (
            r1 !=
            JOptionPane.YES_OPTION
        ) {
            return;
        }

        int r2 =
            JOptionPane.showConfirmDialog(
                this,
                "Esta acción eliminará TODA la información.\n¿Está completamente seguro?",
                "Confirmación final",
                JOptionPane.YES_NO_OPTION
            );

        if (
            r2 !=
            JOptionPane.YES_OPTION
        ) {
            return;
        }

        try {

            Connection con =
                DatabaseConnection
                    .getConnection(
                        "GestionP",
                        "GestionP"
                    );

            DatabaseInstaller installer =
                new DatabaseInstaller(
                    con
                );

            installer.borrarTablas();

            JOptionPane.showMessageDialog(
                this,
                "Tablas eliminadas correctamente"
            );

            con.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: "
                + e.getMessage()
            );
        }
    }
}