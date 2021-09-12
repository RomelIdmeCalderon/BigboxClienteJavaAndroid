package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import wholetec.cliente.bigbox.mvp.Interface.IPrincipal;

public class PPrincipal implements IPrincipal.Presenter {
    public IPrincipal.View view;
    public IPrincipal.Interactor interactor;

    public PPrincipal(IPrincipal.View view, Context context) {
        this.view = view;
    }

    @Override
    public void escogerNegocio() {
        if(view!= null)
            interactor.escogerNegocio();
    }
}
