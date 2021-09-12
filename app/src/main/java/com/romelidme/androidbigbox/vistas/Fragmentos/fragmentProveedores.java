package com.romelidme.androidbigbox.vistas.Fragmentos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.IProveedores;
import wholetec.cliente.bigbox.mvp.Presenter.PProveedores;

import static android.app.Activity.RESULT_OK;


public class fragmentProveedores extends Fragment implements  IProveedores.View {

    private IProveedores.Presenter presenter;
    private FloatingActionButton btnAgregarProveedores;
    private RecyclerView rv;
    private int CAMERA_REQUEST_CODE = 1;
    public fragmentProveedores() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proveedores, container, false);
        castView(view);
        btnAgregarProveedores.setOnClickListener(v->{
            presenter.crearProveedor();
        });
        return view;
    }
    public void castView(View vista){
        presenter = new PProveedores(this,getActivity());
        btnAgregarProveedores = vista.findViewById(R.id.btnproveedores);
        rv = vista.findViewById(R.id.rvproveedores);
        presenter.listarProveedores(rv);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            if(uri!=null)
                presenter.uploadFoto(uri);
            else
                Toast.makeText(getActivity(), getString(R.string.imagennoexiste), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void uploadFoto() { obtenerPermisos();}

    @Override
    public void actualizar() {
        presenter.listarProveedores(rv);
    }

    private void obtenerPermisos() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
            }
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA_REQUEST_CODE);
            }
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA_REQUEST_CODE);
            }
            else
                iniciarFoto();
        }
        else
            iniciarFoto();
    }
    private void iniciarFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen para tu proveedor."),1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==CAMERA_REQUEST_CODE){
            if(grantResults.length>0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    obtenerPermisos();
                }
            }
        }
    }

}
