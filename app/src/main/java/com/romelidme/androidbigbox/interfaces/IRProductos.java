package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Producto_Entidad;

public interface IRProductos {
    void eliminar(String idProducto);
    void editar(Producto_Entidad entidad);


}
