package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MListaproductosprov;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosprov;

public class    PListaproductosprov implements IListaproductosprov.Presenter {

    public IListaproductosprov.Interactor interactor;
    public IListaproductosprov.View view;
    public PListaproductosprov(IListaproductosprov.View view, Context context) {
        this.view= view;
        interactor = new MListaproductosprov(context,  this);
    }
    @Override
    public void agregarProducto(String idProveedor) {
        if(view!= null){
            interactor.agregarProducto(idProveedor);
        }
    }

    @Override
    public void listarProductos(RecyclerView rv, String idProveedor,String nombreProveedor) {
        if(view!= null){
            interactor.listarProductos(rv, idProveedor,nombreProveedor);
        }
    }

    @Override
    public void actualizar() {
        if(view!= null){
            view.actualizar();
        }

    }
}
