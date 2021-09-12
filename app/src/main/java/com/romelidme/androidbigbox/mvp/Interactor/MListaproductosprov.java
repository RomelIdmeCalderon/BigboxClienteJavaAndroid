package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.adaptadores.Adapter_Productos_DProveedores;
import wholetec.cliente.bigbox.adaptadores.Adapter_Productos_Proveedores;
import wholetec.cliente.bigbox.entidades.Precio_Entidad;
import wholetec.cliente.bigbox.entidades.Producto_Carrito_Entidad;
import wholetec.cliente.bigbox.entidades.Producto_Entidad;
import wholetec.cliente.bigbox.interfaces.IRDProductosProveedores;
import wholetec.cliente.bigbox.interfaces.IRProductosProveedores;
import wholetec.cliente.bigbox.mvp.Interface.IListaproductosprov;
import wholetec.cliente.bigbox.utils.Common;


public class MListaproductosprov implements IListaproductosprov.Interactor, IRDProductosProveedores,IRProductosProveedores {
    private Context context;
    private IListaproductosprov.Presenter presenter;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Adapter_Productos_Proveedores adapter= null;
    private Adapter_Productos_DProveedores dadapter= null;
    private StorageReference ref_storage=null;
    private Uri uri=null;
    private Dialog alertagrgarProducto;
    private EditText etNombre;
    private Button btnCerrar;
    private Button btnCerrarPrecio,btnCrearPrecio;
    private Button btnCerrarMejorPrecio,btnNuevoPrecio,btnMismoPrecio;
    private RecyclerView rv;
    private SearchView svSearch;
    private EditText etPrecio;
    private TextView etProveedor,etMejorPrecio;
    private String nombreProv,idProv;
    public MListaproductosprov(Context context, IListaproductosprov.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    @Override
    public void agregarProducto(String idProveedor) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialoglayout = inflater.inflate(R.layout.dialog_agregarproductos, null);
        castViewProducto(dialoglayout);
        builder.setCancelable(true);
        builder.setView(dialoglayout);
        alertagrgarProducto = builder.show();
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarProducto(newText);
                return true;
            }
        });
        svSearch.setOnCloseListener(() -> {
            listarProductos(rv);
            return false;
        });
        btnCerrar.setOnClickListener(view -> alertagrgarProducto.dismiss());
    }

    private boolean validar() {
        if(TextUtils.isEmpty(etPrecio.getText().toString().trim())){
            etPrecio.setError("Ingrese el precio");
           return false;
        }
        return true;
    }

    private void castViewProducto(View view) {
        btnCerrar = view.findViewById(R.id.btn_dagregarproductos_cerrar);
        svSearch= view.findViewById(R.id.sv_dagregarproductos_search);
        rv = view.findViewById(R.id.rv_productosproveedorestext);
        listarProductos(rv);
    }

    public void listarProductos(RecyclerView rv) {
        if (Common.isConnect()) {
            Common.ref.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Producto_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Producto_Entidad entidad = area.getValue(Producto_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdTienda().equals(Common.entidadNegocio.getId()))
                                lista.add(entidad);

                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterProductosProveedores(rv, lista);
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
    public void listarProductos(RecyclerView rv, String idProveedor,String nombreProveedor) {
        idProv =idProveedor;
        nombreProv=nombreProveedor;
        if(Common.isConnect()){
            Common.ref.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Producto_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Producto_Entidad entidad = area.getValue(Producto_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getPrecioProveedores().contains(idProveedor)) {
                                Common.ref.child("Productos").child(entidad.getId()).child("precios").child(idProveedor).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        if(snapshot2.exists()) {
                                            entidad.setPrecio(snapshot2.child("precio").getValue(Double.class));
                                            lista.add(entidad);
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

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


    private void setAdapterProductos(RecyclerView rv, ArrayList<Producto_Entidad> lista) {
        adapter = new Adapter_Productos_Proveedores(context,lista,  this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void setAdapterProductosProveedores(RecyclerView rv, ArrayList<Producto_Entidad> lista) {
        dadapter = new Adapter_Productos_DProveedores(context,lista,this);
        rv.setAdapter(dadapter);
        dadapter.notifyDataSetChanged();
    }


    private void buscarProducto(String query) {
        if (Common.isConnect()) {
            Common.ref.child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ArrayList<Producto_Entidad> lista = new ArrayList<>();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Producto_Entidad entidad = area.getValue(Producto_Entidad.class);
                            entidad.setId(area.getKey());
                            if (entidad.getIdTienda().equals(Common.entidadNegocio.getId())) {
                                if(entidad.getNombre().contains(query))
                                    lista.add(entidad);
                            }

                        }
                        if (lista != null && lista.size() > 0) {
                            setAdapterProductosProveedores(rv, lista);
                            rv.setVisibility(View.VISIBLE);
                        }
                        else
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
    public void ponerprecio(String idProducto) {
        if (Common.isConnect()) {
            Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Producto_Entidad entidad = snapshot.getValue(Producto_Entidad.class);
                        entidad.setId(snapshot.getKey());
                        if (entidad.getPrecioProveedores().contains(idProv)) {
                            Toast.makeText(context,"Ya tiene el producto",Toast.LENGTH_SHORT).show();
                        } else {
                            alertagrgarProducto.dismiss();
                            Dialog alert;
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            final View dialoglayout = inflater.inflate(R.layout.dialog_precio, null);
                            castViewPrecio(dialoglayout);
                            builder.setCancelable(true);
                            builder.setView(dialoglayout);
                            alert = builder.show();
                            btnCerrarPrecio.setOnClickListener(view -> alert.dismiss());
                            btnCrearPrecio.setOnClickListener(view -> {
                                        if (validar()) {
                                            if (Common.isConnect()) {
                                                Precio_Entidad precio = new Precio_Entidad();
                                                precio.setIdProveedor(idProv);
                                                precio.setNombreProveedor(nombreProv);
                                                precio.setPrecio(Double.parseDouble(etPrecio.getText().toString().trim()));
                                                HashMap<String, Object> map = new HashMap<>();
                                                Common.ref.child("Productos").child(idProducto).child("precios").child(idProv).setValue(precio).addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                map.put("precioProveedores", snapshot.child("precioProveedores").getValue() + "~" + idProv);
                                                                Common.ref.child("Productos").child(idProducto).updateChildren(map);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                        alert.dismiss();
                                                        presenter.actualizar();
                                                    }
                                                });
                                            }
                                        }
                                    }
                            );

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
    private void castViewPrecio(View view) {
        etPrecio = view.findViewById(R.id.et_dprecio_precio);
        btnCerrarPrecio = view.findViewById(R.id.btn_dprecio_cerrar);
        btnCrearPrecio = view.findViewById(R.id.btn_dprecio_crear);
        btnCrearPrecio.setEnabled(true);
    }

    @Override
    public void editar(String idProducto) {
        Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Producto_Entidad entidad = snapshot.getValue(Producto_Entidad.class);
                    entidad.setId(snapshot.getKey());
                    if (entidad.getPrecioProveedores().contains(idProv)) {
                        Dialog alert;
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final View dialoglayout = inflater.inflate(R.layout.dialog_precio, null);
                        castVieweditarPrecio(dialoglayout);
                        builder.setCancelable(true);
                        builder.setView(dialoglayout);
                        alert = builder.show();
                        btnCerrarPrecio.setOnClickListener(view -> alert.dismiss());
                        btnCrearPrecio.setOnClickListener(view -> {
                                    if (validar()) {
                                        if (Common.isConnect()) {
                                            HashMap<String, Object> map = new HashMap<>();
                                            map.put("idProveedor",idProv);
                                            map.put("nombreProveedor",nombreProv);
                                            map.put("precio",Double.parseDouble(etPrecio.getText().toString().trim()));
                                            Common.ref.child("Productos").child(idProducto).child("precios").child(idProv).updateChildren(map).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context,"precio "+ map.get("precio"),Toast.LENGTH_SHORT).show();
                                                    alert.dismiss();
                                                    presenter.actualizar();
                                                }
                                            });
                                        }
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(context,"El producto no existe error revisar",Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void castVieweditarPrecio(View view) {
        etPrecio = view.findViewById(R.id.et_dprecio_precio);
        btnCerrarPrecio = view.findViewById(R.id.btn_dprecio_cerrar);
        btnCrearPrecio = view.findViewById(R.id.btn_dprecio_crear);
        btnCrearPrecio.setEnabled(true);

    }

    @Override
    public void eliminar(String idProducto) {
        Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String precioProveedores = snapshot.child("precioProveedores").getValue().toString();
                if(precioProveedores.contains("~"+ idProv)){
                    precioProveedores=precioProveedores.replace("~"+ idProv,"");
                }else {
                    precioProveedores = precioProveedores.replace(idProv, "");
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("precioProveedores", precioProveedores);
                Common.ref.child("Productos").child(idProducto).updateChildren(map).addOnCompleteListener(task->{
                    if(task.isSuccessful())
                    {
                        Common.ref.child("Productos").child(idProducto).child("precio").child(idProv).removeValue().addOnCompleteListener(task1 ->{
                            if(task1.isSuccessful()){
                                Toast.makeText(context, "eliminado correctamente", Toast.LENGTH_SHORT).show();
                                presenter.actualizar();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void agregarCarrito(String idProducto,int cantidad) {
        if(cantidad >0)
        {
            Common.ref.child("Productos").child(idProducto).child("precios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        double a = 100000;
                        double b = Double.parseDouble(snapshot.child(idProv).child("precio").getValue().toString());
                        Precio_Entidad entidad2 = new Precio_Entidad();
                        for (DataSnapshot area : snapshot.getChildren()) {
                            Precio_Entidad entidad = area.getValue(Precio_Entidad.class);
                            entidad.getPrecio();
                            if (entidad.getPrecio() < a) {
                                entidad2 = area.getValue(Precio_Entidad.class);
                                a = entidad2.getPrecio();
                            }
                        }
                        if (entidad2.getNombreProveedor().equals(nombreProv)) {
                            subirCarrito(entidad2.getIdProveedor(), idProducto, cantidad);
                        } else {
                            Dialog alert;
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            final View dialoglayout = inflater.inflate(R.layout.dialog_mejorprecio, null);
                            castDCarrito(dialoglayout);
                            String Proveedor = entidad2.getIdProveedor();
                            etProveedor.setText(entidad2.getNombreProveedor());
                            etMejorPrecio.setText("S/." + entidad2.getPrecio());
                            builder.setCancelable(true);
                            builder.setView(dialoglayout);
                            alert = builder.show();
                            btnCerrarMejorPrecio.setOnClickListener(v -> {
                                alert.dismiss();
                            });
                            btnMismoPrecio.setOnClickListener(v -> {
                                subirCarrito(idProv, idProducto, cantidad);
                                alert.dismiss();
                            });
                            btnNuevoPrecio.setOnClickListener(v -> {
                                subirCarrito(Proveedor, idProducto, cantidad);
                                alert.dismiss();
                            });
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else{
            Toast.makeText(context,"debe de ingresar una cantidad mayor a cero (0)",Toast.LENGTH_SHORT).show();
        }
    }

    private void castDCarrito(View view) {
        btnCerrarMejorPrecio = view.findViewById(R.id.btn_dmejorprecio_cerrar);
        btnMismoPrecio = view.findViewById(R.id.btn_dmejorprecio_mismo);
        btnNuevoPrecio = view.findViewById(R.id.btn_dmejorprecio_nuevo);
        etProveedor = view.findViewById(R.id.tv_dmejorprecio_proveedor);
        etMejorPrecio = view.findViewById(R.id.tv_dmejorprecio_precio);

    }

    private void subirCarrito(String proveedor, String idProducto, int cantidad) {
        Common.ref.child("Proveedor").child(proveedor).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numero =snapshot.child("telefono").getValue().toString();
                Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if(snapshot.child("Productos").child(idProducto).exists()){
                                Toast.makeText(context,"Actualizar ",Toast.LENGTH_SHORT).show();
                                Producto_Carrito_Entidad entidad = new Producto_Carrito_Entidad();
                                Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Double a = Double.parseDouble(snapshot.child("precios").child(proveedor).child("precio").getValue().toString());
                                            entidad.setId(idProducto);
                                            entidad.setNombre(snapshot.child("nombre").getValue().toString());
                                            entidad.setCantidad(cantidad);
                                            entidad.setPrecio(a);
                                            Double b = cantidad * a;
                                            entidad.setTotal(b);
                                            Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("Productos").child(idProducto).setValue(entidad).addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context,"producto agregado",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled (@NonNull DatabaseError error){

                                    }

                                });
                            }else{
                                Producto_Carrito_Entidad entidad = new Producto_Carrito_Entidad();
                                Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Double a = Double.parseDouble(snapshot.child("precios").child(proveedor).child("precio").getValue().toString());
                                            entidad.setId(idProducto);
                                            entidad.setNombre(snapshot.child("nombre").getValue().toString());
                                            entidad.setCantidad(cantidad);
                                            entidad.setPrecio(a);
                                            Double b = cantidad * a;
                                            entidad.setTotal(b);

                                            Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("Productos").child(idProducto).setValue(entidad).addOnCompleteListener(task -> {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context,"producto agregado",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            //
                                        }
                                    }
                                    @Override
                                    public void onCancelled (@NonNull DatabaseError error){

                                    }

                                });

                            }

                        }else{
                            Producto_Carrito_Entidad entidad = new Producto_Carrito_Entidad();
                            Common.ref.child("Productos").child(idProducto).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Double a = Double.parseDouble(snapshot.child("precios").child(proveedor).child("precio").getValue().toString());
                                        entidad.setId(idProducto);
                                        entidad.setNombre(snapshot.child("nombre").getValue().toString());
                                        entidad.setCantidad(cantidad);
                                        entidad.setPrecio(a);
                                        Double b = cantidad * a;
                                        entidad.setTotal(b);
                                        Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("nombreProveedor").setValue(snapshot.child("precios").child(proveedor).child("nombreProveedor").getValue());
                                        Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("id").setValue(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId());
                                        Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("nombre").setValue("Compra Nueva");
                                        Common.ref.child("Carrito").child(user.getUid()+"~"+proveedor+"~"+Common.entidadNegocio.getId()).child("telefono").setValue(numero);
                                        Common.ref.child("Carrito").child(user.getUid() + "~" + proveedor+"~"+Common.entidadNegocio.getId()).child("Productos").child(idProducto).setValue(entidad).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "producto agregado", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled (@NonNull DatabaseError error){

                                }

                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
