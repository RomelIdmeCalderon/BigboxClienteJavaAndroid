package com.romelidme.androidbigbox.mvp.Interactor;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import wholetec.cliente.bigbox.adaptadores.Adapter_Graficos_Proveedores;
import wholetec.cliente.bigbox.entidades.Proveedor_Entidad;
import wholetec.cliente.bigbox.interfaces.IRGraficos_Proveedores;
import wholetec.cliente.bigbox.mvp.Interface.IGraficos;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.vistas.Actividades.Graficos;

public class MGraficos implements IGraficos.Interactor, IRGraficos_Proveedores {
    private Context context;
    private IGraficos.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText etNombre;
    private Adapter_Graficos_Proveedores adapter;
    public MGraficos(Context context, IGraficos.Presenter presenter){

        this.context = context;
        this.presenter = presenter;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }


    @Override
    public void listarProveedores(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Proveedor").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ArrayList<Proveedor_Entidad> lista = new ArrayList<>();
                        for(DataSnapshot area: snapshot.getChildren()){
                            Proveedor_Entidad entidad = area.getValue(Proveedor_Entidad.class);
                            entidad.setId(area.getKey());
                            if(entidad.getIdTienda().equals(Common.entidadNegocio.getId())){
                                lista.add(entidad);
                            }
                            if (lista != null && lista.size() > 0) {
                                setAdapterProveedores(rv, lista);
                                rv.setVisibility(View.VISIBLE);
                            } else
                                rv.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setAdapterProveedores(RecyclerView rv, ArrayList<Proveedor_Entidad> lista) {
        adapter= new Adapter_Graficos_Proveedores(context, lista, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void revisar(Proveedor_Entidad entidad) {
        Intent i = new Intent(context, Graficos.class);
        i.putExtra("nombre", entidad.getNombre());
        i.putExtra("proveedor", entidad.getId());
        context.startActivity(i);
    }

}
