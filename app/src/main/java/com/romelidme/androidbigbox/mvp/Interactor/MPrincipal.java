package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.entidades.Negocio_Entidad;
import wholetec.cliente.bigbox.mvp.Interface.IPrincipal;
import wholetec.cliente.bigbox.utils.Common;

public class MPrincipal implements IPrincipal.Interactor {
    private IPrincipal.Presenter presenter;
    private Context context;
    private Spinner spEscogerNegocios;
    private Button btnEscogerNegocios,btnEscogerNegociosCerrar;
    private ArrayList<Negocio_Entidad> listaNegocio;
    private ArrayList<String> listaNegocioST;

    private FirebaseAuth auth;
    private FirebaseUser user;

    public MPrincipal(Context context,IPrincipal.Presenter presenter) {
        this.presenter = presenter;
        this.context= context;
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
    }

    @Override
    public void escogerNegocio() {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_escogernegocio, null);
        castViewEscogerNegocio(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        btnEscogerNegociosCerrar.setOnClickListener(view -> alert.dismiss());
        btnEscogerNegocios.setOnClickListener(v->{

        });
    }

    private void castViewEscogerNegocio(View view) {
        btnEscogerNegociosCerrar = view.findViewById(R.id.btn_descogernegocio_cerrar);
        spEscogerNegocios = view.findViewById(R.id.sp_descogernegocio_negocio);
        btnEscogerNegocios = view.findViewById(R.id.btn_descogernegocio_aceptar);
        btnEscogerNegocios.setEnabled(true);
        Negociospinner(spEscogerNegocios);
    }
    private void Negociospinner(Spinner spEscogerNegocios) {
        if(Common.isConnect()){
            Common.ref.child("Negocio").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listaNegocio = new ArrayList<>();
                        listaNegocioST = new ArrayList<>();
                        Negocio_Entidad entidad = new Negocio_Entidad();
                        entidad.setId("default");
                        entidad.setNombre("Puedes editar tu Negocio");
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
}
