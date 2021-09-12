package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Carrito_Entidad;

public interface IRCarrito {
    void eliminar(String idCarrito);
    void editar(Carrito_Entidad entidad);
    void revisar(Carrito_Entidad entidad);
}
