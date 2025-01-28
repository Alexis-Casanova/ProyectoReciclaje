package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import Models.Usuario;
import Sesiones.SessionManager;

public class DetalleUsuario extends AppCompatActivity {
    TextView txtNombre, txtBarrio, txtEmail, txtDNI, txtRecompensas;
    ImageView imgUsuario;
    Button btn_canjearUsuario, btn_editarUsuario;
    private Usuario usuarioLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);

        txtNombre = findViewById(R.id.txtNombreDetalleUsuario);
        txtBarrio = findViewById(R.id.txtLugarDetalleUsuario);
        txtEmail = findViewById(R.id.txtEmailDetalleUsuario);
        txtDNI = findViewById(R.id.txtDNIDetalleUsuario);
        txtRecompensas = findViewById(R.id.txtRecompensaDetalleUsuario);
        imgUsuario = findViewById(R.id.imgDetalleUsuario);
        btn_canjearUsuario = findViewById(R.id.btnCanjearUsuario);
        btn_editarUsuario = findViewById(R.id.btnEditarUsuarioDetalle);

        SessionManager sessionManager = new SessionManager(this);
        usuarioLogeado = sessionManager.getUsuarioDetalles();

        if (usuarioLogeado == null) {
            Toast.makeText(this, "Debe iniciar sesión para ver los detalles del usuario.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosUsuario();

        btn_editarUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(DetalleUsuario.this, EditarUsuario.class);
            startActivity(oIntento);
        });

        btn_canjearUsuario.setOnClickListener(v -> {
            Toast.makeText(this, "Recompensas canjeadas con éxito: " + usuarioLogeado.getRecompensa() + " puntos", Toast.LENGTH_SHORT).show();
            // Aquí puedes implementar la lógica para reducir los puntos del usuario en la base de datos
        });

    }

    private void cargarDatosUsuario() {
        txtNombre.setText(usuarioLogeado.getNombre());
        txtBarrio.setText(usuarioLogeado.getLugar());
        txtEmail.setText(usuarioLogeado.getEmail());
        txtDNI.setText(usuarioLogeado.getDni());
        txtRecompensas.setText(String.valueOf(usuarioLogeado.getRecompensa()));

        if (usuarioLogeado.getRutaUsuario() != null) {
            Glide.with(this)
                    .load(usuarioLogeado.getRutaUsuario())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgUsuario);
        } else {
            imgUsuario.setImageResource(R.drawable.placeholder_image); // Imagen predeterminada si no hay URL
        }
    }
}