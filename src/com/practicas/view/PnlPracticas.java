/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlPracticas extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtNumero;
    private JTextField txtHoras;

    public PnlPracticas() {

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(
                new GridLayout(4,2,5,5)
        );

        txtNombre = new JTextField();
        txtNumero = new JTextField();
        txtHoras = new JTextField();

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Número"));
        formulario.add(txtNumero);

        formulario.add(new JLabel("Horas"));
        formulario.add(txtHoras);

        JButton btnGuardar = new JButton("Guardar");

        formulario.add(btnGuardar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel();

        modelo.addColumn("Nombre");
        modelo.addColumn("Número");
        modelo.addColumn("Horas");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);


        btnGuardar.addActionListener(e -> guardarPractica());
    }

    private void guardarPractica() {

        modelo.addRow(new Object[] {
            txtNombre.getText(),
            txtNumero.getText(),
            txtHoras.getText()
        });

        limpiar();
    }

    private void limpiar() {

        txtNombre.setText("");
        txtNumero.setText("");
        txtHoras.setText("");
    }
}