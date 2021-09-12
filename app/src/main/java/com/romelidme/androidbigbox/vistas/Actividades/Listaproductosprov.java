package com.romelidme.androidbigbox.vistas.Actividades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosprov;
import wholetec.cliente.bigbox.mvp.Presenter.PListaproductosprov;

public class Listaproductosprov extends AppCompatActivity implements IListaproductosprov.View {
    private TextView mTopToolbar;
    private FloatingActionButton btnAgregarProducto;
    private IListaproductosprov.Presenter presenter;
    private RecyclerView rv;
    String idProveedor,nombreProveedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaproductosprov);
        castView();

        btnAgregarProducto.setOnClickListener(v->{
            presenter.agregarProducto(idProveedor);
        });
    }

    private void castView() {
        if(getIntent()!= null && getIntent().getExtras()!= null){
            idProveedor = getIntent().getExtras().getString("proveedor");
            nombreProveedor = getIntent().getExtras().getString("nombre");
            presenter = new PListaproductosprov(this,this);
            rv = findViewById(R.id.rv_productosproveedores);
            mTopToolbar = findViewById(R.id.toolbar_title);
            btnAgregarProducto = findViewById(R.id.btn_agregarproductosprov);
            presenter.listarProductos(rv, idProveedor,nombreProveedor);
            mTopToolbar.setText(nombreProveedor);
        }
    }

    @Override
    public void actualizar() {
        presenter.listarProductos(rv,idProveedor,nombreProveedor);
    }
}