package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;
import android.widget.ImageView;

import wholetec.cliente.bigbox.mvp.Interactor.MSplashscreen;
import wholetec.cliente.bigbox.mvp.Interface.ISplashscreen;

public class PSplashscreen implements ISplashscreen.Presenter {
    private ISplashscreen.View view;
    private ISplashscreen.Interactor interactor;

    public PSplashscreen(ISplashscreen.View view, Context context) {
        this.view = view;
        interactor = new MSplashscreen(context);
    }

    @Override
    public void animacion(ImageView iv) {
        if(view!=null)
            interactor.animacion(iv);
    }
}
