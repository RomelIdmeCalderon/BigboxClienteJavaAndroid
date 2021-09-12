package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IMedidas;
import wholetec.cliente.bigbox.mvp.Presenter.PMedidas;

public class fragmentMedidas extends Fragment implements  IMedidas.View {
    private IMedidas.Presenter presenter;
    private RecyclerView rv;
    private FloatingActionButton btnAgregarMedidas;

    public fragmentMedidas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medidas, container, false);
        castView(view);
        btnAgregarMedidas.setOnClickListener(v->{
            presenter.crearMedida();
        });
        return view;
    }

    private void castView(View vista) {
        presenter = new PMedidas(this,getActivity());
        btnAgregarMedidas = vista.findViewById(R.id.btnmedidas);
        rv = vista.findViewById(R.id.rvmedidas);
        presenter.listarMedidas(rv);

    }

    @Override
    public void actualizar() { presenter.listarMedidas(rv);    }
}