package com.practicas.view;

import com.practicas.dao.ReportesDAO;
import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;
import com.practicas.util.DatabaseConnection;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlReportes extends JPanel {

    private final Usuario usuario;

    private JTabbedPane tabbedPane;

    // Tab 0 - Avance por estudiante
    private DefaultTableModel modeloAvance;

    // Tab 1 - Ocupacion de grupos
    private DefaultTableModel modeloOcupacion;

    // Tab 2 - Estudiantes por practica
    private DefaultTableModel modeloEstudiantes;

    // Tab 3 - Auditoria contrasenas (solo ADMIN)
    private DefaultTableModel modeloAuditoria;

    public PnlReportes(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // --- Tab: Avance por estudiante ---
        modeloAvance = new DefaultTableModel(
                new String[]{"Estudiante", "Grupo", "Horas Cumplidas", "Horas Reglamentarias", "Porcentaje"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabbedPane.addTab("Avance por estudiante", new JScrollPane(new JTable(modeloAvance)));

        // --- Tab: Ocupacion de grupos ---
        modeloOcupacion = new DefaultTableModel(
                new String[]{"Grupo", "Cupo Maximo", "Inscritos Activos", "Cupos Disponibles"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabbedPane.addTab("Ocupacion de grupos", new JScrollPane(new JTable(modeloOcupacion)));

        // --- Tab: Estudiantes por practica ---
        modeloEstudiantes = new DefaultTableModel(
                new String[]{"Practica", "Grupo", "Estudiante", "Apellido", "Estado", "Horas"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabbedPane.addTab("Estudiantes por practica", new JScrollPane(new JTable(modeloEstudiantes)));

        // --- Tab: Auditoria contrasenas (solo ADMIN) ---
        if (usuario != null && usuario.getRol() == RolUsuario.ADMIN) {
            modeloAuditoria = new DefaultTableModel(
                    new String[]{"Nombre", "Apellido", "Email", "Fecha Cambio"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            tabbedPane.addTab("Auditoria contrasenas", new JScrollPane(new JTable(modeloAuditoria)));
        }

        add(tabbedPane, BorderLayout.CENTER);

        // --- Boton Actualizar ---
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.addActionListener(e -> cargarPestanaActiva());

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnActualizar);
        add(panelSur, BorderLayout.SOUTH);

        // Carga inicial
        recargar();
    }

    /** Recarga todos los reportes. */
    public void recargar() {
        cargarAvance();
        cargarOcupacion();
        cargarEstudiantes();
        if (modeloAuditoria != null) {
            cargarAuditoria();
        }
    }

    /** Recarga solo la pestaña actualmente visible. */
    private void cargarPestanaActiva() {
        int idx = tabbedPane.getSelectedIndex();
        switch (idx) {
            case 0 -> cargarAvance();
            case 1 -> cargarOcupacion();
            case 2 -> cargarEstudiantes();
            case 3 -> { if (modeloAuditoria != null) cargarAuditoria(); }
            default -> { /* nada */ }
        }
    }

    private void cargarAvance() {
        try (Connection conn = DatabaseConnection.getConnection("GestionP", "GestionP")) {
            ReportesDAO dao = new ReportesDAO(conn);
            List<Object[]> datos = dao.reporteAvancePorEstudiante();
            modeloAvance.setRowCount(0);
            for (Object[] fila : datos) {
                modeloAvance.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando avance por estudiante:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarOcupacion() {
        try (Connection conn = DatabaseConnection.getConnection("GestionP", "GestionP")) {
            ReportesDAO dao = new ReportesDAO(conn);
            List<Object[]> datos = dao.reporteOcupacionGrupos();
            modeloOcupacion.setRowCount(0);
            for (Object[] fila : datos) {
                modeloOcupacion.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando ocupacion de grupos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEstudiantes() {
        try (Connection conn = DatabaseConnection.getConnection("GestionP", "GestionP")) {
            ReportesDAO dao = new ReportesDAO(conn);
            List<Object[]> datos = dao.reporteEstudiantesPorPractica();
            modeloEstudiantes.setRowCount(0);
            for (Object[] fila : datos) {
                modeloEstudiantes.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando estudiantes por practica:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarAuditoria() {
        try (Connection conn = DatabaseConnection.getConnection("GestionP", "GestionP")) {
            ReportesDAO dao = new ReportesDAO(conn);
            List<Object[]> datos = dao.reporteAuditoriaPassword();
            modeloAuditoria.setRowCount(0);
            for (Object[] fila : datos) {
                modeloAuditoria.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando auditoria de contrasenas:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
