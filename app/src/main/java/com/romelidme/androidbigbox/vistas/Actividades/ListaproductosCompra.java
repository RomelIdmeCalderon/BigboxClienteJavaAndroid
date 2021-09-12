package com.romelidme.androidbigbox.vistas.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosCompra;
import wholetec.cliente.bigbox.mvp.Presenter.PListaproductosCompra;

public class ListaproductosCompra extends AppCompatActivity  implements IListaproductosCompra.View {
    private TextView mTopToolbarCompra;
    private FloatingActionButton btnAgregarProducto;
    private IListaproductosCompra.Presenter presenter;
    private RecyclerView rv;
    private Button btnRealizarPedido;
    String idProveedor,nombreProveedor,nombreCompra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listaproductos_compra);
        castView();
        btnRealizarPedido.setOnClickListener(v -> {
            presenter.hacerPedido(idProveedor);
        });
    }

    private void castView() {
        if(getIntent()!= null && getIntent().getExtras()!= null){
            idProveedor = getIntent().getExtras().getString("proveedor");
            nombreProveedor = getIntent().getExtras().getString("nombreProveedor");
            nombreCompra = getIntent().getExtras().getString("nombre");
            presenter = new PListaproductosCompra(this,this);
            rv = findViewById(R.id.rv_productoscompra);
            mTopToolbarCompra = findViewById(R.id.toolbar_title_compra);
            btnAgregarProducto = findViewById(R.id.btn_agregarproductoscompra);
            btnRealizarPedido = findViewById(R.id.btn_listaproductoscompra_pedido);
            presenter.listarProductos(rv, idProveedor,nombreProveedor,nombreCompra);
            mTopToolbarCompra.setText(nombreCompra);
        }
    }

    @Override
    public void actualizar() {presenter.listarProductos(rv,idProveedor,nombreProveedor,nombreCompra); }
}