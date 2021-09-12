package com.romelidme.androidbigbox.mvp.Interface;

import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

public interface IProductos {
    interface View{
        void uploadFoto();
        void actualizar();
    }
    interface Presenter{
        void actualizar();
        void uploadFoto();
        void crearProducto();
        void listarProductos(RecyclerView rv);
        void uploadFoto(Uri uri);
    }
    interface Iteractor{
        void crearProducto();
        void listarProductos(RecyclerView rv);
        void uploadFoto(Uri uri);
    }
}
