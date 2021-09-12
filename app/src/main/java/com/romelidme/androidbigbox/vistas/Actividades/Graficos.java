package com.romelidme.androidbigbox.vistas.Actividades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IAGraficos;

public class Graficos extends AppCompatActivity implements IAGraficos.View {
    private TextView mTopToolbar;
    String idProveedor;
    String nombreProveedor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);
        castView();
    }

    private void castView() {
        idProveedor = getIntent().getExtras().getString("proveedor");
        nombreProveedor = getIntent().getExtras().getString("nombre");
        mTopToolbar = findViewById(R.id.toolbar_title_grafico);
        mTopToolbar.setText("Graficos de "+ nombreProveedor);
    }
}