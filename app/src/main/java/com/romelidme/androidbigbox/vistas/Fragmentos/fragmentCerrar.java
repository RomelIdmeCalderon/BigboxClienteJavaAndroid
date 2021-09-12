package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.vistas.Actividades.Inicio;


public class fragmentCerrar extends Fragment {
    private FirebaseAuth auth;

    public fragmentCerrar() {
        auth= FirebaseAuth.getInstance();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth.signOut();
        if (Common.entidadNegocio != null) {
            Common.entidadNegocio =null;
        }
        Intent intent = new Intent(getActivity(), Inicio.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_cerrar, container, false);
    }
}