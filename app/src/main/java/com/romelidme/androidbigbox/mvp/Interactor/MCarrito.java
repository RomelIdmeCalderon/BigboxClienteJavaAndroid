package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import wholetec.cliente.bigbox.adaptadores.Adapter_Carrito;
import wholetec.cliente.bigbox.entidades.Carrito_Entidad;
import wholetec.cliente.bigbox.interfaces.IRCarrito;
import wholetec.cliente.bigbox.mvp.Interface.ICarrito;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.vistas.Actividades.ListaproductosCompra;


public class MCarrito implements ICarrito.Iteractor, IRCarrito {
    private Context context;
    private ICarrito.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Carrito adapter= null;

    private EditText etNombre;
    private Button btnCerrarNombre,btnCrearNombre;

    public MCarrito(Context context, ICarrito.Presenter presenter){
        this.context = context;
        this.presenter = presenter;
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }

    @Override
    public void listarCompras(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Carrito").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Carrito_Entidad> lista = new ArrayList<>();
                    if(snapshot.exists()){
                        for(DataSnapshot area : snapshot.getChildren()){
                            Carrito_Entidad entidad = area.getValue(Carrito_Entidad.class);
                            entidad.setId(area.getKey());
                            if(entidad.getId().contains(user.getUid()) && entidad.getId().contains(Common.entidadNegocio.getId())){
                                lista.add(entidad);
                            }
                        }
                    }
                    if (lista != null && lista.size()>0){
                        setAdapterCarrito(rv,lista);
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

    private void setAdapterCarrito(RecyclerView rv, ArrayList<Carrito_Entidad> lista) {
        adapter= new Adapter_Carrito(context ,lista, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminar(String idCarrito) {
        if(Common.isConnect()){
            Common.ref.child("Carrito").child(idCarrito).removeValue().addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    presenter.actualizar();
                }
            });
        }
    }

    @Override
    public void editar(Carrito_Entidad compra) {
        if(Common.isConnect()){
            Common.ref.child("Carrito").child(compra.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Dialog alert;
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final View dialoglayout = inflater.inflate(R.layout.dialog_editarcompra, null);
                        castVieweditarNombre(dialoglayout);
                        etNombre.setText(compra.getNombre());
                        builder.setCancelable(true);
                        builder.setView(dialoglayout);
                        alert = builder.show();
                        btnCerrarNombre.setOnClickListener(view -> alert.dismiss());
                        btnCrearNombre.setOnClickListener(view -> {
                            if (validar()) {
                                if(Common.isConnect()){
                                    Common.ref.child("Carrito").child(compra.getId()).child("nombre").setValue(etNombre.getText().toString());
                                    presenter.actualizar();
                                    alert.dismiss();
                                }

                            }

                        });
                    }else {
                        Toast.makeText(context,"El producto no existe, error revisar",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private boolean validar() {
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            etNombre.setError("Ingrese el nombre");
            return false;
        }
        return true;
    }

    private void castVieweditarNombre(View view) {
        btnCerrarNombre = view.findViewById(R.id.btn_deditarcompra_cerrar);
        btnCrearNombre = view.findViewById(R.id.btn_deditarcompra_crear);
        etNombre = view.findViewById(R.id.et_deditarcompra_nombre);

    }

    @Override
    public void revisar(Carrito_Entidad entidad) {
        Intent i = new Intent(context, ListaproductosCompra.class);
        i.putExtra("nombre",entidad.getNombre());
        i.putExtra("nombreProveedor", entidad.getNombreProveedor());
        i.putExtra("proveedor", entidad.getId());
        context.startActivity(i);
    }
}
