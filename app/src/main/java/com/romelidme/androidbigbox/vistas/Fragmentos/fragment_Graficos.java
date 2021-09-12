package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IGraficos;
import wholetec.cliente.bigbox.mvp.Presenter.PGraficos;

public class fragment_Graficos extends Fragment implements IGraficos.View {
    private IGraficos.Presenter presenter;
    private RecyclerView rv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__graficos, container , false);
        castView(view);
        return  view;
    }

    private void castView(View view) {
        presenter = new PGraficos(this, getActivity());
        rv= view .findViewById(R.id.rvgraficos);
        presenter.listarProveedores(rv);
    }

    @Override
    public void actualizar() {
        presenter.listarProveedores(rv);
    }
}