package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IMedidas {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void actualizar();

        void crearMedida();
        void listarMedidas(RecyclerView rv);
    }
    interface Interactor{
        void listarMedidas(RecyclerView rv);
        void crearMedida();
    }
}
