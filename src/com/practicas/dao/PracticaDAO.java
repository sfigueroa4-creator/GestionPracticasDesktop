/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practicas.dao;

import com.practicas.model.Practica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PracticaDAO {

    private Connection conn;

    public PracticaDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================
    // CREATE
    // =========================
    public void insertar(Practica p) throws SQLException {

        String sql = """
            INSERT INTO PRACTICA
            (ID_PRACTICA, NOMBRE, DESCRIPCION, NUMERO_PRACTICA,
             HORAS_REGLAMENTARIAS, FECHA_INICIO, FECHA_FIN,
             ESTADO, TIPO_PRACTICA, FECHA_CREACION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, p.getIdPractica());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getDescripcion());
        ps.setInt(4, p.getNumeroPractica());
        ps.setInt(5, p.getHorasReglamentarias());

        ps.setDate(6, p.getFechaInicio() != null
                ? Date.valueOf(p.getFechaInicio())
                : null);

        ps.setDate(7, p.getFechaFin() != null
                ? Date.valueOf(p.getFechaFin())
                : null);

        ps.setString(8, p.getEstado().name());
        ps.setString(9, p.getTipoPractica());

        ps.setTimestamp(10, p.getFechaCreacion() != null
                ? Timestamp.valueOf(p.getFechaCreacion())
                : null);

        ps.executeUpdate();
        ps.close();
    }

    // =========================
    // READ ALL
    // =========================
    public List<Practica> listar() throws SQLException {

        List<Practica> lista = new ArrayList<>();

        String sql = "SELECT * FROM PRACTICA";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Practica p = new Practica();

            p.setIdPractica(rs.getInt("ID_PRACTICA"));
            p.setNombre(rs.getString("NOMBRE"));
            p.setDescripcion(rs.getString("DESCRIPCION"));
            p.setNumeroPractica(rs.getInt("NUMERO_PRACTICA"));
            p.setHorasReglamentarias(rs.getInt("HORAS_REGLAMENTARIAS"));

            Date fi = rs.getDate("FECHA_INICIO");
            if (fi != null) p.setFechaInicio(fi.toLocalDate());

            Date ff = rs.getDate("FECHA_FIN");
            if (ff != null) p.setFechaFin(ff.toLocalDate());

            p.setEstado(Practica.EstadoPractica.valueOf(rs.getString("ESTADO")));
            p.setTipoPractica(rs.getString("TIPO_PRACTICA"));

            Timestamp fc = rs.getTimestamp("FECHA_CREACION");
            if (fc != null) p.setFechaCreacion(fc.toLocalDateTime());

            lista.add(p);
        }

        rs.close();
        ps.close();

        return lista;
    }

    // =========================
    // UPDATE
    // =========================
    public void actualizar(Practica p) throws SQLException {

        String sql = """
            UPDATE PRACTICA
            SET NOMBRE = ?,
                DESCRIPCION = ?,
                NUMERO_PRACTICA = ?,
                HORAS_REGLAMENTARIAS = ?,
                FECHA_INICIO = ?,
                FECHA_FIN = ?,
                ESTADO = ?,
                TIPO_PRACTICA = ?
            WHERE ID_PRACTICA = ?
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setInt(3, p.getNumeroPractica());
        ps.setInt(4, p.getHorasReglamentarias());

        ps.setDate(5, p.getFechaInicio() != null
                ? Date.valueOf(p.getFechaInicio())
                : null);

        ps.setDate(6, p.getFechaFin() != null
                ? Date.valueOf(p.getFechaFin())
                : null);

        ps.setString(7, p.getEstado().name());
        ps.setString(8, p.getTipoPractica());
        ps.setInt(9, p.getIdPractica());

        ps.executeUpdate();
        ps.close();
    }

    // =========================
    // DELETE
    // =========================
    public void eliminar(int idPractica) throws SQLException {

        String sql = "DELETE FROM PRACTICA WHERE ID_PRACTICA = ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, idPractica);

        ps.executeUpdate();
        ps.close();
    }
}