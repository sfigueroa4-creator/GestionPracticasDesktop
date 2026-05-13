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
    // CREATE
    // =========================
    public void insertar(GrupoPractica g) throws SQLException {

        String sql = """
            INSERT INTO GRUPO_PRACTICA
            (ID_GRUPO, NOMBRE, ID_PRACTICA, ID_DOCENTE, ID_INSTITUCION, CUPO_MAXIMO, OBSERVACIONES)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, g.getIdGrupo());
        ps.setString(2, g.getNombre());

        ps.setInt(3, g.getPractica().getIdPractica());
        ps.setInt(4, g.getDocenteAsesor().getIdUsuario());
        ps.setInt(5, g.getInstitucion().getIdInstitucion());

        ps.setInt(6, g.getCupoMaximo());
        ps.setString(7, g.getObservaciones());

        ps.executeUpdate();
        ps.close();
    }

    // =========================
    // READ ALL
    // =========================
    public List<GrupoPractica> listar() throws SQLException {

        List<GrupoPractica> lista = new ArrayList<>();

        String sql = "SELECT * FROM GRUPO_PRACTICA";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            GrupoPractica g = new GrupoPractica();

            g.setIdGrupo(rs.getInt("ID_GRUPO"));
            g.setNombre(rs.getString("NOMBRE"));
            g.setCupoMaximo(rs.getInt("CUPO_MAXIMO"));
            g.setObservaciones(rs.getString("OBSERVACIONES"));

            // ⚠️ Aquí solo cargas IDs (no objetos completos)
            lista.add(g);
        }

        rs.close();
        ps.close();

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    public void actualizar(GrupoPractica g) throws SQLException {

        String sql = """
            UPDATE GRUPO_PRACTICA
            SET NOMBRE = ?,
                CUPO_MAXIMO = ?,
                OBSERVACIONES = ?
            WHERE ID_GRUPO = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, g.getNombre());
        ps.setInt(2, g.getCupoMaximo());
        ps.setString(3, g.getObservaciones());
        ps.setInt(4, g.getIdGrupo());

        ps.executeUpdate();
        ps.close();
    }

    // =========================
    // DELETE
    // =========================
    public void eliminar(int idGrupo) throws SQLException {

        String sql = "DELETE FROM GRUPO_PRACTICA WHERE ID_GRUPO = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, idGrupo);

        ps.executeUpdate();
        ps.close();
    }
}
