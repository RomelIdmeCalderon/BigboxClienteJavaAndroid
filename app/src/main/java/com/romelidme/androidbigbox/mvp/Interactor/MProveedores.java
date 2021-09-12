package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Proveedores;
import wholetec.cliente.bigbox.entidades.Proveedor_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProveedores;
import wholetec.cliente.bigbox.mvp.Interface.IProveedores;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.vistas.Actividades.Listaproductosprov;

public class MProveedores  implements IProveedores.Iteractor, IRProveedores {
    private Context context;
    private IProveedores.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Proveedores adapter=null;
    private StorageReference ref_storage=null;
    private Uri uri=null;

    private EditText etNombre,etCorreo,etTelefono;
    private Button btnAceptar,btnCerrar;
    private ImageView ivFoto;

    public MProveedores(Context context, IProveedores.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }

    @Override
    public void crearProveedor() {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_proveedores, null);
        castViewProveedor(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
            if(validar()){
                btnAceptar.setEnabled(false);
                Proveedor_Entidad entidad = new Proveedor_Entidad();
                entidad.setIdUsuario(user.getUid());
                entidad.setImagen("default");
                entidad.setNombre(etNombre.getText().toString().trim());
                entidad.setCorreo(etCorreo.getText().toString().trim());
                entidad.setTelefono(etTelefono.getText().toString().trim());
                entidad.setIdTienda(Common.entidadNegocio.getId());
                entidad.setDireccion("");
                String idProveedor = Common.ref.child("Proveedor").push().getKey();
                Common.ref.child("Proveedor").child(idProveedor).setValue(entidad).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(uri!=null){
                            alert.dismiss();
                            agregarImagen(idProveedor,0);
                        }
                        else{
                            Toast.makeText(context, context.getString(R.string.proveedorcreadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            presenter.actualizar();
                        }
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());
    }

    private void agregarImagen(String idProveedor,int mensaje) {
        if (auth.getCurrentUser() == null)
            return;
        if (ref_storage == null)
            ref_storage = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = ref_storage.child("Proveedor").child(idProveedor);
        Common.loading(context);
        filepath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String urlImagen = String.valueOf(task.getResult());
                    Map<String,Object> map = new HashMap<>();
                    map.put("imagen",urlImagen);
                    Common.ref.child("Proveedor").child(idProveedor).updateChildren(map).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            this.uri=null;
                            if(mensaje==0)
                                Toast.makeText(context, context.getString(R.string.proveedorcreadook), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, context.getString(R.string.proveedoreditadook), Toast.LENGTH_SHORT).show();
                            Common.close();
                            presenter.actualizar();
                        }
                    });
                }
                else
                    Common.close();

            });
        }).addOnFailureListener(e -> {
            Common.close();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
    private boolean validar(){
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            etNombre.setError(context.getString(R.string.ingresenombre));
            return false;
        }
        if (TextUtils.isEmpty(etCorreo.getText().toString().trim())){
            etCorreo.setError(context.getString(R.string.ingresecorreo));
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText().toString()).matches()){
            etCorreo.setError("Ingrese un correo correcto");
            return false;
        }
        if (TextUtils.isEmpty(etTelefono.getText().toString().trim())){
            etTelefono.setError(context.getString(R.string.ingresetelefono));
            return false;
        }
        if(etTelefono.getText().toString().length()>9){
            etTelefono.setError(context.getString(R.string.ingresetelefonocorrecto));
            return false;
        }
        return true;
    }

    private void castViewProveedor(View view) {
        btnCerrar= view.findViewById(R.id.btn_dproveedores_cerrar);
        etNombre = view.findViewById(R.id.et_dproveedores_nombre);
        etCorreo = view.findViewById(R.id.et_dproveedores_correo);
        etTelefono = view.findViewById(R.id.et_dproveedores_telefono);
        btnAceptar = view.findViewById(R.id.btn_dproveedores_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dproveedor);
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
                            Proveedor_Entidad entidad= area.getValue(Proveedor_Entidad.class);
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

    @Override
    public void uploadFoto(Uri uri) {
        this.uri = uri;
        Picasso.with(context).load(uri).fit().error(R.drawable.proveedor).into(ivFoto);
    }

    private void setAdapterProveedores(RecyclerView rv, ArrayList<Proveedor_Entidad> lista) {
        adapter= new Adapter_Proveedores(context ,lista,this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminar(String idProveedores) {
        if(Common.isConnect()){
            Common.ref.child("Proveedor").child(idProveedores).removeValue().addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(context,"Eliminado correctamente", Toast.LENGTH_SHORT).show();
                   presenter.actualizar();
               }
            });
        }

    }

    @Override
    public void editar(Proveedor_Entidad entidad) {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_proveedores, null);
        castViewProveedor(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
            if(validar()){
                btnAceptar.setEnabled(false);
                Map<String,Object> map = new HashMap<>();
                map.put("nombre",etNombre.getText().toString().trim());
                map.put("correo",etCorreo.getText().toString().trim());
                map.put("telefono",etTelefono.getText().toString().trim());
                map.put("direccion","");

                Common.ref.child("Proveedor").child(entidad.getId()).updateChildren(map).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(uri!=null){
                            Toast.makeText(context, context.getString(R.string.negocioeditadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            agregarImagen(entidad.getId(),1);
                            presenter.actualizar();
                        }
                        else{
                            Toast.makeText(context, context.getString(R.string.negocioeditadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            presenter.actualizar();
                        }
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());
    }

    @Override
    public void revisar(Proveedor_Entidad entidad) {
        Intent i = new Intent(context, Listaproductosprov.class);
        i.putExtra("nombre", entidad.getNombre());
        i.putExtra("proveedor", entidad.getId());
        context.startActivity(i);
    }

    private void castViewProveedor(View view,Proveedor_Entidad entidad) {
        btnCerrar= view.findViewById(R.id.btn_dproveedores_cerrar);
        etNombre = view.findViewById(R.id.et_dproveedores_nombre);
        etCorreo = view.findViewById(R.id.et_dproveedores_correo);
        etTelefono = view.findViewById(R.id.et_dproveedores_telefono);
        btnAceptar = view.findViewById(R.id.btn_dproveedores_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dproveedor);
        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.logov).into(ivFoto);
        btnAceptar.setText("Editar");
        etNombre.setText(entidad.getNombre());
        etCorreo.setText(entidad.getCorreo());
        etTelefono.setText(entidad.getTelefono());
    }

}
