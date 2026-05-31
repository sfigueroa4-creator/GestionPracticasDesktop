/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public FrmConfiguracionBD(FrmPrincipal parent) {
        super(parent, true);
        this.frmPrincipal = parent;
        this.usuarioActual = parent.getUsuarioActual();
        construirUI(parent);
    }

    private void construirUI(Frame parent) {

        setTitle("Configuracion BD");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(5, 2, 5, 5));

        txtHost    = new JTextField(DatabaseConfig.getHost());
        txtPuerto  = new JTextField(DatabaseConfig.getPuerto());
        txtServicio = new JTextField(DatabaseConfig.getServicio());

        add(new JLabel("Host"));
        add(txtHost);

        add(new JLabel("Puerto"));
        add(txtPuerto);

        add(new JLabel("Servicio"));
        add(txtServicio);

        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnCrear    = new JButton("Crear tablas");
        JButton btnBorrar   = new JButton("Borrar tablas");

        // Las operaciones destructivas se muestran en rojo para dejar claro su impacto
        btnBorrar.setBackground(new Color(192, 57, 43));
        btnBorrar.setForeground(Color.WHITE);
        btnBorrar.setOpaque(true);

        add(btnGuardar);
        add(btnCancelar);
        add(btnCrear);
        add(btnBorrar);

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        btnCrear.addActionListener(e -> crearTablas());
        btnBorrar.addActionListener(e -> borrarTablas());
    }

    /** Verifica que el usuario actual sea ADMIN. Muestra error y retorna false si no lo es. */
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

    private void guardar() {
        if (!verificarAdmin()) return;

        DatabaseConfig.guardar(
            txtHost.getText(),
            txtPuerto.getText(),
            txtServicio.getText()
        );

        JOptionPane.showMessageDialog(this, "Configuracion guardada");
    }

    private void crearTablas() {
        if (!verificarAdmin()) return;

        try {
            Connection con = DatabaseConnection.getConnection("GestionP", "GestionP");
            new DatabaseInstaller(con).crearTablas();

            JOptionPane.showMessageDialog(this, "Tablas creadas correctamente");
            con.close();

            if (frmPrincipal != null) {
                frmPrincipal.recargarTodo();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void borrarTablas() {
        if (!verificarAdmin()) return;

        int r1 = JOptionPane.showConfirmDialog(
            this,
            "Te recomendamos hacer una copia de seguridad antes de continuar.\n¿Desea continuar?",
            "Advertencia",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (r1 != JOptionPane.YES_OPTION) return;

        int r2 = JOptionPane.showConfirmDialog(
            this,
            "Esta accion eliminara TODA la informacion.\n¿Esta completamente seguro?",
            "Confirmacion final",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (r2 != JOptionPane.YES_OPTION) return;

        try {
            Connection con = DatabaseConnection.getConnection("GestionP", "GestionP");
            new DatabaseInstaller(con).borrarTablas();

            JOptionPane.showMessageDialog(this, "Tablas eliminadas correctamente");
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
