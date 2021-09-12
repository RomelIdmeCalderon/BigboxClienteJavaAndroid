package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Categorias;
import wholetec.cliente.bigbox.entidades.Categoria_Entidad;
import wholetec.cliente.bigbox.interfaces.IRCategorias;
import wholetec.cliente.bigbox.mvp.Interface.ICategorias;
import wholetec.cliente.bigbox.utils.Common;

public class MCategorias implements ICategorias.Iteractor, IRCategorias {
    private Context context;
    private ICategorias.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Categorias adapter= null;

    private EditText etNombre;
    private Button btnCerrar,btnCrear;

    public MCategorias(Context context, ICategorias.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }

    @Override
    public void listarCategorias(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Categorias").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ArrayList<Categoria_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Categoria_Entidad entidad = area.getValue(Categoria_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdTienda().equals(Common.entidadNegocio.getId())) {
                                lista.add(entidad);
                            }
                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterCategorias(rv, lista);
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
    public void crearCategoria() {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_categorias, null);
        castViewCategoria(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        btnCrear.setOnClickListener(view ->{
            if(validar()){
                btnCrear.setEnabled(false);
                Categoria_Entidad entidad = new Categoria_Entidad();
                entidad.setIdTienda(Common.entidadNegocio.getId());
                entidad.setNombre(etNombre.getText().toString().trim());
                Common.ref.child("Categorias").push().setValue(entidad).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(context, context.getString(R.string.categoriacreadook), Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                        presenter.actualizar();
                    }
                });




            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());
    }
    private boolean validar(){
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            etNombre.setError(context.getString(R.string.ingresenombre));
            return false;
        }
        return true;
    }

    private void castViewCategoria(View view) {
        btnCerrar = view.findViewById(R.id.btn_dcategoria_cerrar);
        etNombre = view.findViewById(R.id.et_dcategorias_nombre);
        btnCrear= view.findViewById(R.id.btn_dcategoria_crear);
        btnCrear.setEnabled(true);
    }

    private void setAdapterCategorias(RecyclerView rv, ArrayList<Categoria_Entidad> lista) {
        adapter= new Adapter_Categorias(context ,lista,this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminar(String idCategoria) {
        if(Common.isConnect()){
            Common.ref.child("Categorias").child(idCategoria).removeValue().addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    presenter.actualizar();
                }
            });
        }

    }

    @Override
    public void editar(Categoria_Entidad entidad) {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_categorias, null);
        castViewCategoria(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        btnCrear.setOnClickListener(view ->{
            if(validar()){
                btnCrear.setEnabled(false);
                Map<String,Object> map = new HashMap<>();
                map.put("nombre",etNombre.getText().toString().trim());
                Common.ref.child("Categorias").child(entidad.getId()).updateChildren(map).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(context, "Editado correctamente", Toast.LENGTH_SHORT).show();

                        alert.dismiss();
                        presenter.actualizar();
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());
    }
    private void castViewCategoria(View view,Categoria_Entidad entidad) {
        btnCerrar = view.findViewById(R.id.btn_dcategoria_cerrar);
        etNombre = view.findViewById(R.id.et_dcategorias_nombre);
        btnCrear= view.findViewById(R.id.btn_dcategoria_crear);
        btnCrear.setText("Editar");
        btnCerrar= view.findViewById(R.id.btn_dcategoria_cerrar);
        btnCrear.setEnabled(true);
        btnCrear.setText("Editar");
        etNombre.setText(entidad.getNombre());
    }
}
