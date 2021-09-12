package com.romelidme.androidbigbox.mvp.Interface;

public interface IPrincipal {
    interface View{}
    interface Presenter{
        void escogerNegocio();
    }
    interface Interactor{
        void escogerNegocio();
    }

}
