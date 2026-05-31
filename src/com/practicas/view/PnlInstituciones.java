/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.practicas.dao.InstitucionReceptoraDAO;
import com.practicas.model.InstitucionReceptora;
import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;

public class PnlInstituciones extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtNit;
    private JTextField txtTelefono;
    private InstitucionReceptoraDAO institucionDAO;
    private FrmPrincipal frmPrincipal;

    private static boolean puedeEscribir(Usuario u) {
        if (u == null) return false;
        return u.getRol() == RolUsuario.ADMIN
            || u.getRol() == RolUsuario.DIRECTOR;
    }

    public PnlInstituciones() {
        this(null);
    }

    public PnlInstituciones(FrmPrincipal frmPrincipal) {
        this.frmPrincipal = frmPrincipal;

        Usuario usuario = frmPrincipal != null ? frmPrincipal.getUsuarioActual() : null;
        boolean escritura = puedeEscribir(usuario);

        setLayout(new BorderLayout());

        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection("GestionP", "GestionP");
            institucionDAO = new InstitucionReceptoraDAO(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error conexion: " + e.getMessage());
        }

        JPanel formulario = new JPanel(new GridLayout(4, 2, 5, 5));

        txtNombre   = new JTextField();
        txtNit      = new JTextField();
        txtTelefono = new JTextField();

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("NIT"));
        formulario.add(txtNit);

        formulario.add(new JLabel("Telefono"));
        formulario.add(txtTelefono);

        JButton btnGuardar = new JButton("Guardar");
        formulario.add(btnGuardar);

        txtNombre.setEditable(escritura);
        txtNit.setEditable(escritura);
        txtTelefono.setEditable(escritura);
        btnGuardar.setEnabled(escritura);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Nombre");
        modelo.addColumn("NIT");
        modelo.addColumn("Telefono");

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarInstitucion());
        cargarInstituciones();
    }

    public void recargar() {
        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection("GestionP", "GestionP");
            institucionDAO = new InstitucionReceptoraDAO(con);
            cargarInstituciones();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error recargando instituciones: " + e.getMessage());
        }
    }

    private void guardarInstitucion() {
        try {
            InstitucionReceptora i = new InstitucionReceptora();
            i.setNombre(txtNombre.getText());
            i.setNit(txtNit.getText());
            i.setTelefono(txtTelefono.getText());

            institucionDAO.insertar(i);
            cargarInstituciones();
            limpiar();

            if (frmPrincipal != null && frmPrincipal.getPnlGrupos() != null) {
                frmPrincipal.getPnlGrupos().recargar();
            }

            JOptionPane.showMessageDialog(this, "Institucion guardada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cargarInstituciones() {
        try {
            modelo.setRowCount(0);
            for (InstitucionReceptora i : institucionDAO.listar()) {
                modelo.addRow(new Object[]{i.getNombre(), i.getNit(), i.getTelefono()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando instituciones");
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtNit.setText("");
        txtTelefono.setText("");
    }
}
