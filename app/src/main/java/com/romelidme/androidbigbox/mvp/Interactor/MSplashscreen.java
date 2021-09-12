package com.romelidme.androidbigbox.mvp.Interactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.ISplashscreen;
import wholetec.cliente.bigbox.utils.Common;
import wholetec.cliente.bigbox.vistas.Actividades.Inicio;

public class MSplashscreen implements ISplashscreen.Interactor {
    private Context context;

    public MSplashscreen(Context context) {
        this.context = context;
    }

    @Override
    public void animacion(ImageView iv) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        iv.startAnimation(anim);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                }
                catch (InterruptedException ex){
                    ex.printStackTrace();
                }
                finally {
                    verificarVersion();

                }
            }

            private void verificarVersion() {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                ref.child("Configuracion").child("version").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int version  = dataSnapshot.getValue(Integer.class);
                            if(version == Common.VERSIONAPP){
                                Intent i = new Intent(context, Inicio.class);
                                context.startActivity(i);
                                ((Activity)context).finish();
                            }
                            else {
                                Toast.makeText(context, "Hay una nueva versión disponible en la Play Store descargala.", Toast.LENGTH_LONG).show();
                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=wholetec.cliente.bigbox")));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=wholetec.cliente.bigbox")));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        timer.start();
    }
}
