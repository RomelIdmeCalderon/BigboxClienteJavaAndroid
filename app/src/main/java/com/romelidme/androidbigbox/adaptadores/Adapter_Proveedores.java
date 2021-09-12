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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Proveedor_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProveedores;

public class Adapter_Proveedores extends RecyclerView.Adapter<Adapter_Proveedores.ViewHolder> {
    private Context context;
    private ArrayList<Proveedor_Entidad> item;
    private IRProveedores listener;

    public Adapter_Proveedores(Context context, ArrayList<Proveedor_Entidad> item, IRProveedores listener){
        this.context = context;
        this.item = item;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proveedores,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Proveedor_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombre());
        holder.telefono.setText(entidad.getTelefono());
        holder.correo.setText(entidad.getCorreo());

        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.proveedor).into(holder.imagen);

        holder.btnEditar.setOnClickListener(v -> {
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v -> {
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
        private TextView nombre;
        private TextView telefono;
        private TextView correo;
        private CircleImageView imagen;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container_proveedores);
            btnEditar=itemView.findViewById(R.id.btn_itemproveedores_editar);
            btnEliminar=itemView.findViewById(R.id.btn_itemproveedores_eliminar);
            nombre = itemView.findViewById(R.id.tv_itemproveedores_nombre);
            telefono = itemView.findViewById(R.id.tv_itemproveedores_telefono);
            correo = itemView.findViewById(R.id.tv_itemproveedores_correo);
            imagen = itemView.findViewById(R.id.iv_itemproveedor);

        }
    }
}
