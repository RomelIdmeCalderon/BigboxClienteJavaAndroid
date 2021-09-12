package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import wholetec.cliente.bigbox.mvp.Interactor.MLogin;
import wholetec.cliente.bigbox.mvp.Interface.ILogin;

public class PLogin implements ILogin.Presenter {
    public ILogin.View view;
    public ILogin.Interactor interactor;

    public PLogin(ILogin.View view, Context context){
        this.view= view;
        interactor =new MLogin(context);

    }


    @Override
    public void iniciarFacebook() {
        if(view!=null )
            interactor.iniciarFacebook();
    }

    @Override
    public void registrarGoogle(String idToken) {
        if(view!=null )
            interactor.registrarGoogle(idToken);
    }

    @Override
    public void verificarSesion() {
        if(view!=null)
            interactor.verificarSesion();
    }


}
