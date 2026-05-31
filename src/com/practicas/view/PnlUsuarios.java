package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;
import com.practicas.service.AuthService;

public class PnlUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEmail;
    private JComboBox<RolUsuario> cbRol;

    private AuthService authService;
    private FrmPrincipal frmPrincipal;

    private int idSeleccionado = -1;

    public PnlUsuarios() {
        this(null);
    }

    public PnlUsuarios(FrmPrincipal frmPrincipal) {
        this.frmPrincipal = frmPrincipal;

        setLayout(new BorderLayout());

        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection("GestionP", "GestionP");
            authService = new AuthService(con);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexion: " + e.getMessage());
        }

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));

        txtNombre   = new JTextField();
        txtApellido = new JTextField();
        txtEmail    = new JTextField();
        cbRol       = new JComboBox<>(RolUsuario.values());

        panelFormulario.add(new JLabel("Nombre"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Apellido"));
        panelFormulario.add(txtApellido);

        panelFormulario.add(new JLabel("Email"));
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Rol"));
        panelFormulario.add(cbRol);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnPassword = new JButton("Cambiar contrasena");

        panelFormulario.add(btnGuardar);
        panelFormulario.add(btnPassword);

        add(panelFormulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Email");
        modelo.addColumn("Rol");

        tabla = new JTable(modelo);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardarUsuario());
        btnPassword.addActionListener(e -> cambiarPassword());

        cargarUsuarios();
    }

    private void guardarUsuario() {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(txtNombre.getText());
            usuario.setApellido(txtApellido.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setPasswordHash("123456");
            usuario.setRol((RolUsuario) cbRol.getSelectedItem());
            usuario.setActivo(true);

            if (authService.registrarUsuario(usuario)) {
                cargarUsuarios();
                if (frmPrincipal != null) {
                    if (frmPrincipal.getPnlGrupos() != null)
                        frmPrincipal.getPnlGrupos().recargar();
                    if (frmPrincipal.getPnlInscripciones() != null)
                        frmPrincipal.getPnlInscripciones().recargar();
                }
                JOptionPane.showMessageDialog(this, "Usuario guardado correctamente");
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void cambiarPassword() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario en la tabla primero.",
                "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPasswordField txtNueva    = new JPasswordField();
        JPasswordField txtConfirma = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Nueva contrasena:"));
        panel.add(txtNueva);
        panel.add(new JLabel("Confirmar:"));
        panel.add(txtConfirma);

        int resultado = JOptionPane.showConfirmDialog(
            this, panel,
            "Cambiar contrasena de: " + txtNombre.getText() + " " + txtApellido.getText(),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) return;

        String nueva    = new String(txtNueva.getPassword());
        String confirma = new String(txtConfirma.getPassword());

        if (nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contrasena no puede estar vacia.");
            return;
        }

        if (!nueva.equals(confirma)) {
            JOptionPane.showMessageDialog(this,
                "Las contrasenas no coinciden.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.cambiarPassword(idSeleccionado, nueva)) {
            JOptionPane.showMessageDialog(this, "Contrasena actualizada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo actualizar la contrasena.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtApellido.setText(modelo.getValueAt(fila, 2).toString());
            txtEmail.setText(modelo.getValueAt(fila, 3).toString());
            String rol = modelo.getValueAt(fila, 4).toString();
            cbRol.setSelectedItem(RolUsuario.valueOf(rol));
        }
    }

    public void recargar() {
        try {
            java.sql.Connection con =
                com.practicas.util.DatabaseConnection.getConnection("GestionP", "GestionP");
            authService = new AuthService(con);
            cargarUsuarios();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error recargando usuarios: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        try {
            modelo.setRowCount(0);
            for (Usuario u : authService.obtenerUsuarios()) {
                modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getEmail(),
                    u.getRol() != null ? u.getRol().name() : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando usuarios");
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        cbRol.setSelectedIndex(0);
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}
