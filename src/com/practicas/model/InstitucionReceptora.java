/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.model;

import java.time.LocalDate;

public class InstitucionReceptora {

    private int idInstitucion;
    private String nombre;
    private String nit;
    private String direccion;
    private String municipio;
    private String departamento;
    private String telefono;
    private String emailContacto;
    private boolean convenioActivo;
    private LocalDate fechaConvenio;

    public InstitucionReceptora() {
    }


    public int getIdInstitucion() {
        return idInstitucion;
    }

    public void setIdInstitucion(int idInstitucion) {
        this.idInstitucion = idInstitucion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public boolean isConvenioActivo() {
        return convenioActivo;
    }

    public void setConvenioActivo(boolean convenioActivo) {
        this.convenioActivo = convenioActivo;
    }

    public LocalDate getFechaConvenio() {
        return fechaConvenio;
    }

    public void setFechaConvenio(LocalDate fechaConvenio) {
        this.fechaConvenio = fechaConvenio;
    }
    @Override
    public String toString() {
        return nombre;
    }
}