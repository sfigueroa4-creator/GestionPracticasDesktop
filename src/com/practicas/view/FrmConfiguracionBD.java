package com.practicas.view;

import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;
import com.practicas.util.DatabaseConfig;
import com.practicas.util.DatabaseInstaller;
import com.practicas.util.DatabaseConnection;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

public class FrmConfiguracionBD extends JDialog {

    private JTextField txtHost;
    private JTextField txtPuerto;
    private JTextField txtServicio;

    private FrmPrincipal frmPrincipal;
    private Usuario usuarioActual;

    /** Abre el dialogo completo (conexion + tablas) desde FrmPrincipal. */
    public FrmConfiguracionBD(FrmPrincipal parent) {
        super(parent, true);
        this.frmPrincipal = parent;
        this.usuarioActual = parent.getUsuarioActual();
        construirUI(parent, false);
    }


    public FrmConfiguracionBD(Window parent) {
        super(parent, ModalityType.APPLICATION_MODAL);
        construirUI(null, true);
    }

    private void construirUI(Frame frameParent, boolean soloConexion) {
        setTitle(soloConexion ? "Configuración de conexión" : "Configuración BD");
        setLocationRelativeTo(frameParent);
        setResizable(false);

        txtHost     = new JTextField(DatabaseConfig.getHost());
        txtPuerto   = new JTextField(DatabaseConfig.getPuerto());
        txtServicio = new JTextField(DatabaseConfig.getServicio());

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardar(soloConexion));
        btnCancelar.addActionListener(e -> dispose());

        if (soloConexion) {
            JButton btnCrear = new JButton("Crear tablas (primera vez)");
            btnCrear.addActionListener(e -> crearTablasSinAuth());

            setLayout(new GridLayout(5, 2, 8, 8));
            setSize(400, 240);
            getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel("Host"));
            add(txtHost);
            add(new JLabel("Puerto"));
            add(txtPuerto);
            add(new JLabel("Servicio"));
            add(txtServicio);
            add(btnGuardar);
            add(btnCancelar);
            add(new JLabel("¿Primera instalación?"));
            add(btnCrear);
        } else {
            setLayout(new GridLayout(5, 2, 8, 8));
            setSize(400, 280);

            JButton btnCrear  = new JButton("Crear tablas");
            JButton btnBorrar = new JButton("Borrar tablas");

            btnBorrar.setBackground(new Color(192, 57, 43));
            btnBorrar.setForeground(Color.WHITE);
            btnBorrar.setOpaque(true);

            btnCrear.addActionListener(e -> crearTablas());
            btnBorrar.addActionListener(e -> borrarTablas());

            add(new JLabel("Host"));
            add(txtHost);
            add(new JLabel("Puerto"));
            add(txtPuerto);
            add(new JLabel("Servicio"));
            add(txtServicio);
            add(btnGuardar);
            add(btnCancelar);
            add(btnCrear);
            add(btnBorrar);
        }
    }

    private boolean verificarAdmin() {
        if (usuarioActual == null || usuarioActual.getRol() != RolUsuario.ADMIN) {
            JOptionPane.showMessageDialog(
                this,
                "Acceso denegado. Solo un administrador puede realizar esta operacion.",
                "Sin permisos",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
        return true;
    }

    private void guardar(boolean soloConexion) {
        if (!soloConexion && !verificarAdmin()) return;

        DatabaseConfig.guardar(
            txtHost.getText().trim(),
            txtPuerto.getText().trim(),
            txtServicio.getText().trim()
        );

        JOptionPane.showMessageDialog(this, "Configuración guardada");
        dispose();
    }

    /** Crea las tablas sin requerir sesion iniciada. Solo para la primera instalacion. */
    private void crearTablasSinAuth() {
        // Guardar configuracion de conexion antes de intentar
        DatabaseConfig.guardar(
            txtHost.getText().trim(),
            txtPuerto.getText().trim(),
            txtServicio.getText().trim()
        );

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Esto creará las tablas y el usuario administrador inicial (GestionP/GestionP).\n" +
            "Solo debe ejecutarse una vez. ¿Continuar?",
            "Primera instalación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Connection con = DatabaseConnection.getConnection("GestionP", "GestionP");
            new DatabaseInstaller(con).crearTablas();
            JOptionPane.showMessageDialog(this,
                "Tablas creadas correctamente.\nAhora puede iniciar sesión con GestionP / GestionP.",
                "Instalación completada", JOptionPane.INFORMATION_MESSAGE);
            con.close();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al crear tablas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTablas() {
        if (!verificarAdmin()) return;

        try {
            Connection con = DatabaseConnection.getConnection("GestionP", "GestionP");
            new DatabaseInstaller(con).crearTablas();
            JOptionPane.showMessageDialog(this, "Tablas creadas correctamente");
            con.close();
            if (frmPrincipal != null) frmPrincipal.recargarTodo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarTablas() {
        if (!verificarAdmin()) return;

        int r1 = JOptionPane.showConfirmDialog(
            this,
            "Te recomendamos hacer una copia de seguridad antes de continuar.\n¿Desea continuar?",
            "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
        if (r1 != JOptionPane.YES_OPTION) return;

        int r2 = JOptionPane.showConfirmDialog(
            this,
            "Esta accion eliminara TODA la informacion.\n¿Esta completamente seguro?",
            "Confirmacion final", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
        if (r2 != JOptionPane.YES_OPTION) return;

        try {
            Connection con = DatabaseConnection.getConnection("GestionP", "GestionP");
            new DatabaseInstaller(con).borrarTablas();
            JOptionPane.showMessageDialog(this, "Tablas eliminadas correctamente");
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
