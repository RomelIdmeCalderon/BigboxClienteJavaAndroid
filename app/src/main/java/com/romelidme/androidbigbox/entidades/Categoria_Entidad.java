package com.romelidme.androidbigbox.entidades;

import java.io.Serializable;

public class Categoria_Entidad implements Serializable {
    private String id;
    private String idTienda;
    private String nombre;

    public Categoria_Entidad() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
