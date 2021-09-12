package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Historial_Entidad;

public interface IRHistorial {
    void eliminar(String idEntidad);
    void revisar(Historial_Entidad idEntidad);
}
