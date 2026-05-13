/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.repository;

import com.practicas.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private List<Usuario> listaUsuarios;

    public UsuarioRepository() {
        listaUsuarios = new ArrayList<>();
    }

    public void guardar(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    public List<Usuario> listar() {
        return listaUsuarios;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    
    public void eliminar(String email) {
        Usuario usuario = buscarPorEmail(email);

        if (usuario != null) {
            usuario.setActivo(false);
        }
    }
}