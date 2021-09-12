package com.romelidme.androidbigbox.interfaces;

import wholetec.cliente.bigbox.entidades.Categoria_Entidad;


public interface IRCategorias {
        void eliminar(String idCategoria);
        void editar(Categoria_Entidad entidad);

}
