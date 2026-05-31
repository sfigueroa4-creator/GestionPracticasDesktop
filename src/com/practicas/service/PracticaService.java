package com.practicas.service;

import com.practicas.dao.PracticaDAO;
import com.practicas.model.Practica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PracticaService {

    private PracticaDAO practicaDAO;

    public PracticaService(Connection conn) {
        practicaDAO = new PracticaDAO(conn);
    }

    public void crearPractica(Practica practica) throws Exception {
        validarPractica(practica);
        practicaDAO.insertar(practica);
    }

    public List<Practica> obtenerPracticas() {
        try {
            return practicaDAO.listar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Practica buscarPractica(int numero) {
        try {
            for (Practica p : practicaDAO.listar()) {
                if (p.getNumeroPractica() == numero) return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actualizarPractica(Practica practica) throws Exception {
        validarPractica(practica);
        practicaDAO.actualizar(practica);
    }

    public void eliminarPractica(int idPractica) throws Exception {
        practicaDAO.eliminar(idPractica);
    }

    private void validarPractica(Practica practica) throws Exception {
        if (practica.getNombre() == null || practica.getNombre().trim().isEmpty())
            throw new Exception("El nombre es obligatorio");
        if (practica.getNumeroPractica() < 1 || practica.getNumeroPractica() > 8)
            throw new Exception("El numero debe estar entre 1 y 8");
        if (practica.getHorasReglamentarias() <= 0)
            throw new Exception("Las horas deben ser mayores que 0");
    }
}
