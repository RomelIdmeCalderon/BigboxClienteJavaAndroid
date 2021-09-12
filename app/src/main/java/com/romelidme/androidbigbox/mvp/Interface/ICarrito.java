package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface ICarrito {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void actualizar();
        void listarCompras(RecyclerView rv);
    }
    interface Iteractor{
        void listarCompras(RecyclerView rv);
    }
}
