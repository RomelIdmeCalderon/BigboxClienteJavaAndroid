package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MProductos;
import wholetec.cliente.bigbox.mvp.Interface.IProductos;

public class PProductos implements IProductos.Presenter {

    public IProductos.View view;
    public IProductos.Iteractor iteractor;

    public PProductos(IProductos.View view, Context context) {
        this.view = view;
        this.iteractor = new MProductos(context,  this);
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }

    @Override
    public void uploadFoto() {
        if(view!=null)
            view.uploadFoto();
    }

    @Override
    public void crearProducto() {
        if(view!=null)
            iteractor.crearProducto();
    }

    @Override
    public void listarProductos(RecyclerView rv) {
            if(view!=null)
                iteractor.listarProductos(rv);
    }

    @Override
    public void uploadFoto(Uri uri) {
        if(view!=null)
            iteractor.uploadFoto(uri);
    }
}
