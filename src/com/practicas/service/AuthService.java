/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.service;

import com.practicas.dao.UsuarioDAO;
import com.practicas.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;

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

        System.out.println("Error al registrar usuario:");
        e.printStackTrace();

        return false;
    }
}
    public void mostrarUsuarios() {

        try {

            for (Usuario u : usuarioDAO.listar()) {

                System.out.println(
                    u.getNombreCompleto()
                    + " - " +
                    u.getEmail()
                );
            }

        } catch (SQLException e) {

            System.out.println("Error al listar usuarios:");
            e.printStackTrace();
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
}