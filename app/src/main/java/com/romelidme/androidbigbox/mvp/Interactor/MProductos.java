package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Productos;
import wholetec.cliente.bigbox.entidades.Categoria_Entidad;
import wholetec.cliente.bigbox.entidades.Medida_Entidad;
import wholetec.cliente.bigbox.entidades.Producto_Entidad;
import wholetec.cliente.bigbox.interfaces.IRProductos;
import wholetec.cliente.bigbox.mvp.Interface.IProductos;
import wholetec.cliente.bigbox.utils.Common;

public class MProductos implements IProductos.Iteractor, IRProductos {
    private Context context;
    private IProductos.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Productos adapter= null;
    private StorageReference ref_storage=null;
    private Uri uri=null;


    private EditText etNombre,etDescripcion;
    private Button btnAceptar,btnCerrar;
    private ImageView ivFoto;

    private Spinner spCategoria,spMedida;
    private ArrayList<Categoria_Entidad> listaCategoria;
    private ArrayList<Medida_Entidad> listaMedida;
    private ArrayList<String> listaCategoriaST,listaMedidaST;
    public MProductos(Context context, IProductos.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public void crearProducto() {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_producto, null);
        castViewProducto(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
            if(validar()){
                btnAceptar.setEnabled(false);
                Producto_Entidad entidad = new Producto_Entidad();
                entidad.setDescripcion(etDescripcion.getText().toString().trim());
                entidad.setIdCategoria(listaCategoria.get(spCategoria.getSelectedItemPosition()).getId());
                entidad.setIdMedida(listaMedida.get(spMedida.getSelectedItemPosition()).getId());
                entidad.setIdTienda(Common.entidadNegocio.getId());
                entidad.setIdUsuario(user.getUid());
                entidad.setImagen("default");
                entidad.setPrecio(0.0);
                entidad.setCantidad(0);
                entidad.setPrecioProveedores("default");
                entidad.setNombre(etNombre.getText().toString().trim());
                entidad.setNombreCategoria(listaCategoriaST.get(spCategoria.getSelectedItemPosition()));
                entidad.setNombreMedida(listaMedidaST.get(spMedida.getSelectedItemPosition()));
                String idProducto = Common.ref.child("Productos").push().getKey();
                Common.ref.child("Productos").child(idProducto).setValue(entidad).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(uri!=null){
                            alert.dismiss();
                            agregarImagen(idProducto,0);
                        }
                        else{
                            Toast.makeText(context, context.getString(R.string.productocreadook), Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                            presenter.actualizar();
                        }
                    }
                });
            }
        });
        btnCerrar.setOnClickListener(view -> alert.dismiss());

    }
    private void agregarImagen(String idProducto,int mensaje) {
        if (auth.getCurrentUser() == null)
            return;
        if (ref_storage == null)
            ref_storage = FirebaseStorage.getInstance().getReference();
        final StorageReference filepath = ref_storage.child("Productos").child(idProducto);
        Common.loading(context);
        filepath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> downloadUri = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String urlImagen = String.valueOf(task.getResult());
                    Map<String,Object> map = new HashMap<>();
                    map.put("imagen",urlImagen);
                    Common.ref.child("Productos").child(idProducto).updateChildren(map).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            this.uri=null;
                            if(mensaje==0)
                                Toast.makeText(context, context.getString(R.string.productocreadook), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, context.getString(R.string.productoeditadook), Toast.LENGTH_SHORT).show();
                            Common.close();
                            presenter.actualizar();
                        }
                    });
                }
                else
                    Common.close();

            });
        }).addOnFailureListener(e -> {
            Common.close();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
    private boolean validar(){
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            etNombre.setError(context.getString(R.string.ingresenombre));
            return false;
        }
        if(TextUtils.isEmpty(etDescripcion.getText().toString().trim())){
            etDescripcion.setError(context.getString(R.string.ingresedescripcion));
            return false;
        }
        if(spCategoria.getSelectedItemPosition()==0){
            Toast.makeText(context, "Seleccione su categoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(spMedida.getSelectedItemPosition()==0){
            Toast.makeText(context, "Seleccione su medida", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private void castViewProducto(View view){
        btnCerrar= view.findViewById(R.id.btn_dproducto_cerrar);
        etNombre = view.findViewById(R.id.et_dproducto_nombre);
        etDescripcion= view.findViewById(R.id.et_dproducto_descripcion);
        btnAceptar = view.findViewById(R.id.btn_dproducto_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dproducto);
        spCategoria = view.findViewById(R.id.sp_dproducto_categoria);
        spMedida = view.findViewById(R.id.sp_dproducto_medida);
        categoriaSpinner(null);
        medidaSpinner(null);
    }

    private void categoriaSpinner(String idCategoria) {
        if(Common.isConnect()){
            Common.ref.child("Categorias").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listaCategoria = new ArrayList<>();
                        listaCategoriaST = new ArrayList<>();
                        Categoria_Entidad entidad = new Categoria_Entidad();
                        entidad.setId("default");
                        entidad.setNombre("Selecciona tu Categoria");
                        listaCategoriaST.add(entidad.getNombre());
                        listaCategoria.add(entidad);

                        for(DataSnapshot area : dataSnapshot.getChildren()){
                            Categoria_Entidad entidad1 = area.getValue(Categoria_Entidad.class);
                            entidad1.setId(area.getKey());
                            if (entidad1.getIdTienda().equals(Common.entidadNegocio.getId())) {
                                listaCategoriaST.add(entidad1.getNombre());
                                listaCategoria.add(entidad1);
                            }
                        }
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaCategoriaST);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spCategoria.setAdapter(addressAdapter);
                        if(idCategoria!=null){
                            for(int i=0;i<listaCategoria.size();i++) {
                                if (listaCategoria.get(i).getId().equals(idCategoria)) {
                                    spCategoria.setSelection(i);
                                }
                            }
                        }
                        Common.close();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


    private void medidaSpinner(String idMedida) {
        if(Common.isConnect()){
            Common.ref.child("Medidas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listaMedida = new ArrayList<>();
                        listaMedidaST = new ArrayList<>();
                        Medida_Entidad entidad = new Medida_Entidad();
                        entidad.setId("default");
                        entidad.setNombreMedida("Seleccionar Medida");
                        listaMedidaST.add(entidad.getNombreMedida());
                        listaMedida.add(entidad);
                        for(DataSnapshot area : dataSnapshot.getChildren()){
                            Medida_Entidad entidad1 = area.getValue(Medida_Entidad.class);
                            entidad1.setId(area.getKey());
                            if (entidad1.getIdUsuario().equals(user.getUid())) {
                                listaMedidaST.add(entidad1.getNombreMedida());
                                listaMedida.add(entidad1);
                            }
                        }
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listaMedidaST);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spMedida.setAdapter(addressAdapter);
                        if(Common.entidadNegocio!=null){
                            for(int i=0;i<listaMedida.size();i++){
                                if(listaMedida.get(i).getId().equals(idMedida)){
                                    spMedida.setSelection(i);
                                }
                            }
                        }

                        Common.close();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void listarProductos(RecyclerView rv) {
        if(Common.isConnect()){
            Common.ref.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Producto_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Producto_Entidad entidad = area.getValue(Producto_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdTienda().equals(Common.entidadNegocio.getId())) {
                                lista.add(entidad);
                            }
                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterProductos(rv, lista);
                            rv.setVisibility(View.VISIBLE);
                        } else
                            rv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void uploadFoto(Uri uri) {
        this.uri = uri;
        Picasso.with(context).load(uri).fit().error(R.drawable.logov).into(ivFoto);
    }

    private void setAdapterProductos(RecyclerView rv, ArrayList<Producto_Entidad> lista) {
        adapter = new Adapter_Productos(context,lista,this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void eliminar(String idProducto) {
        if(Common.isConnect()){
            Common.ref.child("Productos").child(idProducto).removeValue().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(context,"Eliminado correctamente",Toast.LENGTH_SHORT).show();
                    presenter.actualizar();
                }
            });
        }

    }

    @Override
    public void editar(Producto_Entidad entidad) {
        Dialog alert;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_producto, null);
        castViewProducto(dialoglayout,entidad);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alert = builder.show();
        ivFoto.setOnClickListener(view -> presenter.uploadFoto());
        btnAceptar.setOnClickListener(view -> {
                if (validar()){
                    btnAceptar.setEnabled(false);
                    Map<String,Object> map = new HashMap<>();
                    map.put("descripcion",etDescripcion.getText().toString().trim());
                    map.put("idCategoria",listaCategoria.get(spCategoria.getSelectedItemPosition()).getId());
                    map.put("idMedida",listaMedida.get(spMedida.getSelectedItemPosition()).getId());
                    map.put("nombre",etNombre.getText().toString().trim());
                    map.put("nombreCategoria",listaCategoriaST.get(spCategoria.getSelectedItemPosition()));
                    map.put("nombreMedida",listaMedidaST.get(spMedida.getSelectedItemPosition()));
                    Common.ref.child("Productos").child(entidad.getId()).updateChildren(map).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            if(uri!=null){
                                alert.dismiss();
                                Toast.makeText(context, context.getString(R.string.productoeditadook), Toast.LENGTH_SHORT).show();
                                agregarImagen(entidad.getId(),1);
                                presenter.actualizar();
                            }
                            else{
                                Toast.makeText(context, context.getString(R.string.productoeditadook), Toast.LENGTH_SHORT).show();
                                alert.dismiss();
                                presenter.actualizar();
                            }
                        }
                    });
                }
        });

    }
    private void castViewProducto(View view,Producto_Entidad entidad){
        btnCerrar= view.findViewById(R.id.btn_dproducto_cerrar);
        etNombre = view.findViewById(R.id.et_dproducto_nombre);
        etDescripcion= view.findViewById(R.id.et_dproducto_descripcion);
        btnAceptar = view.findViewById(R.id.btn_dproducto_crear);
        btnAceptar.setEnabled(true);
        ivFoto = view.findViewById(R.id.iv_dproducto);
        spCategoria = view.findViewById(R.id.sp_dproducto_categoria);
        spMedida = view.findViewById(R.id.sp_dproducto_medida);
        categoriaSpinner(entidad.getIdCategoria());
        medidaSpinner(entidad.getIdMedida());
        etNombre.setText(entidad.getNombre());
        etDescripcion.setText(entidad.getDescripcion());
        Picasso.with(context).load(entidad.getImagen()).fit().error(R.drawable.logov).into(ivFoto);
        btnAceptar.setText("Editar");
    }
}
