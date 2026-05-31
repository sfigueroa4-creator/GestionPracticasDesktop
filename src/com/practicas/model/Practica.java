package com.practicas.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Practica {

    private int idPractica;
    private String nombre;
    private String descripcion;
    private int numeroPractica;
    private int horasReglamentarias;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoPractica estado;
    private String tipoPractica;
    private LocalDateTime fechaCreacion;

    public enum EstadoPractica {
        PROGRAMADA, ACTIVA, FINALIZADA
    }

    public Practica() {}

    public boolean estaActiva() { return EstadoPractica.ACTIVA.equals(this.estado); }

    public int getIdPractica() { return idPractica; }
    public void setIdPractica(int idPractica) { this.idPractica = idPractica; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getNumeroPractica() { return numeroPractica; }
    public void setNumeroPractica(int numeroPractica) { this.numeroPractica = numeroPractica; }

    public int getHorasReglamentarias() { return horasReglamentarias; }
    public void setHorasReglamentarias(int horasReglamentarias) { this.horasReglamentarias = horasReglamentarias; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public EstadoPractica getEstado() { return estado; }
    public void setEstado(EstadoPractica estado) { this.estado = estado; }

    public String getTipoPractica() { return tipoPractica; }
    public void setTipoPractica(String tipoPractica) { this.tipoPractica = tipoPractica; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() { return nombre; }
}
