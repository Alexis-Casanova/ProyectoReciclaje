package com.example.appreciclaje;

import static com.example.appreciclaje.ActividadInicioAdmin.publicacionObtenida;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadDetalleRecompensa extends AppCompatActivity {
    TextView txtnombreUsuarioP, txtFechaUsuarioP, txtDescripcionUsuarioP, txtNombreUsuarioRecompensa, txtRecompensa;
    ImageView imgPerfilUsuario, imgPDescripcionUsuario, imgUsuarioRecompensa;
    Button btnAumentarR, btnDisminuirR;
    Usuario usuarioSeleccionado;
    int recompensaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_detalle_recompensa);

        txtNombreUsuarioRecompensa = findViewById(R.id.txtNombreUsuarioDetalleRecompensa);
        txtDescripcionUsuarioP = findViewById(R.id.txtDescripcionUsuarioP);
        txtRecompensa = findViewById(R.id.txtRecompensaUsuario);
        txtnombreUsuarioP = findViewById(R.id.txtNombreUsuarioP);
        txtFechaUsuarioP = findViewById(R.id.txtFechaUsuarioP);
        imgPerfilUsuario = findViewById(R.id.imgFPerfilUsuario);
        imgPDescripcionUsuario = findViewById(R.id.imgPDescripcionUsuario);
        imgUsuarioRecompensa = findViewById(R.id.imgImagenUsuarioRecompensa);
        btnAumentarR = findViewById(R.id.btn_aumentarRecom);
        btnDisminuirR = findViewById(R.id.btn_disminuirRecom);

        usuarioSeleccionado = publicacionObtenida.getUsuario();
        recompensaActual = usuarioSeleccionado.getRecompensa();

        txtnombreUsuarioP.setText(usuarioSeleccionado.getNombre());
        txtDescripcionUsuarioP.setText(publicacionObtenida.getContenido());
        txtFechaUsuarioP.setText(publicacionObtenida.getFecha());
        txtNombreUsuarioRecompensa.setText(usuarioSeleccionado.getNombre());
        txtRecompensa.setText(String.valueOf(recompensaActual));


        // Cargar imágenes con Glide
        if (usuarioSeleccionado.getRutaUsuario() != null) {
            Glide.with(this)
                    .load(usuarioSeleccionado.getRutaUsuario())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgPerfilUsuario);
        }

        if (usuarioSeleccionado.getRutaUsuario() != null) {
            Glide.with(this)
                    .load(usuarioSeleccionado.getRutaUsuario())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgUsuarioRecompensa);
        }

        if (publicacionObtenida.getRutaPublicacion() != null) {
            Glide.with(this)
                    .load(publicacionObtenida.getRutaPublicacion())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgPDescripcionUsuario);
        }

        btnAumentarR.setOnClickListener(v -> confirmarCambioRecompensa(100));
        btnDisminuirR.setOnClickListener(v -> confirmarCambioRecompensa(-100));
    }

    private void confirmarCambioRecompensa(int cambio) {
        String mensaje = cambio > 0 ? "¿Estás seguro de aumentar 100 puntos?" : "¿Estás seguro de disminuir 100 puntos?";
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage(mensaje)
                .setPositiveButton("Sí", (d, which) -> actualizarRecompensa(cambio))
                .setNegativeButton("No", null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    private void actualizarRecompensa(int cambio) {
        recompensaActual += cambio;
        if (recompensaActual < 0) {
            recompensaActual = 0;
        }
        txtRecompensa.setText(String.valueOf(recompensaActual));

        // Crear objeto usuario actualizado
        usuarioSeleccionado.setRecompensa(recompensaActual);

        // Hacer PUT a la API
        ApiServicioReciclaje api = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        Call<Usuario> call = api.actualizarUsuario(usuarioSeleccionado.getIdUsuario(), usuarioSeleccionado);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadDetalleRecompensa.this, "Recompensa actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActividadDetalleRecompensa.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(ActividadDetalleRecompensa.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}