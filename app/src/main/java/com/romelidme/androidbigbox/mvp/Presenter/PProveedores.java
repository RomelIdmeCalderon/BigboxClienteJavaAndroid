package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MProveedores;
import wholetec.cliente.bigbox.mvp.Interface.IProveedores;

public class PProveedores implements IProveedores.Presenter{
    public IProveedores.View view;
    public IProveedores.Iteractor iteractor;

    public PProveedores(IProveedores.View view, Context context) {
        this.view = view;
        iteractor = new MProveedores(context,this);
    }

    @Override
    public void crearProveedor() {
        if(view!=null)
            iteractor.crearProveedor();
    }

    @Override
    public void listarProveedores(RecyclerView rv) {
        if(view!=null)
            iteractor.listarProveedores(rv);
    }

    @Override
    public void uploadFoto() {
        if(view!=null)
            view.uploadFoto();
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }

    @Override
    public void uploadFoto(Uri uri) {
        if(view!=null)
            iteractor.uploadFoto(uri);
    }
}
