package com.romelidme.androidbigbox.mvp.Presenter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.mvp.Interactor.MListaproductosCompra;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosCompra;

public class PListaproductosCompra implements IListaproductosCompra.Presenter {
    public IListaproductosCompra.Interactor interactor;
    public IListaproductosCompra.View view;
    public PListaproductosCompra(IListaproductosCompra.View view, Context context) {
        this.view= view;
        interactor = new MListaproductosCompra(context,  this);
    }

    @Override
    public void agregarProducto(String idProveedor) {
        if(view!= null){
            interactor.agregarProducto(idProveedor);
        }
    }

    @Override
    public void listarProductos(RecyclerView rv, String idProveedor, String nombreProveedor, String nombreCompra) {
        if(view!= null){
            interactor.listarProductos(rv, idProveedor,nombreProveedor,nombreCompra);
        }
    }

    @Override
    public void actualizar() {
        if(view!= null){
            view.actualizar();
        }

    }

    @Override
    public void hacerPedido(String idProveedor) {
        if(view!= null){
            interactor.hacerPedido(idProveedor);
        }
    }
}
