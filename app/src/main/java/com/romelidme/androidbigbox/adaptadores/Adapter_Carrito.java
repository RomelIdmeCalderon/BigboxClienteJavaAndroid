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
import wholetec.cliente.bigbox.entidades.Carrito_Entidad;
import wholetec.cliente.bigbox.interfaces.IRCarrito;


public class Adapter_Carrito extends RecyclerView.Adapter<Adapter_Carrito.ViewHolder> {
    private Context context;
    private ArrayList<Carrito_Entidad> item;
    private IRCarrito listener;
    public Adapter_Carrito(Context context, ArrayList<Carrito_Entidad> item, IRCarrito listener){
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carrito_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombre());
        holder.nombreProveedor.setText(entidad.getNombreProveedor());

        holder.btnEditar.setOnClickListener(v -> {
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v ->{
            listener.eliminar(entidad.getId());
        });
        holder.container.setOnClickListener(v ->{
            listener.revisar(entidad);
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,nombreProveedor;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_carrito);
            btnEditar=itemView.findViewById(R.id.btn_itemcarrito_editar);
            btnEliminar=itemView.findViewById(R.id.btn_itemcarrito_eliminar);
            container = itemView.findViewById(R.id.container_carrito);
            nombre = itemView.findViewById(R.id.tv_itemcarrito_nombre);
            nombreProveedor = itemView.findViewById(R.id.tv_itemcarrito_proveedor);

        }
    }


}
