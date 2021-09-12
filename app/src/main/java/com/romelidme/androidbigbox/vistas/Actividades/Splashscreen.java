package com.romelidme.androidbigbox.vistas.Actividades;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.ISplashscreen;
import wholetec.cliente.bigbox.mvp.Presenter.PSplashscreen;


public class    Splashscreen extends AppCompatActivity implements ISplashscreen.View {
    private ISplashscreen.Presenter presenter;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        castView();
    }

    private void castView() {
        presenter=new PSplashscreen(this,this);
        iv=findViewById(R.id.iv_splashscreen);
        presenter.animacion(iv);
    }
}
