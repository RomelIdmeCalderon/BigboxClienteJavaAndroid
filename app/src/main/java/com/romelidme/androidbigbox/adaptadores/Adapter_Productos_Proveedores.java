package com.romelidme.androidbigbox.adaptadores;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Producto_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductosProveedores;

public class Adapter_Productos_Proveedores extends RecyclerView.Adapter<Adapter_Productos_Proveedores.ViewHolder> {
    private Context context;
    private ArrayList<Producto_Entidad> item;
    private IRProductosProveedores listener;

    public Adapter_Productos_Proveedores(Context context, ArrayList<Producto_Entidad> item, IRProductosProveedores listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dlistaproductosprov,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto_Entidad entidad= item.get(position);
        holder.nombre.setText(entidad.getNombre());
        holder.precio.setText("S/."+entidad.getPrecio());
        holder.btnEliminar.setOnClickListener(v->{
            listener.eliminar(entidad.getId());
        });
        holder.btnEditar.setOnClickListener(v->{
            listener.editar(entidad.getId());
        });
        holder.btnCarrito.setOnClickListener(v->{
            if(TextUtils.isEmpty(holder.cantidad.getText().toString().trim())){
                Toast.makeText(context,"debe agregar una cantidad",Toast.LENGTH_SHORT).show();
            }
            else{ listener.agregarCarrito(entidad.getId(),Integer.parseInt(holder.cantidad.getText().toString().trim())); }
        });

    }


    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nombre,precio,cantidad;
        private LinearLayout container;
        private ImageButton btnEditar,btnEliminar;
        private Button btnCarrito;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_dlistaproductosprov);
            nombre = itemView.findViewById(R.id.tv_ditemlistaproductosprov_nombre);
            precio = itemView.findViewById(R.id.tv_ditemlistaproductosprov_precio);
            btnEditar = itemView.findViewById(R.id.btn_ditemlistaproductosprov_editar);
            btnEliminar = itemView.findViewById(R.id.btn_ditemlistaproductosprov_eliminar);
            btnCarrito = itemView.findViewById(R.id.btn_ditemlistaproductosprov_carrito);
            cantidad = itemView.findViewById(R.id.et_ditemlistaproductosprov_cantidad);

        }

    }
}
