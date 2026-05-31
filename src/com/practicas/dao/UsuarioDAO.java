package com.practicas.dao;

import com.practicas.model.RolUsuario;
import com.practicas.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertar(Usuario u) throws SQLException {
        String sql = """
            INSERT INTO USUARIO
            (NOMBRE, APELLIDO, EMAIL, PASSWORD_HASH, ROL, ACTIVO, TELEFONO, FECHA_CREACION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getApellido());
        ps.setString(3, u.getEmail());
        ps.setString(4, u.getPasswordHash());
        ps.setString(5, u.getRol() != null ? u.getRol().name() : null);
        ps.setInt(6, u.isActivo() ? 1 : 0);
        ps.setString(7, u.getTelefono());
        ps.setTimestamp(8, u.getFechaCreacion() != null ? Timestamp.valueOf(u.getFechaCreacion()) : null);
        ps.executeUpdate();
        ps.close();
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USUARIO");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("ID_USUARIO"));
            u.setNombre(rs.getString("NOMBRE"));
            u.setApellido(rs.getString("APELLIDO"));
            u.setEmail(rs.getString("EMAIL"));
            u.setPasswordHash(rs.getString("PASSWORD_HASH"));
            u.setRol(RolUsuario.valueOf(rs.getString("ROL").toUpperCase()));
            u.setActivo(rs.getInt("ACTIVO") == 1);
            u.setTelefono(rs.getString("TELEFONO"));
            Timestamp ts = rs.getTimestamp("FECHA_CREACION");
            if (ts != null) u.setFechaCreacion(ts.toLocalDateTime());
            lista.add(u);
        }
        rs.close();
        ps.close();
        return lista;
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = """
            UPDATE USUARIO
            SET NOMBRE = ?, APELLIDO = ?, EMAIL = ?, PASSWORD_HASH = ?,
                ROL = ?, ACTIVO = ?, TELEFONO = ?, FECHA_CREACION = ?
            WHERE ID_USUARIO = ?
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getNombre());
        ps.setString(2, u.getApellido());
        ps.setString(3, u.getEmail());
        ps.setString(4, u.getPasswordHash());
        ps.setString(5, u.getRol() != null ? u.getRol().name() : null);
        ps.setInt(6, u.isActivo() ? 1 : 0);
        ps.setString(7, u.getTelefono());
        ps.setTimestamp(8, u.getFechaCreacion() != null ? Timestamp.valueOf(u.getFechaCreacion()) : null);
        ps.setInt(9, u.getIdUsuario());
        ps.executeUpdate();
        ps.close();
    }

    public void cambiarPassword(int idUsuario, String nuevaPassword) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE USUARIO SET PASSWORD_HASH = ? WHERE ID_USUARIO = ?"
        );
        ps.setString(1, nuevaPassword);
        ps.setInt(2, idUsuario);
        ps.executeUpdate();
        ps.close();
    }

    public void eliminar(int idUsuario) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM USUARIO WHERE ID_USUARIO = ?");
        ps.setInt(1, idUsuario);
        ps.executeUpdate();
        ps.close();
    }

    public Usuario login(String email, String passwordHash) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE EMAIL = ? AND PASSWORD_HASH = ? AND ACTIVO = 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, email);
        ps.setString(2, passwordHash);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("ID_USUARIO"));
            u.setNombre(rs.getString("NOMBRE"));
            u.setApellido(rs.getString("APELLIDO"));
            u.setEmail(rs.getString("EMAIL"));
            u.setRol(RolUsuario.valueOf(rs.getString("ROL").toUpperCase()));
            rs.close();
            ps.close();
            return u;
        }
        rs.close();
        ps.close();
        return null;
    }
}
