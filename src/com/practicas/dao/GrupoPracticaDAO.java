/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.dao;

import com.practicas.model.GrupoPractica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoPracticaDAO {

    private Connection conn;

    public GrupoPracticaDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // INSERTAR
    // =========================
    public void insertar(GrupoPractica g) throws SQLException {

        if (g == null) {

            throw new SQLException(
                "El grupo es null"
            );
        }

        String sql = """
            INSERT INTO GRUPO_PRACTICA
            (ID_GRUPO, NOMBRE, ID_PRACTICA,
             ID_DOCENTE, ID_INSTITUCION,
             CUPO_MAXIMO, OBSERVACIONES)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setInt(1, g.getIdGrupo());

            ps.setString(2, g.getNombre());

            ps.setInt(
                3,
                g.getPractica() != null
                    ? g.getPractica().getIdPractica()
                    : 0
            );

            ps.setInt(
                4,
                g.getDocenteAsesor() != null
                    ? g.getDocenteAsesor().getIdUsuario()
                    : 0
            );

            ps.setInt(
                5,
                g.getInstitucion() != null
                    ? g.getInstitucion().getIdInstitucion()
                    : 0
            );

            ps.setInt(6, g.getCupoMaximo());

            ps.setString(7, g.getObservaciones());

            ps.executeUpdate();
        }
    }

    // =========================
    // LISTAR
    // =========================
    public List<GrupoPractica> listar()
            throws SQLException {

        List<GrupoPractica> lista =
            new ArrayList<>();

        String sql = """
            SELECT
                ID_GRUPO,
                NOMBRE,
                CUPO_MAXIMO,
                OBSERVACIONES
            FROM GRUPO_PRACTICA
        """;

        try (PreparedStatement ps =
                 conn.prepareStatement(sql);

             ResultSet rs =
                 ps.executeQuery()) {

            while (rs.next()) {

                GrupoPractica g =
                    new GrupoPractica();

                g.setIdGrupo(
                    rs.getInt("ID_GRUPO")
                );

                g.setNombre(
                    rs.getString("NOMBRE")
                );

                g.setCupoMaximo(
                    rs.getInt("CUPO_MAXIMO")
                );

                g.setObservaciones(
                    rs.getString("OBSERVACIONES")
                );

                lista.add(g);
            }
        }

        return lista;
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public void actualizar(GrupoPractica g)
            throws SQLException {

        String sql = """
            UPDATE GRUPO_PRACTICA
            SET NOMBRE = ?,
                CUPO_MAXIMO = ?,
                OBSERVACIONES = ?
            WHERE ID_GRUPO = ?
        """;

        try (PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setString(1, g.getNombre());

            ps.setInt(2, g.getCupoMaximo());

            ps.setString(
                3,
                g.getObservaciones()
            );

            ps.setInt(4, g.getIdGrupo());

            ps.executeUpdate();
        }
    }

    // =========================
    // ELIMINAR
    // =========================
    public void eliminar(int idGrupo)
            throws SQLException {

        String sql = """
            DELETE FROM GRUPO_PRACTICA
            WHERE ID_GRUPO = ?
        """;

        try (PreparedStatement ps =
                 conn.prepareStatement(sql)) {

            ps.setInt(1, idGrupo);

            ps.executeUpdate();
        }
    }
}