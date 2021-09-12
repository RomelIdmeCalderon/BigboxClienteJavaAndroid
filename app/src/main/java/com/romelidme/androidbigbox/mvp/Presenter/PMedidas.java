package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MMedidas;
import wholetec.cliente.bigbox.mvp.Interface.IMedidas;

public class PMedidas implements IMedidas.Presenter {
    IMedidas.View view;
    IMedidas.Interactor interactor;

    public PMedidas(IMedidas.View view , Context context) {
        this.view = view;
        interactor = new MMedidas(context,this);
    }

    @Override
    public void actualizar() {
        if(view!=null)
            view.actualizar();
    }

    @Override
    public void crearMedida() {
        if(view!=null)
            interactor.crearMedida();

    }

    @Override
    public void listarMedidas(RecyclerView rv) {
        if(view!=null)
            interactor.listarMedidas(rv);
    }
}
