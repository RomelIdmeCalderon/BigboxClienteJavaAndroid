package com.romelidme.androidbigbox.mvp.Interface;

import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

public interface IAdministrarNegocio {

    interface View{
        void uploadFoto();
        void actualizar();
    }

    interface Presenter{
        void actualizar();
        void uploadFoto();

        void verificar();
        void crearNegocio(boolean flag);
        void listarNegocios(RecyclerView rv);
        void uploadFoto(Uri uri);

        void escogerNegocio();

    }

    interface Interactor{ // modelo
        void crearNegocio(boolean flag);
        void listarNegocios(RecyclerView rv);
        void uploadFoto(Uri uri);
        void escogerNegocio();
        void verificar();
    }
}
