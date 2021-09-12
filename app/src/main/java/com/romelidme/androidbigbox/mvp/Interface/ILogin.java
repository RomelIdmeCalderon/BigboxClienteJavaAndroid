package com.romelidme.androidbigbox.mvp.Interface;

public interface ILogin {
    interface View{}
    interface Presenter{
        void iniciarFacebook();
        void registrarGoogle(String idToken);
        void verificarSesion();
    }
    interface Interactor{
        void iniciarFacebook();
        void registrarGoogle(String idToken);
        void verificarSesion();
    }
}
