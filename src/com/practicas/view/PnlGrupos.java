/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlGrupos extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtCupo;

    public PnlGrupos() {

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(
                new GridLayout(3,2,5,5)
        );

        txtNombre = new JTextField();
        txtCupo = new JTextField();

        formulario.add(new JLabel("Nombre grupo"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Cupo máximo"));
        formulario.add(txtCupo);

        JButton btnGuardar = new JButton("Guardar");

        formulario.add(btnGuardar);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel();

        modelo.addColumn("Grupo");
        modelo.addColumn("Cupo");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarGrupo());
    }

    private void guardarGrupo() {

        modelo.addRow(new Object[] {
            txtNombre.getText(),
            txtCupo.getText()
        });

        limpiar();
    }

    private void limpiar() {

        txtNombre.setText("");
        txtCupo.setText("");
    }
}
