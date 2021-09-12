package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import wholetec.cliente.bigbox.adaptadores.Adapter_Historial;
import wholetec.cliente.bigbox.adaptadores.Adapter_Productos_Historial;
import wholetec.cliente.bigbox.entidades.Historial_Entidad;
import wholetec.cliente.bigbox.entidades.Producto_Historial_Entidad;
import wholetec.cliente.bigbox.interfaces.IRHistorial;
import wholetec.cliente.bigbox.interfaces.IRProductosHistorial;
import wholetec.cliente.bigbox.mvp.Interface.IHistorial;
import wholetec.cliente.bigbox.utils.Common;

public class MHistorial implements IHistorial.Interactor , IRHistorial, IRProductosHistorial {
    private Context context;
    private IHistorial.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Historial adapter = null;
    private Adapter_Productos_Historial adapter2 = null;
    private Button btnCerrar,btnPedido;
    private RecyclerView rv;
    private Dialog alertaRevisar;
    public MHistorial(Context context, IHistorial.Presenter presenter){
        this.context = context;
        this.presenter = presenter;
        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    @Override
    public void listarHistorial(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Historial").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Historial_Entidad> lista = new ArrayList<>();
                    if(snapshot.exists()) {
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Historial_Entidad entidad = area.getValue(Historial_Entidad.class);
                            entidad.setId(area.getKey());
                           if (entidad.getId().contains(user.getUid()) && entidad.getId().contains(Common.entidadNegocio.getId())) {
                                lista.add(entidad);
                            }
                        }
                    }
                        if(lista != null && lista.size() > 0){
                            setAdapterHistorial(rv,lista);
                            rv.setVisibility(View.VISIBLE);
                        }else {
                            rv.setVisibility(View.GONE);
                        }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setAdapterHistorial(RecyclerView rv, ArrayList<Historial_Entidad> lista) {
        adapter = new Adapter_Historial(context, lista, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void editar(String idProducto) {

    }

    @Override
    public void eliminar(String idEntidad) {
        if(Common.isConnect()){
            Common.ref.child("Historial").child(idEntidad).removeValue().addOnCompleteListener(task ->{
                if (task.isSuccessful()){
                    Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    presenter.actualizar();
                }
            });
        }
    }

    @Override
    public void agregarCarrito(String idProducto, int cantidad) {

    }

    @Override
    public void revisar(Historial_Entidad entidad) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_productos_historial, null);
        castViewProducto(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alertaRevisar = builder.show();
        btnCerrar.setOnClickListener(view -> alertaRevisar.dismiss());
    }

    private void castViewProducto(View view, Historial_Entidad entidad) {
        btnCerrar = view.findViewById(R.id.btn_dproductohistorial_cerrar);
        rv = view.findViewById(R.id.rv_dproductoshistorial);
        btnPedido = view.findViewById(R.id.btn_dproductohistorial_pedido);
        listarProductos(rv, entidad);
    }

    private void listarProductos(RecyclerView rv, Historial_Entidad entidad) {
        if(Common.isConnect()){
            Common.ref.child("Historial").child(entidad.getId()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ArrayList<Producto_Historial_Entidad> lista = new ArrayList<>();
                        for(DataSnapshot area: snapshot.getChildren()){
                            Producto_Historial_Entidad entidad = area.getValue(Producto_Historial_Entidad.class);
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

    private void setAdapterProductos(RecyclerView rv, ArrayList<Producto_Historial_Entidad> lista) {
        adapter2 = new Adapter_Productos_Historial(context, lista, this);
        rv.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }
}
