package com.example.prueba1b2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class LogActivity extends AppCompatActivity {

    Button btn_signUp, btn_signIn;
    EditText email, password;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Object AlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signIn = findViewById(R.id.btn_signIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        setup();

    }

    private void setup() {
        setTitle("Inicio");

        btn_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((email.getText().toString()).isEmpty() && (password.getText().toString()).isEmpty())
                {
                    showAlertNull();
                }
                else
                {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                String email = task.getResult().getUser().getEmail();
                                showAgenda(email);
                                Map<String, Object> user = new HashMap<>();
                                user.put("uid", uid);
                                user.put("email", email);
                                db.collection("users").add(user);
                            }
                            else {
                                showAlertRegistrado();
                            }
                        }
                    });
                }
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
                    showAlertNull();
                }
                else{
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString()
                            , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String email= task.getResult().getUser().getEmail();
                                showAgenda(email);

                            } else {
                                showAlertUsuarioNR();
                            }
                        }
                    });
                }
            }
        });


    }

    private void showAlertNull(){
        AlertDialog = new AlertDialog.Builder(this);
        ((AlertDialog.Builder) AlertDialog).setTitle("Error");
        ((AlertDialog.Builder) AlertDialog).setMessage("Por Favor rellene los campos");
        ((AlertDialog.Builder) AlertDialog).setPositiveButton("Aceptar", null);
        android.app.AlertDialog dialog = ((AlertDialog.Builder) AlertDialog).create();
        dialog.show();
    }
    private void showAlertRegistrado() {
        AlertDialog = new AlertDialog.Builder(this);
        ((AlertDialog.Builder) AlertDialog).setTitle("Error");
        ((AlertDialog.Builder) AlertDialog).setMessage("El Usuario ya se encuentra Registrado");
        ((AlertDialog.Builder) AlertDialog).setPositiveButton("Aceptar", null);
        android.app.AlertDialog dialog = ((AlertDialog.Builder) AlertDialog).create();
        dialog.show();
    }
    private void showAlertUsuarioNR() {
        AlertDialog = new AlertDialog.Builder(this);
        ((AlertDialog.Builder) AlertDialog).setTitle("Error");
        ((AlertDialog.Builder) AlertDialog).setMessage("El Usuario no se encuentra Reistrado");
        ((AlertDialog.Builder) AlertDialog).setPositiveButton("Aceptar", null);
        android.app.AlertDialog dialog = ((AlertDialog.Builder) AlertDialog).create();
        dialog.show();
    }

    private void showAgenda(String email) {
        Intent homeIntent = new Intent(this, AgendaActivity.class).putExtra("email", email);
        startActivity(homeIntent);

    }

}