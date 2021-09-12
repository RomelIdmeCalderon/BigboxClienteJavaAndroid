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
import wholetec.cliente.bigbox.entidades.Proveedor_Entidad;
import wholetec.cliente.bigbox.interfaces.IRGraficos_Proveedores;

public class Adapter_Graficos_Proveedores extends RecyclerView.Adapter<Adapter_Graficos_Proveedores.ViewHolder> {
    private Context context;
    private ArrayList<Proveedor_Entidad> item;
    private IRGraficos_Proveedores listener;

    public Adapter_Graficos_Proveedores(Context context, ArrayList<Proveedor_Entidad> item, IRGraficos_Proveedores listener){
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_graficos,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proveedor_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombre());

        holder.container.setOnClickListener(v ->{
            listener.revisar(entidad);
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
            container = itemView.findViewById(R.id.container_graficos);
            nombre = itemView.findViewById(R.id.tv_itemgraficos_nombre);

        }
    }
}
