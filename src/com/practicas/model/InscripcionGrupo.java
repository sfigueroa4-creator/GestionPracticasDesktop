/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.practicas.model;

import java.time.LocalDateTime;


public class InscripcionGrupo {

    private int idInscripcion;
    private Usuario estudiante;
    private GrupoPractica grupo;
    private double horasCumplidas;
    private EstadoInscripcion estado;
    private LocalDateTime fechaInscripcion;
    private String observacionFinal;

    public enum EstadoInscripcion {
        ACTIVO,
        COMPLETADO,
        RETIRADO,
        REPROBADO
    }

    public InscripcionGrupo() {
    }

    public double getPorcentajeCumplimiento() {
        int horasReq = grupo.getPractica().getHorasReglamentarias();

        if (horasReq == 0) {
            return 0.0;
        }

        return Math.min(
                (horasCumplidas / horasReq) * 100.0,
                100.0
        );
    }


    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public GrupoPractica getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoPractica grupo) {
        this.grupo = grupo;
    }

    public double getHorasCumplidas() {
        return horasCumplidas;
    }

    public void setHorasCumplidas(double horasCumplidas) {
        this.horasCumplidas = horasCumplidas;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getObservacionFinal() {
        return observacionFinal;
    }

    public void setObservacionFinal(String observacionFinal) {
        this.observacionFinal = observacionFinal;
    }
}