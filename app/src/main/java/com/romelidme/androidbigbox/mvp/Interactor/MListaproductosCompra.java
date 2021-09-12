package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Productos_Carrito;
import wholetec.cliente.bigbox.entidades.Carrito_Entidad;
import wholetec.cliente.bigbox.entidades.Producto_Carrito_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductosCompras;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosCompra;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.utils.Fecha;

public class MListaproductosCompra implements IListaproductosCompra.Interactor, IRProductosCompras {
    private Context context;
    private IListaproductosCompra.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Productos_Carrito adapter= null;
    private String idCompraGeneral,mensajeWhatsapp = "Nuevo Pedido";
    private Button btnEditar,btnSumar,btnRestar,btnCerrar;
    private TextView tvCantidad,tvProducto,tvPrecio,tvTotal;
    private Button btnFinalizarPedido,btnWFinalizarPedido,btnwhatsapp;

    public MListaproductosCompra(Context context, IListaproductosCompra.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    @Override
    public void agregarProducto(String idProveedor) {

    }

    @Override
    public void listarProductos(RecyclerView rv, String idCompra, String nombreProveedor, String nombreCompra) {
        idCompraGeneral =idCompra;
        if(Common.isConnect()){
            Common.ref.child("Carrito").child(idCompra).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Producto_Carrito_Entidad> lista = new ArrayList<>();
                        for(DataSnapshot area: snapshot.getChildren()){
                            Producto_Carrito_Entidad entidad = area.getValue(Producto_Carrito_Entidad.class);
                            entidad.setId(area.getKey());
                            lista.add(entidad);
                        }
                        if(lista!=null &&    lista.size()>0){
                            setAdapterProductos(rv,lista);
                            rv.setVisibility(View.VISIBLE);
                        } else
                            rv.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void hacerPedido(String idCompra) {
        generarMensaje(idCompra);
        Dialog alert;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_hacerpedido, null);
        castViewHacerPedido(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();

        btnFinalizarPedido.setOnClickListener(v->{
            finalizarPedido(idCompra);
            alert.dismiss();
        });

        btnWFinalizarPedido.setOnClickListener(v->{
            finalizarPedido(idCompra);
            alert.dismiss();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mensajeWhatsapp);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        });

        btnwhatsapp.setOnClickListener(v->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mensajeWhatsapp);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        });

    }

    private void finalizarPedido(String idCompra) {
        Common.ref.child("Carrito").child(idCompra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Carrito_Entidad carrito = snapshot.getValue(Carrito_Entidad.class);
                    carrito.setFecha(Fecha.Actual_Formato());
                    carrito.setHora(Fecha.Actual_Hora());
                    String idComprado = idCompra+"~"+Fecha.Actual_Hora();
                    Common.ref.child("Historial").child(idComprado).setValue(carrito).addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            Common.ref.child("Carrito").child(idCompra).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot area : snapshot.getChildren()){
                                        Producto_Carrito_Entidad entidad = area.getValue(Producto_Carrito_Entidad.class);
                                        entidad.setId(area.getKey());
                                        Common.ref.child("Historial").child(idComprado).child("Productos").child(entidad.getId()).setValue(entidad);
                                    }
                                    Common.ref.child("Carrito").child(idCompra).removeValue();
                                    presenter.actualizar();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void generarMensaje(String idCompra) {
        Common.ref.child("Carrito").child(idCompra).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot area : snapshot.getChildren()){
                    Producto_Carrito_Entidad entidad = area.getValue(Producto_Carrito_Entidad.class);
                    entidad.setId(area.getKey());
                    mensajeWhatsapp += "\n" + entidad.getNombre() +" : " + entidad.getCantidad();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void castViewHacerPedido(View view) {
        btnFinalizarPedido = view.findViewById(R.id.btn_dpedido_finalizar);
        btnWFinalizarPedido = view.findViewById(R.id.btn_dpedido_wfinalizar);
        btnwhatsapp = view.findViewById(R.id.btn_dpedido_whatsapp);
    }

    private void setAdapterProductos(RecyclerView rv, ArrayList<Producto_Carrito_Entidad> lista) {
        adapter = new Adapter_Productos_Carrito(context,lista,  this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editar(Producto_Carrito_Entidad entidad) {
        Common.ref.child("Carrito").child(idCompraGeneral).child("Productos").child(entidad.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Producto_Carrito_Entidad entidad2 = snapshot.getValue(Producto_Carrito_Entidad.class);
                    Dialog alert;
                    LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final View dialoglayout = inflater.inflate(R.layout.dialog_editarproductocompra, null);
                    castVieweditarProducto(dialoglayout,entidad);
                    builder.setCancelable(true);
                    builder.setView(dialoglayout);
                    alert = builder.show();
                    btnCerrar.setOnClickListener(v->{alert.dismiss();});
                    btnSumar.setOnClickListener(v->{
                        int a = Integer.parseInt(tvCantidad.getText().toString());
                        Double b = Double.parseDouble(tvTotal.getText().toString());
                        Double c = Double.parseDouble(tvPrecio.getText().toString());
                        b = b+c;
                        a =a+1;
                        tvCantidad.setText(""+a);
                        tvTotal.setText(b.toString());
                    });
                    btnRestar.setOnClickListener(v->{
                        int a = Integer.parseInt(tvCantidad.getText().toString());
                        if(a>1) {
                            Double b = Double.parseDouble(tvTotal.getText().toString());
                            Double c = Double.parseDouble(tvPrecio.getText().toString());
                            b = b - c;
                            a = a - 1;
                            tvCantidad.setText("" + a);
                            tvTotal.setText(b.toString());
                        }
                    });
                    btnEditar.setOnClickListener(v->{
                       entidad2.setTotal(Double.parseDouble(tvTotal.getText().toString()));
                       entidad2.setCantidad(Integer.parseInt(tvCantidad.getText().toString()));
                       entidad2.setPrecio(Double.parseDouble(tvPrecio.getText().toString()));
                       Common.ref.child("Carrito").child(idCompraGeneral).child("Productos").child(entidad.getId()).setValue(entidad2).addOnCompleteListener(task -> {
                           if(task.isSuccessful()){
                               presenter.actualizar();
                               alert.dismiss();
                           }
                       });
                        });
                }else{
                    Toast.makeText(context,"no existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void castVieweditarProducto(View view, Producto_Carrito_Entidad entidad) {
        tvProducto = view.findViewById(R.id.tv_deditarproductoscompra_producto);
        tvPrecio = view.findViewById(R.id.tv_deditarproductoscompra_precio);
        tvTotal = view.findViewById(R.id.tv_deditarproductoscompra_total);
        btnEditar = view.findViewById(R.id.tv_deditarproductoscompra_crear);
        tvCantidad = view.findViewById(R.id.tv_deditarproductoscompra_cantidad);
        btnRestar = view.findViewById(R.id.btn_deditarproductoscompra_restar);
        btnSumar = view.findViewById(R.id.btn_deditarproductoscompra_sumar);
        btnCerrar = view.findViewById(R.id.btn_deditarproductoscompra_cerrar);
        tvProducto.setText(entidad.getNombre());
        tvPrecio.setText(entidad.getPrecio().toString());
        tvTotal.setText(entidad.getTotal().toString());
        tvCantidad.setText(""+entidad.getCantidad());
    }

    @Override
    public void eliminar(Producto_Carrito_Entidad idProducto) {

    }




}
