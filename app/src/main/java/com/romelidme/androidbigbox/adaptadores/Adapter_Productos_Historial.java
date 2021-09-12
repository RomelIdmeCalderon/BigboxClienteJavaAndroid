package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Producto_Historial_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductosHistorial;

public class Adapter_Productos_Historial extends RecyclerView.Adapter<Adapter_Productos_Historial.ViewHolder>  {
    private Context context;
    private ArrayList<Producto_Historial_Entidad> item;
    private IRProductosHistorial listener;


    public Adapter_Productos_Historial(Context context, ArrayList<Producto_Historial_Entidad> item, IRProductosHistorial listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dproductos_historial,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Producto_Historial_Entidad entidad= item.get(position);

        holder.nombre.setText(entidad.getNombre());
        holder.cantidad.setText("Cantidad: " + String.valueOf(entidad.getCantidad()));
        holder.precioUnidad.setText("Precio Unidad: " + String.valueOf(entidad.getPrecio()));
        holder.total.setText("Total: " + String.valueOf(entidad.getTotal()));

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,precioUnidad,total,cantidad;
        private LinearLayout container;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_dproductoshistorial_nombre);
            cantidad = itemView.findViewById(R.id.tv_dproductoshistorial_cantidad);
            precioUnidad = itemView.findViewById(R.id.tv_dproductoshistorial_preciounit);
            total = itemView.findViewById(R.id.tv_dproductoshistorial_total);
            container = itemView.findViewById(R.id.container_dproductoshistorial);
        }

    }
}
