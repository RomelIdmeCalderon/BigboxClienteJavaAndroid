package com.romelidme.androidbigbox.mvp.Interface;

import android.widget.ImageView;

public interface ISplashscreen {
    interface View{}
    interface Presenter{
        void animacion(ImageView iv);
    }
    interface Interactor{
        void animacion(ImageView iv);
    }
}
