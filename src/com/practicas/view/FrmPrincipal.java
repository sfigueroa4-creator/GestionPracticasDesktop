/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.practicas.util.DatabaseConnection;

public class FrmPrincipal extends JFrame {

    private JPanel panelContenido;
    private Connection conn;

    public FrmPrincipal() {

        try {
            conn = DatabaseConnection.getConnection(
                    "GestionP",
                    "GestionP",
                    "XEPDB1"
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de conexión: " + e.getMessage()
            );
            return;
        }

        setTitle("Sistema de Gestión de Prácticas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/com/practicas/Img/IconProyecto.png")
        );

        setIconImage(icon.getImage());
        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(10, 1, 10, 10));
        panelMenu.setPreferredSize(new Dimension(220, 600));
        panelMenu.setBackground(new Color(44, 62, 80));
        panelMenu.setBorder(
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        );

        JButton btnUsuarios = crearBoton("Usuarios");
        JButton btnPracticas = crearBoton("Prácticas");
        JButton btnInstituciones = crearBoton("Instituciones");
        JButton btnGrupos = crearBoton("Grupos");
        JButton btnInscripciones = crearBoton("Inscripciones");

        panelMenu.add(btnUsuarios);
        panelMenu.add(btnPracticas);
        panelMenu.add(btnInstituciones);
        panelMenu.add(btnGrupos);
        panelMenu.add(btnInscripciones);

        add(panelMenu, BorderLayout.WEST);

        panelContenido = new JPanel(new CardLayout());

        panelContenido.add(new PnlUsuarios(), "usuarios");
        panelContenido.add(new PnlPracticas(), "practicas");
        panelContenido.add(new PnlInstituciones(), "instituciones");
        panelContenido.add(new PnlGrupos(conn), "grupos");
        panelContenido.add(new PnlInscripciones(), "inscripciones");

        add(panelContenido, BorderLayout.CENTER);

        btnUsuarios.addActionListener(e -> mostrarPanel("usuarios"));
        btnPracticas.addActionListener(e -> mostrarPanel("practicas"));
        btnInstituciones.addActionListener(e -> mostrarPanel("instituciones"));
        btnGrupos.addActionListener(e -> mostrarPanel("grupos"));
        btnInscripciones.addActionListener(e -> mostrarPanel("inscripciones"));
    }

    private void mostrarPanel(String nombre) {
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, nombre);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }
}
