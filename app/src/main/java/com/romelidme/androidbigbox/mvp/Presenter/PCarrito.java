package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MCarrito;
import wholetec.cliente.bigbox.mvp.Interface.ICarrito;

public class PCarrito implements ICarrito.Presenter {
    public ICarrito.View view;
    public ICarrito.Iteractor interactor;
    public PCarrito(ICarrito.View view, Context context) {
        this.view= view;
        interactor = new MCarrito(context,this);
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }

    @Override
    public void listarCompras(RecyclerView rv) {
        if(view!=null)
            interactor.listarCompras(rv);
    }
}
