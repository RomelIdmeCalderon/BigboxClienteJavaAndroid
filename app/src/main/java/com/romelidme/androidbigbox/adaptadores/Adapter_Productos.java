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
import wholetec.cliente.bigbox.entidades.Producto_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductos;

public class Adapter_Productos extends RecyclerView.Adapter<Adapter_Productos.ViewHolder> {
    private Context context;
    private ArrayList<Producto_Entidad> item;
    private IRProductos listener;

    public Adapter_Productos(Context context, ArrayList<Producto_Entidad> item, IRProductos listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto_Entidad entidad= item.get(position);
        holder.nombre.setText(entidad.getNombre());
        holder.categoria.setText(entidad.getNombreCategoria());
        holder.medida.setText(entidad.getNombreMedida());

        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.logov).into(holder.imagen);

        holder.btnEditar.setOnClickListener(v -> {
            listener.editar(entidad);
        });
        holder.btnEliminar.setOnClickListener(v -> {
            listener.eliminar(entidad.getId());
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre;
        private TextView categoria;
        private TextView medida;
        private CircleImageView imagen;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_productos);
            nombre = itemView.findViewById(R.id.tv_itemproductos_nombre);
            categoria = itemView.findViewById(R.id.tv_itemproductos_categoria);
            medida = itemView.findViewById(R.id.tv_itemproductos_nombremedida);
            imagen = itemView.findViewById(R.id.iv_itemproductos);
            btnEditar = itemView.findViewById(R.id.btn_itemproductos_editar);
            btnEliminar = itemView.findViewById(R.id.btn_itemproductos_eliminar);
        }

    }
}
