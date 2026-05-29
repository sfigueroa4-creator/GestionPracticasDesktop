/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.service;

import com.practicas.dao.PracticaDAO;
import com.practicas.model.Practica;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

public class PracticaService {

    private PracticaDAO practicaDAO;

    public PracticaService(Connection conn) {

        practicaDAO = new PracticaDAO(conn);
    }

    public void crearPractica(Practica practica) {

        if (practica.getNombre() == null ||
            practica.getNombre().isEmpty()) {

            System.out.println(
                "Error: nombre obligatorio."
            );

            return;
        }

        if (practica.getNumeroPractica() < 1 ||
            practica.getNumeroPractica() > 8) {

            System.out.println(
                "Error: número inválido."
            );

            return;
        }

        try {

            practicaDAO.insertar(practica);

            System.out.println(
                "Práctica registrada correctamente."
            );

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void mostrarPracticas() {

        try {

            List<Practica> lista =
                practicaDAO.listar();

            for (Practica p : lista) {

                System.out.println(
                    p.getNombre()
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public Practica buscarPractica(int numero) {

        try {

            for (Practica p :
                    practicaDAO.listar()) {

                if (p.getNumeroPractica() == numero) {
                    return p;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
    public List<Practica> obtenerPracticas() {

    try {

        return practicaDAO.listar();

    } catch (SQLException e) {

        e.printStackTrace();
    }

    return new ArrayList<>();
}
}