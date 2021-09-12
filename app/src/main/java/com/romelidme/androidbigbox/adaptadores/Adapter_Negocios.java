package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Negocio_Entidad;
import wholetec.cliente.bigbox.interfaces.IRNegocios;
import wholetec.cliente.bigbox.utils.Common;


public class Adapter_Negocios extends RecyclerView.Adapter<Adapter_Negocios.ViewHolder> {
    private Context context;
    private ArrayList<Negocio_Entidad> item;
    private IRNegocios listener;
    public Adapter_Negocios(Context context, ArrayList<Negocio_Entidad> item, IRNegocios listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_negocios,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Negocio_Entidad entidad = item.get(position);
        holder.nombre.setText(entidad.getNombre());
        if(Common.entidadNegocio!= null){
            if(entidad.getId().equals(Common.entidadNegocio.getId())){
                holder.condicion.setText("Abierto");
                holder.condicion.setTextColor(context.getResources().getColor(R.color.verde));
            }
        }
        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.logov).into(holder.imagen);
        holder.btnEditar.setOnClickListener(v -> {
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v -> {
            listener.eliminar(entidad.getId());
        });
        holder.container.setOnClickListener(v ->{
            listener.escoger(entidad);
        });


        //todo verificar un producto de un proveedor
        /*if(entidad.getIdUsuario().contains(idproveeodr)){
            lista.add(entidad);
        }*/

    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,condicion;
        private CircleImageView imagen;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);

            btnEditar=itemView.findViewById(R.id.btn_itemnegocio_editar);
            btnEliminar=itemView.findViewById(R.id.btn_itemnegocio_eliminar);
            container = itemView.findViewById(R.id.container_negocios);
            nombre = itemView.findViewById(R.id.tv_itemnegocio_nombre);
            condicion = itemView.findViewById(R.id.tv_itemnegocio_condicion);
            imagen = itemView.findViewById(R.id.iv_itemnegocio);
        }
    }
}
