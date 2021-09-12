package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MCategorias;
import wholetec.cliente.bigbox.mvp.Interface.ICategorias;

public class PCategorias implements ICategorias.Presenter {
    public ICategorias.View view;
    public ICategorias.Iteractor interactor;
    public PCategorias(ICategorias.View view, Context context) {
        this.view= view;
        interactor = new MCategorias(context,this);
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }

    @Override
    public void crearCategoria() {
        if(view!=null)
            interactor.crearCategoria();
    }

    @Override
    public void listarCategorias(RecyclerView rv) {
        if(view!=null)
            interactor.listarCategorias(rv);
    }
}
