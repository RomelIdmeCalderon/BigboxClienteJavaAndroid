package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IHistorial;
import wholetec.cliente.bigbox.mvp.Presenter.PHistorial;

public class fragmentHistorial extends Fragment implements IHistorial.View {

    private IHistorial.Presenter presenter;
    private RecyclerView rv;

    public fragmentHistorial(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);
        castView(view);
        return view;
    }

    private void castView(View vista) {
        presenter = new PHistorial(this,getActivity());
        rv= vista.findViewById(R.id.rvhistorial);
        presenter.listarHistorial(rv);
    }

    @Override
    public void actualizar() {
            presenter.listarHistorial(rv);
    }
}