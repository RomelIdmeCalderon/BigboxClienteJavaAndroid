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
import wholetec.cliente.bigbox.entidades.Historial_Entidad;
import wholetec.cliente.bigbox.interfaces.IRHistorial;

public class Adapter_Historial extends RecyclerView.Adapter<Adapter_Historial.ViewHolder> {
    private Context context;
    private ArrayList<Historial_Entidad> item;
    private IRHistorial listener;
    public Adapter_Historial(Context context, ArrayList<Historial_Entidad> item, IRHistorial listener){
        this.context = context;
        this.item =item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Historial_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombreProveedor());
        holder.fecha.setText(entidad.getFecha());
        holder.hora.setText(entidad.getHora());
        holder.btnEliminar.setOnClickListener(v->{
            listener.eliminar(entidad.getId());
        });
        holder.container.setOnClickListener(v->{
            listener.revisar(entidad);
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,fecha,hora;
        private LinearLayout container;
        private ImageButton btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            btnEliminar = itemView.findViewById(R.id.btn_itemhistorial_eliminar);
            container = itemView.findViewById(R.id.container_historial);
            nombre = itemView.findViewById(R.id.tv_itemhistorial_nombre);
            fecha = itemView.findViewById(R.id.tv_itemhistorial_fecha);
            hora = itemView.findViewById(R.id.tv_itemhistorial_hora);
        }
    }


}
