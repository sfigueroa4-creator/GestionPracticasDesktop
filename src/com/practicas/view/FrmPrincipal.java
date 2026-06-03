/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.view;

import java.awt.*;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import com.practicas.util.DatabaseConnection;
import com.practicas.model.Usuario;
import com.practicas.model.RolUsuario;

public class FrmPrincipal extends JFrame {

    private JPanel panelContenido;
    private Connection conn;
    private Usuario usuarioActual;

    private PnlUsuarios pnlUsuarios;
    private PnlPracticas pnlPracticas;
    private PnlInstituciones pnlInstituciones;
    private PnlGrupos pnlGrupos;
    private PnlInscripciones pnlInscripciones;
    private PnlReportes pnlReportes;

    public FrmPrincipal(Usuario usuario) {
        this(usuario, null);
    }

    public FrmPrincipal(Usuario usuario, Connection conexionRol) {
        this.usuarioActual = usuario;

        try {
            conn = (conexionRol != null)
                ? conexionRol
                : DatabaseConnection.getConnectionPorRol(usuario.getRol());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexion: " + e.getMessage());
            return;
        }

        setTitle("Gestion de Practicas  " + usuario.getNombreCompleto()
                + " [" + usuario.getRol().name() + "]");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/com/practicas/Img/IconProyecto.png")
        );
        setIconImage(icon.getImage());
        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(10, 1, 10, 10));
        panelMenu.setPreferredSize(new Dimension(220, 600));
        panelMenu.setBackground(new Color(44, 62, 80));
        panelMenu.setBorder(
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        );

        add(panelMenu, BorderLayout.WEST);

        panelContenido = new JPanel(new CardLayout());
        add(panelContenido, BorderLayout.CENTER);

        RolUsuario rol = usuarioActual.getRol();

        if (rol == RolUsuario.ADMIN) {
            pnlUsuarios = new PnlUsuarios(this);
            panelContenido.add(pnlUsuarios, "usuarios");
            JButton btn = crearBoton("Usuarios");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("usuarios"));
        }
        
        if (rol == RolUsuario.ADMIN || rol == RolUsuario.DIRECTOR
                || rol == RolUsuario.COORDINADOR) {
            pnlPracticas = new PnlPracticas(this);
            panelContenido.add(pnlPracticas, "practicas");
            JButton btn = crearBoton("Practicas");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("practicas"));
        }

        if (rol == RolUsuario.ADMIN || rol == RolUsuario.DIRECTOR
                || rol == RolUsuario.COORDINADOR) {
            pnlInstituciones = new PnlInstituciones(this);
            panelContenido.add(pnlInstituciones, "instituciones");
            JButton btn = crearBoton("Instituciones");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("instituciones"));
        }

        if (rol == RolUsuario.ADMIN || rol == RolUsuario.DIRECTOR
                || rol == RolUsuario.COORDINADOR || rol == RolUsuario.DOCENTE) {
            pnlGrupos = new PnlGrupos(conn, this);
            panelContenido.add(pnlGrupos, "grupos");
            JButton btn = crearBoton("Grupos");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("grupos"));
        }

        if (rol != RolUsuario.INSTITUCION) {
            pnlInscripciones = new PnlInscripciones(usuarioActual);
            panelContenido.add(pnlInscripciones, "inscripciones");
            JButton btn = crearBoton("Inscripciones");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("inscripciones"));
        }

        if (rol != RolUsuario.INSTITUCION) {
            pnlReportes = new PnlReportes(usuarioActual);
            panelContenido.add(pnlReportes, "reportes");
            JButton btn = crearBoton("Reportes");
            panelMenu.add(btn);
            btn.addActionListener(e -> mostrarPanel("reportes"));
        }

        if (rol == RolUsuario.ADMIN) {
            JButton btn = crearBoton("Configuracion BD");
            panelMenu.add(btn);
            btn.addActionListener(e -> {
                FrmConfiguracionBD dlg = new FrmConfiguracionBD(this);
                dlg.setVisible(true);
            });
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void recargarTodo() {
        if (pnlUsuarios != null)      pnlUsuarios.recargar();
        if (pnlPracticas != null)     pnlPracticas.recargar();
        if (pnlInstituciones != null) pnlInstituciones.recargar();
        if (pnlGrupos != null)        pnlGrupos.recargar();
        if (pnlInscripciones != null) pnlInscripciones.recargar();
        if (pnlReportes != null)      pnlReportes.recargar();
    }

    public PnlGrupos getPnlGrupos() {
        return pnlGrupos;
    }

    public PnlInscripciones getPnlInscripciones() {
        return pnlInscripciones;
    }

    public PnlReportes getPnlReportes() {
        return pnlReportes;
    }

    private void mostrarPanel(String nombre) {
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, nombre);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
