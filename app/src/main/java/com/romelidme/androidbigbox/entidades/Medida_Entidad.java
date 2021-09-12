package com.romelidme.androidbigbox.entidades;

public class Medida_Entidad {
    private String id;
    private String idUsuario;
    private String nombreMedida;
    private String abreviatura;



    public Medida_Entidad() {

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreMedida() {
        return nombreMedida;
    }

    public void setNombreMedida(String nombre) {
        this.nombreMedida = nombre;
    }

    public String getAbreviatura() { return abreviatura; }

    public void setAbreviatura(String abreviatura) { this.abreviatura = abreviatura; }
}
