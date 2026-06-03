package com.practicas.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportesDAO {

    private Connection conn;

    public ReportesDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Object[]> reporteAvancePorEstudiante() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.NOMBRE, u.APELLIDO, g.NOMBRE AS GRUPO, "
                + "ig.HORAS_CUMPLIDAS, p.HORAS_REGLAMENTARIAS, "
                + "CASE WHEN p.HORAS_REGLAMENTARIAS = 0 THEN 0 "
                + "     ELSE LEAST(ROUND((ig.HORAS_CUMPLIDAS / p.HORAS_REGLAMENTARIAS) * 100, 2), 100) "
                + "END AS PORCENTAJE "
                + "FROM INSCRIPCION_GRUPO ig "
                + "JOIN USUARIO u ON ig.ID_ESTUDIANTE = u.ID_USUARIO "
                + "JOIN GRUPO_PRACTICA g ON ig.ID_GRUPO = g.ID_GRUPO "
                + "JOIN PRACTICA p ON g.ID_PRACTICA = p.ID_PRACTICA "
                + "ORDER BY PORCENTAJE DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("NOMBRE") + " " + rs.getString("APELLIDO"),
                    rs.getString("GRUPO"),
                    rs.getDouble("HORAS_CUMPLIDAS"),
                    rs.getDouble("HORAS_REGLAMENTARIAS"),
                    rs.getDouble("PORCENTAJE")
                });
            }
        }
        return lista;
    }

    public List<Object[]> reporteOcupacionGrupos() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT g.NOMBRE AS GRUPO, g.CUPO_MAXIMO, "
                + "COUNT(CASE WHEN ig.ESTADO NOT IN ('RETIRADO', 'REPROBADO') THEN 1 END) AS INSCRITOS_ACTIVOS, "
                + "(g.CUPO_MAXIMO - COUNT(CASE WHEN ig.ESTADO NOT IN ('RETIRADO', 'REPROBADO') THEN 1 END)) AS CUPOS_DISPONIBLES "
                + "FROM GRUPO_PRACTICA g "
                + "LEFT JOIN INSCRIPCION_GRUPO ig ON g.ID_GRUPO = ig.ID_GRUPO "
                + "GROUP BY g.ID_GRUPO, g.NOMBRE, g.CUPO_MAXIMO "
                + "ORDER BY g.NOMBRE";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("GRUPO"),
                    rs.getInt("CUPO_MAXIMO"),
                    rs.getInt("INSCRITOS_ACTIVOS"),
                    rs.getInt("CUPOS_DISPONIBLES")
                });
            }
        }
        return lista;
    }

    public List<Object[]> reporteEstudiantesPorPractica() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT p.NOMBRE AS PRACTICA, g.NOMBRE AS GRUPO, "
                + "u.NOMBRE AS ESTUDIANTE, u.APELLIDO, ig.ESTADO, ig.HORAS_CUMPLIDAS "
                + "FROM INSCRIPCION_GRUPO ig "
                + "JOIN USUARIO u ON ig.ID_ESTUDIANTE = u.ID_USUARIO "
                + "JOIN GRUPO_PRACTICA g ON ig.ID_GRUPO = g.ID_GRUPO "
                + "JOIN PRACTICA p ON g.ID_PRACTICA = p.ID_PRACTICA "
                + "ORDER BY p.NOMBRE, g.NOMBRE, u.APELLIDO";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("PRACTICA"),
                    rs.getString("GRUPO"),
                    rs.getString("ESTUDIANTE"),
                    rs.getString("APELLIDO"),
                    rs.getString("ESTADO"),
                    rs.getDouble("HORAS_CUMPLIDAS")
                });
            }
        }
        return lista;
    }

    public List<Object[]> reporteAuditoriaPassword() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT u.NOMBRE, u.APELLIDO, u.EMAIL, a.FECHA_CAMBIO "
                + "FROM AUDITORIA_PASSWORD a "
                + "JOIN USUARIO u ON a.ID_USUARIO = u.ID_USUARIO "
                + "ORDER BY a.FECHA_CAMBIO DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("NOMBRE"),
                    rs.getString("APELLIDO"),
                    rs.getString("EMAIL"),
                    rs.getTimestamp("FECHA_CAMBIO")
                });
            }
        }
        return lista;
    }
}
