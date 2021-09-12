package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Producto_Carrito_Entidad;

public interface IRProductosCompras {
    void editar(Producto_Carrito_Entidad idProducto);
    void eliminar(Producto_Carrito_Entidad idProducto);
}
