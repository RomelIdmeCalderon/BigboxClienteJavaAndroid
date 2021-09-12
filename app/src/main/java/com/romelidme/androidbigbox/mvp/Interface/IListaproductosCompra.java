package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IListaproductosCompra {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void agregarProducto(String idProveedor);
        void listarProductos(RecyclerView rv, String idProveedor, String nombreProveedor, String nombreCompra);
        void actualizar();
        void hacerPedido(String idProveedor);
    }
    interface Interactor{
        void agregarProducto(String idProveedor);
        void listarProductos(RecyclerView rv,String idProveedor,String nombreProveedor, String nombreCompra);
        void hacerPedido(String idProveedor);
    }

}
