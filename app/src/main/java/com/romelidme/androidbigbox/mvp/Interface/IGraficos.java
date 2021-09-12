package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IGraficos {
    interface View{
        void actualizar();
    }

    interface Presenter{
        void actualizar();

        void listarProveedores(RecyclerView rv);
    }

    interface Interactor{
        void listarProveedores(RecyclerView rv);
    }
}
