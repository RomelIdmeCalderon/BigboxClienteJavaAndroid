package com.romelidme.androidbigbox.mvp.Interface;

import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

public interface IProveedores {
    interface View{
        void uploadFoto();
        void actualizar();
    }
    interface Presenter{
        void crearProveedor();
        void listarProveedores(RecyclerView rv);

        void uploadFoto();
        void actualizar();
        void uploadFoto(Uri uri);
    }
    interface Iteractor{
        void crearProveedor();
        void listarProveedores(RecyclerView rv);
        void uploadFoto(Uri uri);
    }
}
