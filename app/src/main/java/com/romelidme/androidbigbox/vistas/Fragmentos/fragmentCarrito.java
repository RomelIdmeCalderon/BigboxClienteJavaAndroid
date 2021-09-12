package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.ICarrito;
import wholetec.cliente.bigbox.mvp.Presenter.PCarrito;


public class fragmentCarrito extends Fragment implements ICarrito.View {
    private ICarrito.Presenter presenter;
    private RecyclerView rv;
    private FloatingActionButton btnAgregarCarrito;

    public fragmentCarrito() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_carrito, container, false);
        presenter = new PCarrito(this,getActivity());
        castView(view);

        return view;
    }

    private void castView(View view) {
        presenter = new PCarrito(this,getActivity());
        btnAgregarCarrito = view.findViewById(R.id.btncarrito);
        rv = view.findViewById(R.id.rvcarrito);
        presenter.listarCompras(rv);
    }

    @Override
    public void actualizar() { presenter.listarCompras(rv);

    }
}