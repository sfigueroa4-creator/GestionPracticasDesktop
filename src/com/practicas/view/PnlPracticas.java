/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.practicas.model.Practica;
import com.practicas.service.PracticaService;


public class PnlPracticas extends JPanel {

    private PracticaService practicaService;
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

modelo = new DefaultTableModel() {

    @Override
    public boolean isCellEditable(
            int row,
            int column
    ) {

        return false;
    }
};

        modelo.addColumn("Nombre");
        modelo.addColumn("Número");
        modelo.addColumn("Horas");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);


        btnGuardar.addActionListener(e -> guardarPractica());
        
        try {

    java.sql.Connection con =
        com.practicas.util.DatabaseConnection.getConnection(
            "GestionP",
            "GestionP",
            "orcl"
        );

    practicaService =
        new PracticaService(con);
    cargarPracticas();

} catch (Exception e) {

    JOptionPane.showMessageDialog(
        this,
        "Error de conexión: " +
        e.getMessage()
    );
}
    }

   private void guardarPractica() {

    try {

        Practica practica =
            new Practica();

        practica.setNombre(
            txtNombre.getText()
        );

        practica.setNumeroPractica(
            Integer.parseInt(
                txtNumero.getText()
            )
        );

        practica.setHorasReglamentarias(
            Integer.parseInt(
                txtHoras.getText()
            )
        );
        
        practica.setEstado(
        Practica.EstadoPractica.ACTIVA
        );
        practicaService.crearPractica(
            practica
        );

        modelo.addRow(new Object[] {

            practica.getNombre(),
            practica.getNumeroPractica(),
            practica.getHorasReglamentarias()
        });

        JOptionPane.showMessageDialog(
            this,
            "Práctica guardada"
        );

        limpiar();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Error: " + e.getMessage()
        );
    }
    
}
   
   private void cargarPracticas() {

    try {

        modelo.setRowCount(0);

        for (Practica p :
            practicaService.obtenerPracticas()) {

            modelo.addRow(new Object[] {

                p.getNombre(),
                p.getNumeroPractica(),
                p.getHorasReglamentarias()
            });
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Error cargando prácticas"
        );
    }
}

    private void limpiar() {

        txtNombre.setText("");
        txtNumero.setText("");
        txtHoras.setText("");
    }
}