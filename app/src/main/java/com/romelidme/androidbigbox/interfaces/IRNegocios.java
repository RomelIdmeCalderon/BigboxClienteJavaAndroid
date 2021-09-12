package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Negocio_Entidad;

public interface IRNegocios {
    void eliminar(String idNegocio);
    void editar(Negocio_Entidad entidad);
    void escoger(Negocio_Entidad entidad);
}
