package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrarUsuario extends AppCompatActivity {
    EditText txtNomRegistro, txtApeRegistro,txtNaciRegistro, txtEmailRegistro,txtDNIRegistro, txtContraRegistro;
    Button btnRegistro;
    ImageView imgUsuarioRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        txtNomRegistro = findViewById(R.id.txtNomRegistro);
        txtApeRegistro = findViewById(R.id.txtApeRegistro);
        txtNaciRegistro = findViewById(R.id.txtNaciRegistro);
        txtEmailRegistro = findViewById(R.id.txtEmailRegistro);
        txtDNIRegistro = findViewById(R.id.txtDNIRegistro);
        txtContraRegistro = findViewById(R.id.txtContraRegistro);
        btnRegistro = findViewById(R.id.btnRegistro);
        imgUsuarioRegistro = findViewById(R.id.imgUsuarioRegistro);



        btnRegistro.setOnClickListener(v -> {
            RegistroUsuario();
        });
    }

    private void RegistroUsuario() {
    }
}