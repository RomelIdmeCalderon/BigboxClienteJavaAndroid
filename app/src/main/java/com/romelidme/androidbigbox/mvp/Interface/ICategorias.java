package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface ICategorias {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void actualizar();

        void crearCategoria();
        void listarCategorias(RecyclerView rv);
    }
    interface Iteractor{
        void listarCategorias(RecyclerView rv);
        void crearCategoria();
    }
}
