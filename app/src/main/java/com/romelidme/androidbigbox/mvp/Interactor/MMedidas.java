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

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Medidas;
import wholetec.cliente.bigbox.entidades.Medida_Entidad;
import wholetec.cliente.bigbox.interfaces.IRMedidas;
import wholetec.cliente.bigbox.mvp.Interface.IMedidas;
import wholetec.cliente.bigbox.utils.Common;

public class MMedidas implements IMedidas.Interactor, IRMedidas {
    private Context context;
    private IMedidas.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Medidas adapter= null;

    private EditText etNombre,etAbreviatura;
    private Button btnCerrar,btnCrear;

    public MMedidas(Context context, IMedidas.Presenter presenter){
        this.context = context;
        this.presenter= presenter;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    @Override
    public void listarMedidas(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Medidas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        ArrayList<Medida_Entidad> lista = new ArrayList<>();
                        for(DataSnapshot area : snapshot.getChildren()){
                            Medida_Entidad entidad = area.getValue(Medida_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdUsuario().equals(user.getUid())) {
                                lista.add(entidad);
                            }
                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterMedidas(rv, lista);
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

    private void setAdapterMedidas(RecyclerView rv, ArrayList<Medida_Entidad> lista) {
      adapter= new Adapter_Medidas(context ,lista,this);
      rv.setAdapter(adapter);
      adapter.notifyDataSetChanged();
    }

    @Override
    public void crearMedida() {
        Dialog alert;
        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_medidas,null);
        castViewMedida(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        btnCrear.setOnClickListener(v->{
            if(validar()){
                btnCrear.setEnabled(false);
                Medida_Entidad entidad = new Medida_Entidad();
                entidad.setNombreMedida(etNombre.getText().toString().trim());
                entidad.setAbreviatura(etAbreviatura.getText().toString().trim());
                entidad.setIdUsuario(user.getUid());
                Common.ref.child("Medidas").push().setValue(entidad).addOnCompleteListener(task -> {
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

    private boolean validar() {
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            etNombre.setError(context.getString(R.string.ingresenombre));
            return false;
        }
        if(TextUtils.isEmpty(etAbreviatura.getText().toString().trim())){
            etAbreviatura.setError(context.getString(R.string.ingreseabreviatura));
            return false;
        }
        return true;
    }

    private void castViewMedida(View view) {
        etNombre = view.findViewById(R.id.et_dmedidas_nombre);
        etAbreviatura = view.findViewById(R.id.et_dmedidas_abreviatura);
        btnCerrar = view.findViewById(R.id.btn_dmedidas_cerrar);
        btnCrear = view.findViewById(R.id.btn_dmedidas_aceptar);
        btnCrear.setEnabled(true);
    }

    @Override
    public void eliminar(String idMedida) {
        if(Common.isConnect()){
            Common.ref.child("Medidas").child(idMedida).removeValue().addOnCompleteListener(task ->{
               if (task.isSuccessful()){
                   Toast.makeText(context, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                   presenter.actualizar();
               }
            });
        }

    }

    @Override
    public void editar(Medida_Entidad entidad) {
        Dialog alert;
        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_medidas,null);
        castViewMedida(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        btnCrear.setOnClickListener(v->{
            if(validar()){
                btnCrear.setEnabled(false);
                HashMap<String,Object> map = new HashMap<>();
                map.put("nombreMedida",etNombre.getText().toString().trim());
                map.put("abreviatura",etAbreviatura.getText().toString().trim());

                Common.ref.child("Medidas").child(entidad.getId()).updateChildren(map).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(context, context.getString(R.string.medidaeditadook), Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                        presenter.actualizar();
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());

    }
    private void castViewMedida(View view,Medida_Entidad entidad) {
        etNombre = view.findViewById(R.id.et_dmedidas_nombre);
        etAbreviatura = view.findViewById(R.id.et_dmedidas_abreviatura);
        btnCerrar = view.findViewById(R.id.btn_dmedidas_cerrar);
        btnCrear = view.findViewById(R.id.btn_dmedidas_aceptar);
        btnCrear.setEnabled(true);
        etNombre.setText(entidad.getNombreMedida());
        etAbreviatura.setText(entidad.getAbreviatura());
    }
}
