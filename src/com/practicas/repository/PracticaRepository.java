/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.repository;

import com.practicas.model.Practica;
import java.util.ArrayList;
import java.util.List;


public class PracticaRepository {

    private List<Practica> listaPracticas;

    public PracticaRepository() {
        listaPracticas = new ArrayList<>();
    }

    public void guardar(Practica practica) {
        listaPracticas.add(practica);
    }


    public List<Practica> listar() {
        return listaPracticas;
    }

    public Practica buscarPorNumero(int numeroPractica) {
        for (Practica p : listaPracticas) {
            if (p.getNumeroPractica() == numeroPractica) {
                return p;
            }
        }
        return null;
    }

    public void eliminar(int numeroPractica) {
        Practica practica = buscarPorNumero(numeroPractica);

        if (practica != null) {
            listaPracticas.remove(practica);
        }
    }
}
