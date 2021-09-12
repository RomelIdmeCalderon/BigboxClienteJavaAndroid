package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import wholetec.cliente.bigbox.R;

public class fragmentSuscribirte extends Fragment {
    private Button btnSuscripcion;
    public fragmentSuscribirte() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suscribirte, container, false);
        castView(view);
        btnSuscripcion.setOnClickListener(v->{
            Toast.makeText(getContext(),"Probando Suscripcion",Toast.LENGTH_SHORT).show();
        });
        castView(view);
        return view;

    }

    private void castView(View vista) {
        btnSuscripcion= vista.findViewById(R.id.btn_suscribirte_anual);
    }
}