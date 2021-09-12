package com.romelidme.androidbigbox.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;
import wholetec.cliente.bigbox.entidades.Negocio_Entidad;

public class Common {
    public static AlertDialog dialog;
    private static FirebaseAuth auth;
    private static FirebaseUser user;
    public static int VERSIONAPP=1;
    public static Negocio_Entidad entidadNegocio= null;
    public static DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

    public static boolean isConnect(){
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        if(user!=null && user.getUid()!=null)
            return true;
        return false;
    }
    public static void loading(Context context){
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
        dialog = new SpotsDialog.Builder().setContext(context).setMessage("Cargando...").build();
        dialog.show();
    }
    public static void close(){
        if(dialog!=null && dialog.isShowing())
            dialog.dismiss();
    }
}
