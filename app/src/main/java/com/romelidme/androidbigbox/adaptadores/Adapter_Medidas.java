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
import wholetec.cliente.bigbox.entidades.Medida_Entidad;
import wholetec.cliente.bigbox.interfaces.IRMedidas;

public class Adapter_Medidas extends RecyclerView.Adapter<Adapter_Medidas.ViewHolder> {
    private Context context;
    private ArrayList<Medida_Entidad> item;
    private IRMedidas listener;
    public Adapter_Medidas(Context context, ArrayList<Medida_Entidad> item, IRMedidas listener){
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medidas,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medida_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombreMedida());
        holder.abreviatura.setText(entidad.getAbreviatura());
        holder.btnEditar.setOnClickListener(v->{
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v->{
            listener.eliminar(entidad.getId());
        });
    }

    @Override
    public int getItemCount() { return item.size(); }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,abreviatura;
        private LinearLayout container;
        private ImageButton btnEliminar,btnEditar;
        public ViewHolder(View itemView) {
            super(itemView);
            btnEditar = itemView.findViewById(R.id.btn_itemmedidas_editar);
            btnEliminar = itemView.findViewById(R.id.btn_itemmedidas_eliminar);
            container = itemView.findViewById(R.id.container_medidas);
            nombre = itemView.findViewById(R.id.tv_itemmedidas_nombre);
            abreviatura = itemView.findViewById(R.id.tv_itemmedidas_abreviatura);
        }
    }
}
