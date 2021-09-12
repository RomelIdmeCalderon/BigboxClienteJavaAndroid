package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Producto_Carrito_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductosCompras;

public class Adapter_Productos_Carrito extends RecyclerView.Adapter<Adapter_Productos_Carrito.ViewHolder>{
    private Context context;
    private ArrayList<Producto_Carrito_Entidad> item;
    private IRProductosCompras listener;

    public Adapter_Productos_Carrito(Context context, ArrayList<Producto_Carrito_Entidad> item, IRProductosCompras listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listaproductoscompra,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto_Carrito_Entidad entidad= item.get(position);
        holder.nombre.setText(entidad.getNombre());
        holder.total.setText("Precio Total S/."+ entidad.getTotal());
        holder.precio.setText("P.U. S/."+entidad.getPrecio());
        holder.cantidad.setText("Cantidad: "+ entidad.getCantidad());

        holder.btnEliminar.setOnClickListener(v->{

        });
        holder.btnEditar.setOnClickListener(v->{
                listener.editar(entidad);
        });

    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,precio,cantidad,total;
        private ImageButton btnEditar,btnEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_itemlistaproductoscompra_nombre);
            precio = itemView.findViewById(R.id.tv_itemlistaproductoscompra_preciounit);
            cantidad = itemView.findViewById(R.id.tv_itemlistaproductoscompra_cantidad);
            total = itemView.findViewById(R.id.tv_itemlistaproductoscompra_total);
            btnEditar = itemView.findViewById(R.id.btn_itemlistaproductoscompra_editar);
            btnEliminar = itemView.findViewById(R.id.btn_itemlistaproductoscompra_eliminar);

        }

    }
}