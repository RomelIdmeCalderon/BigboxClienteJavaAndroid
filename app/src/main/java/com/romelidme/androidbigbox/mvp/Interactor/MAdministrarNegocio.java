package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import wholetec.cliente.bigbox.adaptadores.Adapter_Negocios;
import wholetec.cliente.bigbox.entidades.Negocio_Entidad;
import wholetec.cliente.bigbox.interfaces.IRNegocios;
import wholetec.cliente.bigbox.mvp.Interface.IAdministrarNegocio;
import wholetec.cliente.bigbox.utils.Common;

public class MAdministrarNegocio implements IAdministrarNegocio.Interactor, IRNegocios {
    private Context context;
    private IAdministrarNegocio.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Negocios adapter= null;
    private StorageReference ref_storage=null;
    private Uri uri=null;

    private EditText etNombre;
    private Button btnAceptar,btnCerrar;
    private ImageView ivFoto;

    private Spinner spEscogerNegocios;
    private Button btnEscogerNegocios,btnEscogerNegociosCerrar;
    private ArrayList<Negocio_Entidad > listaNegocio;
    private ArrayList<String> listaNegocioST;
    private int a=0;

    public MAdministrarNegocio(Context context, IAdministrarNegocio.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }

    @Override
    public void crearNegocio(boolean flag){
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_negocio, null);
        castViewNegocio(dialoglayout);
        builder.setCancelable(flag);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
            if(validar()){
                btnAceptar.setEnabled(false);
                Negocio_Entidad entidad = new Negocio_Entidad();
                entidad.setIdUsuario(user.getUid());
                entidad.setImagen("default");
                entidad.setNombre(etNombre.getText().toString().trim());
                String idNegocio = Common.ref.child("Negocio").push().getKey();
                Common.ref.child("Negocio").child(idNegocio).setValue(entidad).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(uri!=null){
                            Toast.makeText(context, context.getString(R.string.negociocreadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            agregarImagen(idNegocio,0);
                            if(Common.entidadNegocio==null){
                                escogerNegocio();
                            }
                        }
                        else{
                            Toast.makeText(context, context.getString(R.string.negociocreadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            presenter.actualizar();
                            if(Common.entidadNegocio==null){
                                escogerNegocio();
                            }
                        }
                    }
                });
            }
        });
        if(flag==false) btnCerrar.setVisibility(View.GONE);
        btnCerrar.setOnClickListener(view -> {
            alert.dismiss();
            });
    }

    private void agregarImagen(String idNegocio,int mensaje) {
        if (auth.getCurrentUser() == null)
            return;
        if (ref_storage == null)
            ref_storage = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = ref_storage.child("Negocio").child(idNegocio);
        Common.loading(context);
        filepath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String urlImagen = String.valueOf(task.getResult());
                    Map<String,Object> map = new HashMap<>();
                    map.put("imagen",urlImagen);
                    Common.ref.child("Negocio").child(idNegocio).updateChildren(map).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            this.uri=null;
                            if(mensaje==0)
                                Toast.makeText(context, context.getString(R.string.negociocreadook), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, context.getString(R.string.negocioeditadook), Toast.LENGTH_SHORT).show();
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
        return true;
    }
    private void castViewNegocio(View view){
        btnCerrar= view.findViewById(R.id.btn_dnegocio_cerrar);
        etNombre = view.findViewById(R.id.et_dnegocio_nombre);
        btnAceptar = view.findViewById(R.id.btn_dnegocio_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dnegocio);
    }
    @Override
    public void listarNegocios(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Negocio").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        ArrayList<Negocio_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Negocio_Entidad entidad = area.getValue(Negocio_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdUsuario().equals(user.getUid())) {
                                lista.add(entidad);
                            }
                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterNegocio(rv, lista);
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
    public void uploadFoto(Uri uri) {
        this.uri = uri;
        Picasso.with(context).load(uri).fit().error(R.drawable.logov).into(ivFoto);
    }

    @Override
    public void escogerNegocio() {
            if(Common.entidadNegocio == null){
                Dialog alert;
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View dialoglayout = inflater.inflate(R.layout.dialog_escogernegocio, null);
                castViewEscogerNegocio(dialoglayout);
                builder.setCancelable(false);
                builder.setView(dialoglayout);
                alert = builder.show();
                btnEscogerNegociosCerrar.setOnClickListener(view -> alert.dismiss());
                btnEscogerNegocios.setOnClickListener(v-> {
                    if (validarescoger()) {
                        if (Common.isConnect()) {
                            Common.loading(context);
                            Common.ref.child("Negocio").child(listaNegocio.get(spEscogerNegocios.getSelectedItemPosition()).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Negocio_Entidad entidad = snapshot.getValue(Negocio_Entidad.class);
                                        entidad.setId(snapshot.getKey());
                                        Common.entidadNegocio = entidad;
                                        Toast.makeText(context, "negocio " + listaNegocioST.get(spEscogerNegocios.getSelectedItemPosition()) + " abierto", Toast.LENGTH_SHORT).show();
                                        Common.close();
                                        alert.dismiss();
                                        presenter.actualizar();
                                    } else
                                        Common.close();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                    });
                }
    }


    private boolean validarescoger() {
        if(spEscogerNegocios.getSelectedItemPosition()==0){
            Toast.makeText(context, "Escoga un negocio correcto", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void castViewEscogerNegocio(View view) {
        btnEscogerNegociosCerrar = view.findViewById(R.id.btn_descogernegocio_cerrar);
        spEscogerNegocios = view.findViewById(R.id.sp_descogernegocio_negocio);
        btnEscogerNegocios = view.findViewById(R.id.btn_descogernegocio_aceptar);
        btnEscogerNegocios.setEnabled(true);
        negocioSpinner(spEscogerNegocios);
        btnEscogerNegociosCerrar.setVisibility(View.GONE);
    }

    private void negocioSpinner(Spinner spEscogerNegocios) {
        if(Common.isConnect()){
            Common.ref.child("Negocio").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listaNegocio = new ArrayList<>();
                        listaNegocioST = new ArrayList<>();
                        Negocio_Entidad entidad = new Negocio_Entidad();
                        entidad.setId("default");
                        entidad.setNombre("Escoge tu Negocio");
                        listaNegocioST.add(entidad.getNombre());
                        listaNegocio.add(entidad);
                        for(DataSnapshot area : dataSnapshot.getChildren()){
                            Negocio_Entidad entidad1 = area.getValue(Negocio_Entidad.class);
                            entidad1.setId(area.getKey());
                            if (entidad1.getIdUsuario().equals(user.getUid())) {
                                listaNegocioST.add(entidad1.getNombre());
                                listaNegocio.add(entidad1);
                            }
                        }
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaNegocioST);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spEscogerNegocios.setAdapter(addressAdapter);
                        if(Common.entidadNegocio!=null){
                            for(int i=0;i<listaNegocio.size();i++){
                                if(listaNegocio.get(i).getId().equals(Common.entidadNegocio.getId())){
                                    spEscogerNegocios.setSelection(i);
                                }
                            }
                        }

                        Common.close();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setAdapterNegocio(RecyclerView rv, ArrayList<Negocio_Entidad> lista) {
        adapter= new Adapter_Negocios(context ,lista,this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminar(String idNegocio) {
        //poner un dialog par aconfirmar la elimininacion
        if(Common.isConnect()){
            if(Common.entidadNegocio.getId().equals(idNegocio)) {
                Common.entidadNegocio=null;
                Common.ref.child("Negocio").child(idNegocio).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Eliminado correctamente1.", Toast.LENGTH_SHORT).show();
                        presenter.actualizar();
                    }
                });
                verificar();
            }else {
                Common.ref.child("Negocio").child(idNegocio).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        presenter.actualizar();
                    }
                });
            }
        }
    }

    @Override
    public void editar(Negocio_Entidad entidad) {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_negocio, null);
        castViewNegocio(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
            if(validar()){
                btnAceptar.setEnabled(false);
                Map<String,Object> map = new HashMap<>();
                map.put("nombre",etNombre.getText().toString().trim());
                Common.ref.child("Negocio").child(entidad.getId()).updateChildren(map).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(uri!=null){
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
            if(Common.entidadNegocio.getId().equals(entidad.getId())){
                if(Common.isConnect()) {
                    Common.loading(context);
                    Common.ref.child("Negocio").child(entidad.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Negocio_Entidad entidad = snapshot.getValue(Negocio_Entidad.class);
                                entidad.setId(snapshot.getKey());
                                Common.entidadNegocio = entidad;
                                Toast.makeText(context,"memoria "+ Common.entidadNegocio.getNombre()+" editado",Toast.LENGTH_SHORT).show();
                                Common.close();
                                alert.dismiss();
                            }
                            else
                                Common.close();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());
    }

    @Override
    public void escoger(Negocio_Entidad Negocio) {
        if(Common.isConnect()){
            Common.entidadNegocio = Negocio;
            Toast.makeText(context,"Negocio "+ Common.entidadNegocio.getNombre()+ " abierto correctamente",Toast.LENGTH_SHORT).show();
            presenter.actualizar();
        }
    }

    private void castViewNegocio(View view,Negocio_Entidad entidad){
        btnCerrar= view.findViewById(R.id.btn_dnegocio_cerrar);
        etNombre = view.findViewById(R.id.et_dnegocio_nombre);
        btnAceptar = view.findViewById(R.id.btn_dnegocio_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dnegocio);
        etNombre.setText(entidad.getNombre());
        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.logov).into(ivFoto);
        btnAceptar.setText("Editar");
    }
    @Override
    public void verificar(){
        Common.ref.child("Negocio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    ArrayList<Negocio_Entidad> lista = new ArrayList<>();
                    for (DataSnapshot area : snapshot.getChildren()) {
                        Negocio_Entidad entidad = area.getValue(Negocio_Entidad.class);
                        entidad.setId(area.getKey());
                        if (entidad.getIdUsuario().equals(user.getUid())) {
                            lista.add(entidad);
                        }
                    }
                    if (lista.size() == 0) {
                        crearNegocio(false);
                    }else {
                        escogerNegocio();
                    }
                }else{
                    crearNegocio(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
