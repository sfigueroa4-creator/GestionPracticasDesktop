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
    private int idSeleccionado = -1;

    private JTextField txtNombre;
    private JTextField txtNumero;
    private JTextField txtHoras;

    public PnlPracticas() {

        setLayout(new BorderLayout());

        // FORMULARIO
        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));

        txtNombre = new JTextField();
        txtNumero = new JTextField();
        txtHoras = new JTextField();

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Número"));
        formulario.add(txtNumero);

        formulario.add(new JLabel("Horas"));
        formulario.add(txtHoras);
        formulario.add(new JLabel(" "));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");

        formulario.add(btnGuardar);
        formulario.add(btnActualizar);
        formulario.add(btnEliminar);

        add(formulario, BorderLayout.NORTH);

        // TABLA
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Número");
        modelo.addColumn("Horas");

        tabla = new JTable(modelo);

        // Listener de selección
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // EVENTOS
        btnGuardar.addActionListener(e -> guardarPractica());
        btnActualizar.addActionListener(e -> actualizarPractica());
        btnEliminar.addActionListener(e -> eliminarPractica());

        // CONEXIÓN
        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection(
                    "GestionP",
                    "GestionP",
                    "XEPDB1"
                );

            practicaService = new PracticaService(con);

            cargarPracticas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error de conexión: " + e.getMessage()
            );
        }
    }

    private void guardarPractica() {
        try {
            Practica practica = new Practica();

            practica.setNombre(txtNombre.getText());
            practica.setNumeroPractica(
                Integer.parseInt(txtNumero.getText())
            );
            practica.setHorasReglamentarias(
                Integer.parseInt(txtHoras.getText())
            );

            practica.setEstado(Practica.EstadoPractica.ACTIVA);

            practicaService.crearPractica(practica);

            cargarPracticas();
            limpiar();

            JOptionPane.showMessageDialog(
                this,
                "Práctica guardada"
            );
            practica.setDescripcion("Práctica académica");
            practica.setTipoPractica("GENERAL");
            practica.setFechaCreacion(java.time.LocalDateTime.now());
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

            for (Practica p : practicaService.obtenerPracticas()) {
                modelo.addRow(new Object[]{
                    p.getIdPractica(),
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

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);

            txtNombre.setText(
                modelo.getValueAt(fila, 1).toString()
            );

            txtNumero.setText(
                modelo.getValueAt(fila, 2).toString()
            );

            txtHoras.setText(
                modelo.getValueAt(fila, 3).toString()
            );
        }
    }

    private void actualizarPractica() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una práctica"
                );
                return;
            }

            Practica practica = new Practica();
            practica.setDescripcion("Práctica académica");
            practica.setTipoPractica("GENERAL");
            practica.setIdPractica(idSeleccionado);
            practica.setNombre(txtNombre.getText());
            practica.setNumeroPractica(
                Integer.parseInt(txtNumero.getText())
            );
            practica.setHorasReglamentarias(
                Integer.parseInt(txtHoras.getText())
            );
            practica.setEstado(
                Practica.EstadoPractica.ACTIVA
            );

            practicaService.actualizarPractica(practica);

            cargarPracticas();
            limpiar();

            JOptionPane.showMessageDialog(
                this,
                "Práctica actualizada"
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );
            
        }
    }

    private void eliminarPractica() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una práctica"
                );
                return;
            }

            practicaService.eliminarPractica(idSeleccionado);

            cargarPracticas();
            limpiar();

            JOptionPane.showMessageDialog(
                this,
                "Práctica eliminada"
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtNumero.setText("");
        txtHoras.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}