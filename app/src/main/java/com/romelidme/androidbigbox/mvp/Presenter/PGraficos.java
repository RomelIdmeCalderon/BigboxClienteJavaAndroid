package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MGraficos;
import wholetec.cliente.bigbox.mvp.Interface.IGraficos;

public class PGraficos implements IGraficos.Presenter {
    private IGraficos.Interactor interactor;
    private IGraficos.View view;


    public PGraficos (IGraficos.View view, Context context){
        this.view = view;
        interactor = new MGraficos(context,this);
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }


    @Override
    public void listarProveedores(RecyclerView rv) {
        if(view!=null)
            interactor.listarProveedores(rv);
    }
}
