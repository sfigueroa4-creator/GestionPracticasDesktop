/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;

public class FrmPrincipal extends JFrame {

    private JPanel panelContenido;

    public FrmPrincipal() {

        setTitle("Sistema de Gestión de Prácticas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(10,1));
        panelMenu.setPreferredSize(new Dimension(200,600));

        JButton btnUsuarios = new JButton("Usuarios");
        JButton btnPracticas = new JButton("Prácticas");
        JButton btnInstituciones = new JButton("Instituciones");
        JButton btnGrupos = new JButton("Grupos");
        JButton btnInscripciones = new JButton("Inscripciones");

        panelMenu.add(btnUsuarios);
        panelMenu.add(btnPracticas);
        panelMenu.add(btnInstituciones);
        panelMenu.add(btnGrupos);
        panelMenu.add(btnInscripciones);

        add(panelMenu, BorderLayout.WEST);

        panelContenido = new JPanel();
        panelContenido.setLayout(new CardLayout());

        panelContenido.add(new PnlUsuarios(), "usuarios");
        panelContenido.add(new PnlPracticas(), "practicas");
        panelContenido.add(new PnlInstituciones(), "instituciones");
        panelContenido.add(new PnlGrupos(), "grupos");
        panelContenido.add(new PnlInscripciones(), "inscripciones");

        add(panelContenido, BorderLayout.CENTER);

        btnUsuarios.addActionListener(e -> {
            mostrarPanel("usuarios");
        });

        btnPracticas.addActionListener(e -> {
            mostrarPanel("practicas");
        });
        btnInstituciones.addActionListener(e -> {
            mostrarPanel("instituciones");
        });

        btnGrupos.addActionListener(e -> {
            mostrarPanel("grupos");
        });

        btnInscripciones.addActionListener(e -> {
            mostrarPanel("inscripciones");
        });
    }
    

    private void mostrarPanel(String nombre) {

        CardLayout cl =
            (CardLayout)(panelContenido.getLayout());

        cl.show(panelContenido, nombre);
    }
}
