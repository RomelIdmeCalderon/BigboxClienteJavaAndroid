package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Categoria_Entidad;
import wholetec.cliente.bigbox.interfaces.IRCategorias;


public class Adapter_Categorias extends RecyclerView.Adapter<Adapter_Categorias.ViewHolder> {
    private Context context;
    private ArrayList<Categoria_Entidad> item;
    private IRCategorias listener;
    public Adapter_Categorias(Context context, ArrayList<Categoria_Entidad> item, IRCategorias listener){
        this.context = context;
        this.item = item;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombre());

        holder.btnEditar.setOnClickListener(v -> {
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v ->{
            listener.eliminar(entidad.getId());
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
            public ViewHolder(View itemView) {
            super(itemView);
            btnEditar=itemView.findViewById(R.id.btn_itemcategorias_editar);
            btnEliminar=itemView.findViewById(R.id.btn_itemcategorias_eliminar);
            container = itemView.findViewById(R.id.container_categorias);
            nombre = itemView.findViewById(R.id.tv_itemcategorias_nombre);

        }
    }
}
