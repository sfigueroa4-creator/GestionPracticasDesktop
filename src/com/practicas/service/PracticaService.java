/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.service;

import com.practicas.model.Practica;
import com.practicas.repository.PracticaRepository;
import java.util.List;


public class PracticaService {

    private PracticaRepository practicaRepository;

    public PracticaService() {
        practicaRepository = new PracticaRepository();
    }

    public void crearPractica(Practica practica) {
        if (practica.getNombre() == null || practica.getNombre().isEmpty()) {
            System.out.println("Error: nombre obligatorio.");
            return;
        }

        if (practica.getNumeroPractica() < 1 ||
            practica.getNumeroPractica() > 8) {

            System.out.println("Error: número de práctica inválido.");
            return;
        }

        practicaRepository.guardar(practica);
        System.out.println("Práctica registrada correctamente.");
    }

    public void mostrarPracticas() {
        List<Practica> lista = practicaRepository.listar();

        for (Practica p : lista) {
            System.out.println(
                "Práctica " +
                p.getNumeroPractica() +
                ": " +
                p.getNombre()
            );
        }
    }


    public Practica buscarPractica(int numero) {
        return practicaRepository.buscarPorNumero(numero);
    }
}
