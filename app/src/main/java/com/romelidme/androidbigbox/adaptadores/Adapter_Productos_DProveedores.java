package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Producto_Entidad;
import wholetec.cliente.bigbox.interfaces.IRDProductosProveedores;

public class Adapter_Productos_DProveedores extends RecyclerView.Adapter<Adapter_Productos_DProveedores.ViewHolder> {
    private Context context;
    private ArrayList<Producto_Entidad> item;
    private IRDProductosProveedores listener;
    public Adapter_Productos_DProveedores(Context context, ArrayList<Producto_Entidad> item, IRDProductosProveedores listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listaproductosprov,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto_Entidad entidad= item.get(position);
        holder.nombre.setText(entidad.getNombre());

        holder.container.setOnClickListener(v ->{
            listener.ponerprecio(item.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private LinearLayout container;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_listaproductosprov);
            nombre = itemView.findViewById(R.id.tv_itemlistaproductosprov_nombre);
        }

    }
}
