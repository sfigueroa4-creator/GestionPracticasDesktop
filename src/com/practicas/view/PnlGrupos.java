/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import com.practicas.model.GrupoPractica;
import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;
import com.practicas.service.GrupoPracticaService;
import com.practicas.model.Practica;
import com.practicas.model.InstitucionReceptora;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnlGrupos extends JPanel {

    private GrupoPracticaService grupoService;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtCupo;
    private JComboBox<Practica> cbPractica;
    private JComboBox<Usuario> cbDocente;
    private JComboBox<InstitucionReceptora> cbInstitucion;
    private FrmPrincipal frmPrincipal;

    private static boolean puedeEscribir(Usuario u) {
        if (u == null) return false;
        return u.getRol() == RolUsuario.ADMIN
            || u.getRol() == RolUsuario.DIRECTOR
            || u.getRol() == RolUsuario.COORDINADOR;
    }

    public PnlGrupos(Connection conn) {
        this(conn, null);
    }

    public PnlGrupos(Connection conn, FrmPrincipal frmPrincipal) {
        this.frmPrincipal = frmPrincipal;

        Usuario usuario = frmPrincipal != null ? frmPrincipal.getUsuarioActual() : null;
        boolean escritura = puedeEscribir(usuario);

        grupoService = new GrupoPracticaService(conn);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtNombre = new JTextField(20);
        txtCupo   = new JTextField(20);

        cbPractica   = new JComboBox<>();
        cbDocente    = new JComboBox<>();
        cbInstitucion = new JComboBox<>();

        Dimension size = new Dimension(250, 25);
        cbPractica.setPreferredSize(size);
        cbDocente.setPreferredSize(size);
        cbInstitucion.setPreferredSize(size);

        JButton btnGuardar = new JButton("Guardar");

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Nombre grupo:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombre, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Practica:"), gbc);
        gbc.gridx = 1;
        formulario.add(cbPractica, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Docente:"), gbc);
        gbc.gridx = 1;
        formulario.add(cbDocente, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Institucion:"), gbc);
        gbc.gridx = 1;
        formulario.add(cbInstitucion, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formulario.add(new JLabel("Cupo maximo:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtCupo, gbc);
        y++;

        gbc.gridx = 1; gbc.gridy = y;
        formulario.add(btnGuardar, gbc);

        txtNombre.setEditable(escritura);
        txtCupo.setEditable(escritura);
        cbPractica.setEnabled(escritura);
        cbDocente.setEnabled(escritura);
        cbInstitucion.setEnabled(escritura);
        btnGuardar.setEnabled(escritura);

        add(formulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("Grupo");
        modelo.addColumn("Cupo");

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarGrupo());

        cargarPracticas();
        cargarDocentes();
        cargarInstituciones();
        cargarGrupos();
    }

    public void recargar() {
        cargarPracticas();
        cargarDocentes();
        cargarInstituciones();
        cargarGrupos();
    }

    private void guardarGrupo() {
        try {
            GrupoPractica grupo = new GrupoPractica();
            grupo.setNombre(txtNombre.getText());
            grupo.setCupoMaximo(Integer.parseInt(txtCupo.getText()));
            grupo.setPractica((Practica) cbPractica.getSelectedItem());
            grupo.setDocenteAsesor((Usuario) cbDocente.getSelectedItem());
            grupo.setInstitucion((InstitucionReceptora) cbInstitucion.getSelectedItem());

            grupoService.crearGrupo(grupo);
            cargarGrupos();
            limpiar();

            if (frmPrincipal != null && frmPrincipal.getPnlInscripciones() != null) {
                frmPrincipal.getPnlInscripciones().recargar();
            }

            JOptionPane.showMessageDialog(this, "Grupo guardado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarGrupos() {
        modelo.setRowCount(0);
        for (GrupoPractica g : grupoService.obtenerGrupos()) {
            modelo.addRow(new Object[]{g.getNombre(), g.getCupoMaximo()});
        }
    }

    private void cargarPracticas() {
        try {
            cbPractica.removeAllItems();
            for (Practica p : grupoService.obtenerPracticas()) cbPractica.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando practicas: " + e.getMessage());
        }
    }

    private void cargarDocentes() {
        try {
            cbDocente.removeAllItems();
            for (Usuario u : grupoService.obtenerDocentes()) cbDocente.addItem(u);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando docentes: " + e.getMessage());
        }
    }

    private void cargarInstituciones() {
        try {
            cbInstitucion.removeAllItems();
            for (InstitucionReceptora i : grupoService.obtenerInstituciones()) cbInstitucion.addItem(i);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando instituciones: " + e.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtCupo.setText("");
    }
}
