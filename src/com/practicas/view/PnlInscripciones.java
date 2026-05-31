/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import com.practicas.dao.InscripcionGrupoDAO;
import com.practicas.model.GrupoPractica;
import com.practicas.model.InscripcionGrupo;
import com.practicas.model.Usuario;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlInscripciones extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private InscripcionGrupoDAO inscripcionDAO;

    private JComboBox<Usuario> cbEstudiante;
    private JComboBox<GrupoPractica> cbGrupo;

    private JTextField txtHoras;

    public PnlInscripciones() {

        setLayout(new BorderLayout());

        try {

            java.sql.Connection con =
                com.practicas.util.DatabaseConnection
                    .getConnection(
                        "GestionP",
                        "GestionP"
                    );

            inscripcionDAO =
                new InscripcionGrupoDAO(con);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error conexión: " +
                e.getMessage()
            );
        }

        JPanel formulario =
            new JPanel(
                new GridLayout(4, 2, 5, 5)
            );

        cbEstudiante = new JComboBox<>();
        cbGrupo = new JComboBox<>();
        txtHoras = new JTextField();

        formulario.add(
            new JLabel("Estudiante")
        );

        formulario.add(
            cbEstudiante
        );

        formulario.add(
            new JLabel("Grupo")
        );

        formulario.add(
            cbGrupo
        );

        formulario.add(
            new JLabel("Horas")
        );

        formulario.add(
            txtHoras
        );

        JButton btnGuardar =
            new JButton("Guardar");

        formulario.add(btnGuardar);

        add(
            formulario,
            BorderLayout.NORTH
        );

        modelo =
            new DefaultTableModel() {

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

        add(
            new JScrollPane(tabla),
            BorderLayout.CENTER
        );

        btnGuardar.addActionListener(
            e -> guardarInscripcion()
        );

        cargarEstudiantes();
        cargarGrupos();
        cargarInscripciones();
    }

    private void guardarInscripcion() {

        try {

            InscripcionGrupo i =
                new InscripcionGrupo();

            i.setEstudiante(
                (Usuario)
                cbEstudiante.getSelectedItem()
            );

            i.setGrupo(
                (GrupoPractica)
                cbGrupo.getSelectedItem()
            );

            i.setHorasCumplidas(
                Double.parseDouble(
                    txtHoras.getText()
                )
            );

            i.setEstado(
                InscripcionGrupo
                    .EstadoInscripcion
                    .ACTIVO
            );

            inscripcionDAO.insertar(i);

            modelo.addRow(
                new Object[] {

                    cbEstudiante
                        .getSelectedItem(),

                    cbGrupo
                        .getSelectedItem(),

                    txtHoras.getText()
                }
            );

            JOptionPane.showMessageDialog(
                this,
                "Inscripción guardada"
            );

            limpiar();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: "
                + e.getMessage()
            );
        }
    }

    private void cargarEstudiantes() {

        try {

            cbEstudiante.removeAllItems();

            for (
                Usuario u :
                inscripcionDAO
                    .obtenerEstudiantes()
            ) {

                cbEstudiante.addItem(u);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error cargando estudiantes: "
                + e.getMessage()
            );
        }
    }

    private void cargarGrupos() {

        try {

            cbGrupo.removeAllItems();

            for (
                GrupoPractica g :
                inscripcionDAO
                    .obtenerGrupos()
            ) {

                cbGrupo.addItem(g);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error cargando grupos: "
                + e.getMessage()
            );
        }
    }

    private void limpiar() {

        txtHoras.setText("");

        if (
            cbEstudiante.getItemCount()
            > 0
        ) {

            cbEstudiante
                .setSelectedIndex(0);
        }

        if (
            cbGrupo.getItemCount()
            > 0
        ) {

            cbGrupo
                .setSelectedIndex(0);
        }
    }
private void cargarInscripciones() {

    try {

        modelo.setRowCount(0);

        for (
            InscripcionGrupo i :
            inscripcionDAO.listar()
        ) {

            modelo.addRow(
                new Object[] {

                    i.getEstudiante()
                        .getNombre(),

                    i.getGrupo()
                        .getNombre(),

                    i.getHorasCumplidas()
                }
            );
        }

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            this,
            "Error cargando inscripciones: "
            + e.getMessage()
        );
    }
}
}