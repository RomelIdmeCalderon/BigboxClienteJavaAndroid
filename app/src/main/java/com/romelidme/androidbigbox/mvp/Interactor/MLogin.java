
package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import wholetec.cliente.bigbox.entidades.Cliente_Entidad;
import wholetec.cliente.bigbox.mvp.Interface.ILogin;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.utils.Fecha;
import wholetec.cliente.bigbox.vistas.Actividades.Principal;
import wholetec.cliente.bigbox.vistas.Actividades.Slider;

public class MLogin implements ILogin.Interactor {
    private Context context;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;

    public MLogin(Context context) {
        this.context = context;
        mAuth= FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public void iniciarFacebook() {
        FirebaseUser user = mAuth.getCurrentUser();
        Common.loading(context);
        ref.child("Clientes").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Common.close();
                    Intent i = new Intent(context, Slider.class);
                    context.startActivity(i);
                }
                else{
                    Cliente_Entidad entidad = new Cliente_Entidad();
                    entidad.setCelular("");
                    entidad.setCorreo("");
                    entidad.setImagen("default");
                    entidad.setNombre(mAuth.getCurrentUser().getDisplayName());
                    entidad.setToken(FirebaseInstanceId.getInstance().getToken());
                    entidad.setFechaCreacion(Fecha.Actual_Formato());
                    entidad.setDni("");
                    ref.child("Clientes").child(user.getUid()).setValue(entidad).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(context, "Bienvenido.", Toast.LENGTH_LONG).show();
                            Common.close();
                            Intent i = new Intent(context, Slider.class);
                            context.startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Intent i = new Intent(context, slider.class);
        // context.startActivity(i);
    }

    @Override
    public void registrarGoogle(String idToken ) {
        Common.loading(context);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ref.child("Clientes").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){

                                        Common.close();
                                        Intent i = new Intent(context, Slider.class);
                                        context.startActivity(i);
                                        ((Activity)context).finish();
                                    }
                                    else {
                                        Cliente_Entidad entidad = new Cliente_Entidad();
                                        entidad.setCelular("");
                                        entidad.setCorreo(""+mAuth.getCurrentUser().getEmail());
                                        entidad.setImagen("default");
                                        entidad.setNombre(mAuth.getCurrentUser().getDisplayName());
                                        entidad.setToken(FirebaseInstanceId.getInstance().getToken());
                                        entidad.setFechaCreacion(Fecha.Actual_Formato());
                                        entidad.setDni("");
                                        ref.child("Clientes").child(mAuth.getCurrentUser().getUid()).setValue(entidad).addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){
                                                Common.close();
                                                Intent i = new Intent(context, Slider.class);
                                                context.startActivity(i);
                                                ((Activity)context).finish();
                                            }
                                            else {
                                                Log.i("infoErrrorr",""+task1.getException().getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            // Sign in success, update UI with the signed-in user's information
                         /*   Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(context, slider.class);
                            context.startActivity(intent);*/
                        } else {
                            Common.close();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context,"error al ingresar",Toast.LENGTH_SHORT);
                            mAuth.signOut();
                        }
                        // ...
                });
    }

    @Override
    public void verificarSesion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        if(user!=null && user.getUid()!=null){
            Intent i = new Intent(context, Principal.class);
            context.startActivity(i);
            ((Activity)context).finish();
        }
    }
}


