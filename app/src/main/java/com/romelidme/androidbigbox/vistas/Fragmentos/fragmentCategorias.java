package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.ICategorias;
import wholetec.cliente.bigbox.mvp.Presenter.PCategorias;


public class fragmentCategorias extends Fragment implements ICategorias.View {
    private ICategorias.Presenter presenter;
    private RecyclerView rv;
    private FloatingActionButton btnAgregarCategorias;
    public fragmentCategorias() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);
        castView(view);
        btnAgregarCategorias.setOnClickListener(v->{
            presenter.crearCategoria();
        });
        return view;
    }
    public void castView(View vista) {
        presenter = new PCategorias(this,getActivity());
        btnAgregarCategorias = vista.findViewById(R.id.btncategorias);
        rv = vista.findViewById(R.id.rvcategorias);
        presenter.listarCategorias(rv);
    }

    @Override
    public void actualizar() {
        presenter.listarCategorias(rv);
    }
}