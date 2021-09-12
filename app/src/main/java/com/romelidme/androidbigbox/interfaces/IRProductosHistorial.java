package com.romelidme.androidbigbox.interfaces;

public interface IRProductosHistorial {
    void editar(String idProducto);
    void eliminar(String idProducto);
    void agregarCarrito(String idProducto,int cantidad);
}
