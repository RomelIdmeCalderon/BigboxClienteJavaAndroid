package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Proveedor_Entidad;

public interface IRProveedores {
    void eliminar(String idProveedores);
    void editar(Proveedor_Entidad entidad);
    void revisar(Proveedor_Entidad entidad);
}
