package com.romelidme.androidbigbox.mvp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface IListaproductosprov {
    interface View{
        void actualizar();
    }
    interface Presenter{
        void agregarProducto(String idProveedor);
        void listarProductos(RecyclerView rv, String idProveedor,String nombreProveedor);
        void actualizar();
    }
    interface Interactor{
        void agregarProducto(String idProveedor);
        void listarProductos(RecyclerView rv,String idProveedor,String nombreProveedor);

    }

}
