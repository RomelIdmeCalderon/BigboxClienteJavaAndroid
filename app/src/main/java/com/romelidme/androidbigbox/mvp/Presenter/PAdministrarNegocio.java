package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MAdministrarNegocio;
import wholetec.cliente.bigbox.mvp.Interface.IAdministrarNegocio;

public class PAdministrarNegocio implements IAdministrarNegocio.Presenter {
    public IAdministrarNegocio.View view;
    public IAdministrarNegocio.Interactor interactor;

    public PAdministrarNegocio(IAdministrarNegocio.View view, Context context) {
        this.view = view;
        interactor = new MAdministrarNegocio(context,this);
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
    public void verificar() {
        if(view!=null)
            interactor.verificar();
    }

    @Override
    public void crearNegocio(boolean flag) {
        if(view!=null)
            interactor.crearNegocio(flag);
    }

    @Override
    public void listarNegocios(RecyclerView rv) {
        if(view!=null)
            interactor.listarNegocios(rv);
    }

    @Override
    public void uploadFoto(Uri uri) {
        if(view!=null)
            interactor.uploadFoto(uri);
    }

    @Override
    public void escogerNegocio() {
        if(view!=null)
            interactor.escogerNegocio();
    }



}
