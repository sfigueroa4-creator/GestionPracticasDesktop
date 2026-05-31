package com.practicas.service;

import com.practicas.dao.UsuarioDAO;
import com.practicas.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthService {

    private UsuarioDAO usuarioDAO;

    public AuthService(Connection conn) {
        usuarioDAO = new UsuarioDAO(conn);
    }

    public boolean registrarUsuario(Usuario usuario) {
        try {
            usuarioDAO.insertar(usuario);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario login(String email, String password) {
        try {
            return usuarioDAO.login(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios() {
        try {
            return usuarioDAO.listar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
