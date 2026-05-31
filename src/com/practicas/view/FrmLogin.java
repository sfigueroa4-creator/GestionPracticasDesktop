/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import com.practicas.model.Usuario;
import com.practicas.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class FrmLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private AuthService authService;

    public FrmLogin() {

        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(
            getClass().getResource(
                "/com/practicas/Img/IconProyecto.png"
            )
        );

        setIconImage(icon.getImage());

        setLayout(new BorderLayout());

        try {

            Connection con =
                com.practicas.util.DatabaseConnection
                    .getConnection(
                        "GestionP",
                        "GestionP"
                    );

            authService =
                new AuthService(con);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error conexión: "
                + e.getMessage()
            );
        }

        JPanel panel =
            new JPanel(
                new GridLayout(
                    3,
                    2,
                    5,
                    5
                )
            );

        txtEmail =
            new JTextField();

        txtPassword =
            new JPasswordField();

        panel.add(
            new JLabel("Usuario")
        );

        panel.add(
            txtEmail
        );

        panel.add(
            new JLabel("Contraseña")
        );

        panel.add(
            txtPassword
        );

        JButton btnLogin =
            new JButton("Ingresar");

        panel.add(
            new JLabel()
        );

        panel.add(
            btnLogin
        );

        add(
            panel,
            BorderLayout.CENTER
        );

        btnLogin.addActionListener(
            e -> login()
        );
    }

    private void login() {

        try {

            String email =
                txtEmail.getText();

            String password =
                String.valueOf(
                    txtPassword.getPassword()
                );

            Usuario usuario =
                authService.login(
                    email,
                    password
                );

            if (usuario != null) {

                JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido "
                    + usuario.getNombre()
                );

                FrmPrincipal frm =
                    new FrmPrincipal(usuario);

                frm.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "Credenciales inválidas"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: "
                + e.getMessage()
            );
        }
    }
}