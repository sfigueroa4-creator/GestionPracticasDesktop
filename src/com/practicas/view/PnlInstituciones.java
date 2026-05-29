/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlInstituciones extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtNit;
    private JTextField txtTelefono;

    public PnlInstituciones() {

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(
                new GridLayout(4,2,5,5)
        );

        txtNombre = new JTextField();
        txtNit = new JTextField();
        txtTelefono = new JTextField();

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("NIT"));
        formulario.add(txtNit);

        formulario.add(new JLabel("Teléfono"));
        formulario.add(txtTelefono);

        JButton btnGuardar = new JButton("Guardar");

        formulario.add(btnGuardar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel();

        modelo.addColumn("Nombre");
        modelo.addColumn("NIT");
        modelo.addColumn("Teléfono");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarInstitucion());
    }

    private void guardarInstitucion() {

        modelo.addRow(new Object[] {
            txtNombre.getText(),
            txtNit.getText(),
            txtTelefono.getText()
        });

        limpiar();
    }

    private void limpiar() {

        txtNombre.setText("");
        txtNit.setText("");
        txtTelefono.setText("");
    }
}