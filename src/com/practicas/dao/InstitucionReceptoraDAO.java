package com.practicas.dao;

import com.practicas.model.InstitucionReceptora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstitucionReceptoraDAO {

    private Connection conn;

    public InstitucionReceptoraDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertar(InstitucionReceptora i) throws SQLException {
        String sql = """
            INSERT INTO INSTITUCION_RECEPTORA
            (NOMBRE, NIT, DIRECCION, MUNICIPIO, DEPARTAMENTO, TELEFONO,
             EMAIL_CONTACTO, CONVENIO_ACTIVO, FECHA_CONVENIO)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, i.getNombre());
        ps.setString(2, i.getNit());
        ps.setString(3, i.getDireccion());
        ps.setString(4, i.getMunicipio());
        ps.setString(5, i.getDepartamento());
        ps.setString(6, i.getTelefono());
        ps.setString(7, i.getEmailContacto());
        ps.setInt(8, i.isConvenioActivo() ? 1 : 0);
        ps.setDate(9, i.getFechaConvenio() != null ? java.sql.Date.valueOf(i.getFechaConvenio()) : null);
        ps.executeUpdate();
        ps.close();
    }

    public List<InstitucionReceptora> listar() throws SQLException {
        List<InstitucionReceptora> lista = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM INSTITUCION_RECEPTORA");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            InstitucionReceptora i = new InstitucionReceptora();
            i.setIdInstitucion(rs.getInt("ID_INSTITUCION"));
            i.setNombre(rs.getString("NOMBRE"));
            i.setNit(rs.getString("NIT"));
            i.setDireccion(rs.getString("DIRECCION"));
            i.setMunicipio(rs.getString("MUNICIPIO"));
            i.setDepartamento(rs.getString("DEPARTAMENTO"));
            i.setTelefono(rs.getString("TELEFONO"));
            i.setEmailContacto(rs.getString("EMAIL_CONTACTO"));
            i.setConvenioActivo(rs.getInt("CONVENIO_ACTIVO") == 1);
            Date fecha = rs.getDate("FECHA_CONVENIO");
            if (fecha != null) i.setFechaConvenio(fecha.toLocalDate());
            lista.add(i);
        }
        rs.close();
        ps.close();
        return lista;
    }

    public void actualizar(InstitucionReceptora i) throws SQLException {
        String sql = """
            UPDATE INSTITUCION_RECEPTORA
            SET NOMBRE = ?, NIT = ?, DIRECCION = ?, MUNICIPIO = ?, DEPARTAMENTO = ?,
                TELEFONO = ?, EMAIL_CONTACTO = ?, CONVENIO_ACTIVO = ?, FECHA_CONVENIO = ?
            WHERE ID_INSTITUCION = ?
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, i.getNombre());
        ps.setString(2, i.getNit());
        ps.setString(3, i.getDireccion());
        ps.setString(4, i.getMunicipio());
        ps.setString(5, i.getDepartamento());
        ps.setString(6, i.getTelefono());
        ps.setString(7, i.getEmailContacto());
        ps.setInt(8, i.isConvenioActivo() ? 1 : 0);
        ps.setDate(9, i.getFechaConvenio() != null ? Date.valueOf(i.getFechaConvenio()) : null);
        ps.setInt(10, i.getIdInstitucion());
        ps.executeUpdate();
        ps.close();
    }

    public void eliminar(int idInstitucion) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE FROM INSTITUCION_RECEPTORA WHERE ID_INSTITUCION = ?");
        ps.setInt(1, idInstitucion);
        ps.executeUpdate();
        ps.close();
    }
}
