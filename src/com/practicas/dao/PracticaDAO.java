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

    public void insertar(Practica p) throws SQLException {
        
        if (p == null) {

         throw new SQLException(
                "La práctica es null"
            );
        }

        String sql = """
            INSERT INTO PRACTICA
            (NOMBRE, DESCRIPCION, NUMERO_PRACTICA,
             HORAS_REGLAMENTARIAS, FECHA_INICIO,
             FECHA_FIN, ESTADO, TIPO_PRACTICA,
             FECHA_CREACION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setInt(3, p.getNumeroPractica());
        ps.setInt(4, p.getHorasReglamentarias());

        ps.setDate(5, p.getFechaInicio() != null
                ? java.sql.Date.valueOf(p.getFechaInicio())
                : null);

        ps.setDate(6, p.getFechaFin() != null
                ? java.sql.Date.valueOf(p.getFechaFin())
                : null);

        ps.setString(7,p.getEstado() != null ? p.getEstado().name(): null);
        ps.setString(8, p.getTipoPractica());

        ps.setTimestamp(9, p.getFechaCreacion() != null
                ? Timestamp.valueOf(p.getFechaCreacion())
                : null);

        ps.executeUpdate();
        ps.close();
    }

    public List<Practica> listar() throws SQLException {

        List<Practica> lista = new ArrayList<>();

        String sql = """ 
                     SELECT
                         ID_PRACTICA,
                         NOMBRE,
                         DESCRIPCION,
                         NUMERO_PRACTICA,
                         HORAS_REGLAMENTARIAS,
                         FECHA_INICIO,
                         FECHA_FIN,
                         ESTADO,
                         TIPO_PRACTICA,
                         FECHA_CREACION
                     FROM PRACTICA
                     """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Practica p = new Practica();

            p.setIdPractica(rs.getInt("ID_PRACTICA"));
            p.setNombre(rs.getString("NOMBRE"));
            p.setDescripcion(rs.getString("DESCRIPCION"));
            p.setNumeroPractica(rs.getInt("NUMERO_PRACTICA"));
            p.setHorasReglamentarias(rs.getInt("HORAS_REGLAMENTARIAS"));

            java.sql.Date fi = rs.getDate("FECHA_INICIO");
            if (fi != null) p.setFechaInicio(fi.toLocalDate());

            java.sql.Date ff = rs.getDate("FECHA_FIN");
            if (ff != null) p.setFechaFin(ff.toLocalDate());

            String estado = rs.getString("ESTADO");

            if (estado != null) {

            p.setEstado(
            Practica.EstadoPractica.valueOf(
                estado
            )
        );
}
            p.setTipoPractica(rs.getString("TIPO_PRACTICA"));

            Timestamp fc = rs.getTimestamp("FECHA_CREACION");
            if (fc != null) p.setFechaCreacion(fc.toLocalDateTime());

            lista.add(p);
        }

        rs.close();
        ps.close();

        return lista;
    }

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

        try (PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setInt(3, p.getNumeroPractica());
        ps.setInt(4, p.getHorasReglamentarias());

        ps.setDate(5, p.getFechaInicio() != null
                ? java.sql.Date.valueOf(p.getFechaInicio())
                : null);

        ps.setDate(6, p.getFechaFin() != null
                ? java.sql.Date.valueOf(p.getFechaFin())
                : null);

        ps.setString(7,p.getEstado() != null ? p.getEstado().name(): null);
        ps.setString(8, p.getTipoPractica());
        ps.setInt(9, p.getIdPractica());

        ps.executeUpdate();
        }
    }

    public void eliminar(int idPractica) throws SQLException {

        String sql = "DELETE FROM PRACTICA WHERE ID_PRACTICA = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setInt(1, idPractica);

        ps.executeUpdate();
        }
    }
}