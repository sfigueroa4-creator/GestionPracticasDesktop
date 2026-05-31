package com.practicas.dao;

import com.practicas.model.GrupoPractica;
import com.practicas.model.InscripcionGrupo;
import com.practicas.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionGrupoDAO {

    private Connection conn;

    public InscripcionGrupoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertar(InscripcionGrupo i) throws SQLException {
        if (i == null || i.getEstudiante() == null || i.getGrupo() == null || i.getEstado() == null)
            throw new SQLException("Datos incompletos");
        String sql = """
            INSERT INTO INSCRIPCION_GRUPO
            (ID_ESTUDIANTE, ID_GRUPO, HORAS_CUMPLIDAS, ESTADO, FECHA_INSCRIPCION, OBSERVACION_FINAL)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, i.getEstudiante().getIdUsuario());
        ps.setInt(2, i.getGrupo().getIdGrupo());
        ps.setDouble(3, i.getHorasCumplidas());
        ps.setString(4, i.getEstado().name());
        ps.setTimestamp(5, i.getFechaInscripcion() != null ? Timestamp.valueOf(i.getFechaInscripcion()) : null);
        ps.setString(6, i.getObservacionFinal());
        ps.executeUpdate();
        ps.close();
    }

    public List<InscripcionGrupo> listar() throws SQLException {
        List<InscripcionGrupo> lista = new ArrayList<>();
        String sql = """
            SELECT ig.ID_INSCRIPCION, ig.ID_ESTUDIANTE, ig.ID_GRUPO,
                   ig.HORAS_CUMPLIDAS, ig.ESTADO, ig.FECHA_INSCRIPCION, ig.OBSERVACION_FINAL,
                   u.NOMBRE AS NOMBRE_ESTUDIANTE, g.NOMBRE AS NOMBRE_GRUPO
            FROM INSCRIPCION_GRUPO ig
            JOIN USUARIO u ON ig.ID_ESTUDIANTE = u.ID_USUARIO
            JOIN GRUPO_PRACTICA g ON ig.ID_GRUPO = g.ID_GRUPO
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            InscripcionGrupo i = new InscripcionGrupo();
            i.setIdInscripcion(rs.getInt("ID_INSCRIPCION"));
            i.setHorasCumplidas(rs.getDouble("HORAS_CUMPLIDAS"));
            i.setEstado(InscripcionGrupo.EstadoInscripcion.valueOf(rs.getString("ESTADO")));
            Timestamp ts = rs.getTimestamp("FECHA_INSCRIPCION");
            if (ts != null) i.setFechaInscripcion(ts.toLocalDateTime());
            i.setObservacionFinal(rs.getString("OBSERVACION_FINAL"));
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("ID_ESTUDIANTE"));
            u.setNombre(rs.getString("NOMBRE_ESTUDIANTE"));
            GrupoPractica g = new GrupoPractica();
            g.setIdGrupo(rs.getInt("ID_GRUPO"));
            g.setNombre(rs.getString("NOMBRE_GRUPO"));
            i.setEstudiante(u);
            i.setGrupo(g);
            lista.add(i);
        }
        rs.close();
        ps.close();
        return lista;
    }

    public void actualizar(InscripcionGrupo i) throws SQLException {
        String sql = """
            UPDATE INSCRIPCION_GRUPO
            SET HORAS_CUMPLIDAS = ?, ESTADO = ?, OBSERVACION_FINAL = ?
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

    public void eliminar(int idInscripcion) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM INSCRIPCION_GRUPO WHERE ID_INSCRIPCION = ?");
        ps.setInt(1, idInscripcion);
        ps.executeUpdate();
        ps.close();
    }

    public List<Usuario> obtenerEstudiantes() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIO WHERE ROL = 'ESTUDIANTE'");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("ID_USUARIO"));
            u.setNombre(rs.getString("NOMBRE"));
            lista.add(u);
        }
        rs.close();
        ps.close();
        return lista;
    }

    public List<GrupoPractica> obtenerGrupos() throws SQLException {
        List<GrupoPractica> lista = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM GRUPO_PRACTICA");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            GrupoPractica g = new GrupoPractica();
            g.setIdGrupo(rs.getInt("ID_GRUPO"));
            g.setNombre(rs.getString("NOMBRE"));
            lista.add(g);
        }
        rs.close();
        ps.close();
        return lista;
    }
}
