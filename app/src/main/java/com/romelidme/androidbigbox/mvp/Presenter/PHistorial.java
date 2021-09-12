package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MHistorial;
import wholetec.cliente.bigbox.mvp.Interface.IHistorial;

public class PHistorial implements IHistorial.Presenter {

    private IHistorial.View view;
    private IHistorial.Interactor interactor;

    public PHistorial(IHistorial.View view, Context context){
        this.view = view;
        interactor = new MHistorial(context, this);

    }
    @Override
    public void listarHistorial(RecyclerView rv) {
        if(view!= null){
            interactor.listarHistorial(rv);
        }
    }

    @Override
    public void actualizar() {
        if(view!= null){
            view.actualizar();
        }

    }
}
