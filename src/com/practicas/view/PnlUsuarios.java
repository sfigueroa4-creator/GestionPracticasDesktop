/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;

    public PnlUsuarios() {

        setLayout(new BorderLayout());

        // =========================
        // FORMULARIO
        // =========================

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(4,2,5,5));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtEmail = new JTextField();

        panelFormulario.add(new JLabel("Nombre"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido"));
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("Email"));
        panelFormulario.add(txtEmail);

        JButton btnGuardar = new JButton("Guardar");

        panelFormulario.add(btnGuardar);

        add(panelFormulario, BorderLayout.NORTH);

        // =========================
        // TABLA
        // =========================

        modelo = new DefaultTableModel();

        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Email");

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // =========================
        // EVENTO
        // =========================

        btnGuardar.addActionListener(e -> guardarUsuario());
    }

    private void guardarUsuario() {

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String email = txtEmail.getText();

        modelo.addRow(new Object[]{
            nombre,
            apellido,
            email
        });

        limpiar();
    }

    private void limpiar() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
    }
}