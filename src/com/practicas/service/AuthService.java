/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.service;

import com.practicas.model.Usuario;
import com.practicas.repository.UsuarioRepository;


public class AuthService {

    private UsuarioRepository usuarioRepository;

    public AuthService() {
        usuarioRepository = new UsuarioRepository();
    }


    public void registrarUsuario(Usuario usuario) {
        usuarioRepository.guardar(usuario);
    }


    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null &&
            usuario.getPasswordHash().equals(password) &&
            usuario.isActivo()) {

            return usuario;
        }

        return null;
    }

    public void mostrarUsuarios() {
        for (Usuario u : usuarioRepository.listar()) {
            System.out.println(
                u.getNombreCompleto() +
                " - " +
                u.getEmail()
            );
        }
    }
}
