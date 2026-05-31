/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.dao;

import com.practicas.model.InscripcionGrupo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionGrupoDAO {

    private Connection conn;

    public InscripcionGrupoDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // CREATE (INSERT)
    // =========================
public void insertar(InscripcionGrupo i) throws SQLException {

    String sql = """
        INSERT INTO INSCRIPCION_GRUPO
        (ID_ESTUDIANTE, ID_GRUPO,
         HORAS_CUMPLIDAS, ESTADO,
         FECHA_INSCRIPCION,
         OBSERVACION_FINAL)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setInt(1,
        i.getEstudiante().getIdUsuario()
    );

    ps.setInt(2,
        i.getGrupo().getIdGrupo()
    );

    ps.setDouble(3,
        i.getHorasCumplidas()
    );

    ps.setString(4,
        i.getEstado().name()
    );

    ps.setTimestamp(5,
        i.getFechaInscripcion() != null
            ? Timestamp.valueOf(
                i.getFechaInscripcion()
            )
            : null
    );

    ps.setString(6,
        i.getObservacionFinal()
    );

    ps.executeUpdate();
    ps.close();
}

    // =========================
    // READ (LISTAR TODO)
    // =========================
    public List<InscripcionGrupo> listar() throws SQLException {

        List<InscripcionGrupo> lista = new ArrayList<>();

        String sql = "SELECT * FROM INSCRIPCION_GRUPO";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            InscripcionGrupo i = new InscripcionGrupo();

            i.setIdInscripcion(rs.getInt("ID_INSCRIPCION"));
            i.setHorasCumplidas(rs.getDouble("HORAS_CUMPLIDAS"));
            i.setEstado(InscripcionGrupo.EstadoInscripcion.valueOf(rs.getString("ESTADO")));
            i.setFechaInscripcion(rs.getTimestamp("FECHA_INSCRIPCION").toLocalDateTime());
            i.setObservacionFinal(rs.getString("OBSERVACION_FINAL"));

            // OJO: aquí solo estás trayendo IDs, no objetos completos
            lista.add(i);
        }

        rs.close();
        ps.close();

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    public void actualizar(InscripcionGrupo i) throws SQLException {

        String sql = """
            UPDATE INSCRIPCION_GRUPO
            SET HORAS_CUMPLIDAS = ?,
                ESTADO = ?,
                OBSERVACION_FINAL = ?
            WHERE ID_INSCRIPCION = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setDouble(1, i.getHorasCumplidas());
        ps.setString(2, i.getEstado().name());
        ps.setString(3, i.getObservacionFinal());
        ps.setInt(4, i.getIdInscripcion());

        ps.executeUpdate();
        ps.close();
    }

    // =========================
    // DELETE
    // =========================
    public void eliminar(int idInscripcion) throws SQLException {

        String sql = "DELETE FROM INSCRIPCION_GRUPO WHERE ID_INSCRIPCION = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, idInscripcion);

        ps.executeUpdate();
        ps.close();
    }
}
