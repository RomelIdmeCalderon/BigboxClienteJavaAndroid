package com.romelidme.androidbigbox.vistas.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import wholetec.cliente.bigbox.R;
import wholetec.cliente.bigbox.mvp.Interface.ILogin;
import wholetec.cliente.bigbox.mvp.Presenter.PLogin;

public class Inicio extends AppCompatActivity implements ILogin.View{
    private ILogin.Presenter presenter;
    private Button btnRegistrarFacebook,btnRegistrarGoogle;
    GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private static int RC_SIGN_IN= 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PLogin(this, this);
        presenter.verificarSesion();
        setContentView(R.layout.activity_inicio);
        FacebookSdk.sdkInitialize(getApplicationContext());
        castView();
        btnRegistrarGoogle.setOnClickListener(v->{
            iniciarGoogle();
        });
        btnRegistrarFacebook.setOnClickListener(v->{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken token = loginResult.getAccessToken();
                    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            presenter.iniciarFacebook();
                        }
                        else
                            Log.i("infointeractor","error btn: " + task.getException().getMessage());
                    });

                }
                @Override
                public void onCancel() {

                }
                @Override
                public void onError(FacebookException error) {
                    Log.i("nfoerrofacebok",":"+ error.getCause());
                    Toast.makeText(Inicio.this, "Error al iniciar Facebook: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

    }


    private void castView() {

        btnRegistrarFacebook = findViewById(R.id.btnRegFacebook);
        btnRegistrarGoogle = findViewById(R.id.btnRegGoogle);
        initGoogle();
    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }
    private void iniciarGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                presenter.registrarGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.i("nfogoogle","e:" + e.getMessage());
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

}