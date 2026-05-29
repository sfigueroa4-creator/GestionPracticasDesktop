/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlInscripciones extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtEstudiante;
    private JTextField txtGrupo;
    private JTextField txtHoras;

    public PnlInscripciones() {

        setLayout(new BorderLayout());

        JPanel formulario = new JPanel(
                new GridLayout(4,2,5,5)
        );

        txtEstudiante = new JTextField();
        txtGrupo = new JTextField();
        txtHoras = new JTextField();

        formulario.add(new JLabel("Estudiante"));
        formulario.add(txtEstudiante);

        formulario.add(new JLabel("Grupo"));
        formulario.add(txtGrupo);

        formulario.add(new JLabel("Horas"));
        formulario.add(txtHoras);

        JButton btnGuardar = new JButton("Guardar");

        formulario.add(btnGuardar);

        add(formulario, BorderLayout.NORTH);

modelo = new DefaultTableModel() {

    @Override
    public boolean isCellEditable(
            int row,
            int column
    ) {

        return false;
    }
};

        modelo.addColumn("Estudiante");
        modelo.addColumn("Grupo");
        modelo.addColumn("Horas");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarInscripcion());
    }

    private void guardarInscripcion() {

        modelo.addRow(new Object[] {
            txtEstudiante.getText(),
            txtGrupo.getText(),
            txtHoras.getText()
        });

        limpiar();
    }

    private void limpiar() {

        txtEstudiante.setText("");
        txtGrupo.setText("");
        txtHoras.setText("");
    }
}