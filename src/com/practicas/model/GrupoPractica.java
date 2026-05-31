package com.practicas.model;

public class GrupoPractica {

    private int idGrupo;
    private String nombre;
    private Practica practica;
    private Usuario docenteAsesor;
    private InstitucionReceptora institucion;
    private int cupoMaximo;
    private String observaciones;

    public GrupoPractica() {}

    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public Usuario getDocenteAsesor() { return docenteAsesor; }
    public void setDocenteAsesor(Usuario docenteAsesor) { this.docenteAsesor = docenteAsesor; }

    public InstitucionReceptora getInstitucion() { return institucion; }
    public void setInstitucion(InstitucionReceptora institucion) { this.institucion = institucion; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() { return nombre; }
}
