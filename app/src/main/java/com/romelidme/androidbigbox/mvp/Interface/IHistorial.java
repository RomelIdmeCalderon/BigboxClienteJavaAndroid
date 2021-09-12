package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IHistorial {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void actualizar();
        void listarHistorial(RecyclerView rv);
    }
    interface Interactor{
        void listarHistorial(RecyclerView rv);
    }
}
