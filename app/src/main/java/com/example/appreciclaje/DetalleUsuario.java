package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleUsuario extends AppCompatActivity {
    EditText txtNombre, txtBarrio, txtFechaNac, txtEmail, txtContraseña;
    ImageView imgUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtNombre = findViewById(R.id.txtNombreUsuarioRegistrar);
        txtBarrio = findViewById(R.id.txtLugarRegistrarUsuario);

        imgUsuario = findViewById(R.id.imgRegistrarUsuario);


    }
}