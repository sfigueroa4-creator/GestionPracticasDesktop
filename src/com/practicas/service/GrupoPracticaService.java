package com.practicas.service;

import com.practicas.dao.GrupoPracticaDAO;
import com.practicas.dao.InstitucionReceptoraDAO;
import com.practicas.dao.PracticaDAO;
import com.practicas.dao.UsuarioDAO;
import com.practicas.model.GrupoPractica;
import com.practicas.model.InstitucionReceptora;
import com.practicas.model.Practica;
import com.practicas.model.Usuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoPracticaService {

    private GrupoPracticaDAO grupoDAO;
    private PracticaDAO practicaDAO;
    private UsuarioDAO usuarioDAO;
    private InstitucionReceptoraDAO institucionDAO;

    public GrupoPracticaService(Connection conn) {
        grupoDAO      = new GrupoPracticaDAO(conn);
        practicaDAO   = new PracticaDAO(conn);
        usuarioDAO    = new UsuarioDAO(conn);
        institucionDAO = new InstitucionReceptoraDAO(conn);
    }

    public void crearGrupo(GrupoPractica grupo) throws Exception {
        validarGrupo(grupo);
        grupoDAO.insertar(grupo);
    }

    public List<GrupoPractica> obtenerGrupos() {
        try { return grupoDAO.listar(); } catch (SQLException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public void actualizarGrupo(GrupoPractica grupo) throws Exception {
        validarGrupo(grupo);
        grupoDAO.actualizar(grupo);
    }

    public void eliminarGrupo(int idGrupo) throws Exception {
        grupoDAO.eliminar(idGrupo);
    }

    public List<Practica> obtenerPracticas() {
        try { return practicaDAO.listar(); } catch (SQLException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public List<Usuario> obtenerDocentes() {
        try {
            List<Usuario> docentes = new ArrayList<>();
            docentes.addAll(usuarioDAO.listarPorRol("DOCENTE"));
            docentes.addAll(usuarioDAO.listarPorRol("COORDINADOR"));
            docentes.addAll(usuarioDAO.listarPorRol("DIRECTOR"));
            docentes.addAll(usuarioDAO.listarPorRol("ADMIN"));
            return docentes;
        } catch (SQLException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public List<InstitucionReceptora> obtenerInstituciones() {
        try { return institucionDAO.listar(); } catch (SQLException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    private void validarGrupo(GrupoPractica grupo) throws Exception {
        if (grupo.getNombre() == null || grupo.getNombre().trim().isEmpty())
            throw new Exception("El nombre es obligatorio");
        if (grupo.getCupoMaximo() <= 0)
            throw new Exception("El cupo debe ser mayor que 0");
        if (grupo.getPractica() == null)
            throw new Exception("Debe seleccionar una practica");
        if (grupo.getDocenteAsesor() == null)
            throw new Exception("Debe seleccionar un docente");
        if (grupo.getInstitucion() == null)
            throw new Exception("Debe seleccionar una institucion");
    }
}
