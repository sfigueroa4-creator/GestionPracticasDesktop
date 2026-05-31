/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.practicas.model.Usuario;
import com.practicas.model.RolUsuario;
import com.practicas.service.AuthService;

public class PnlUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JComboBox<com.practicas.model.RolUsuario> cbRol;

    private AuthService authService;
    private FrmPrincipal frmPrincipal;

    public PnlUsuarios() {
        this(null);
    }

    public PnlUsuarios(FrmPrincipal frmPrincipal) {
        this.frmPrincipal = frmPrincipal;

        setLayout(new BorderLayout());

        try {

            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection(
                    "GestionP",
                    "GestionP"
                );

            authService = new AuthService(con);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error de conexión: " + e.getMessage()
            );
        }

        JPanel panelFormulario = new JPanel();

        panelFormulario.setLayout(
            new GridLayout(5,2,5,5)
        );

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtEmail = new JTextField();
        cbRol = new JComboBox<>(com.practicas.model.RolUsuario.values());

        panelFormulario.add(new JLabel("Nombre"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido"));
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("Email"));
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Rol"));
        panelFormulario.add(cbRol);

        JButton btnGuardar = new JButton("Guardar");

        panelFormulario.add(btnGuardar);

        add(panelFormulario, BorderLayout.NORTH);

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
        modelo.addColumn("Apellido");
        modelo.addColumn("Email");
        modelo.addColumn("Rol");

        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);


        btnGuardar.addActionListener(
            e -> guardarUsuario()
        );

        cargarUsuarios();
    }

    private void guardarUsuario() {

        try {

            Usuario usuario = new Usuario();

            usuario.setNombre(txtNombre.getText());
            usuario.setApellido(txtApellido.getText());
            usuario.setEmail(txtEmail.getText());

            usuario.setPasswordHash("123456");

            usuario.setRol(
                (com.practicas.model.RolUsuario) cbRol.getSelectedItem()
            );

            usuario.setActivo(true);

            boolean guardado =
                authService.registrarUsuario(usuario);

            if (guardado) {

                cargarUsuarios();

                // Recargar comboboxes de otros paneles que usan usuarios
                if (frmPrincipal != null) {
                    if (frmPrincipal.getPnlGrupos() != null) {
                        frmPrincipal.getPnlGrupos().recargar();
                    }
                    if (frmPrincipal.getPnlInscripciones() != null) {
                        frmPrincipal.getPnlInscripciones().recargar();
                    }
                }

                JOptionPane.showMessageDialog(
                    this,
                    "Usuario guardado correctamente"
                );

                limpiar();

            } else {

                JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar"
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage()
            );
        }
    }

    public void recargar() {
        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection(
                    "GestionP",
                    "GestionP"
                );
            authService = new AuthService(con);
            cargarUsuarios();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error recargando usuarios: " + e.getMessage()
            );
        }
    }

    private void cargarUsuarios() {

        try {

            modelo.setRowCount(0);

            for (Usuario u : authService.obtenerUsuarios()) {

                modelo.addRow(new Object[] {
                    u.getNombre(),
                    u.getApellido(),
                    u.getEmail(),
                    u.getRol() != null ? u.getRol().name() : ""
                });
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error cargando usuarios"
            );
        }
    }

    private void limpiar() {

        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        cbRol.setSelectedIndex(0);
    }
}