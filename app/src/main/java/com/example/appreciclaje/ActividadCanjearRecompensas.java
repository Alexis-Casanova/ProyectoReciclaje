package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadCanjearRecompensas extends AppCompatActivity {
    TextView txtRecompensa;
    ImageButton btnCanjearBolsa, btnCanjearCamiseta, btnCanjearGorra, btnCanjearCupon;
    private Usuario usuarioLogeado;
    int saldoPuntos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_canjear_recompensas);
        txtRecompensa = findViewById(R.id.txt_puntosRecompensas);
        btnCanjearBolsa = findViewById(R.id.btn_canjearBolsa);
        btnCanjearCamiseta = findViewById(R.id.btn_canjearCamiseta);
        btnCanjearGorra = findViewById(R.id.btn_canjearGorra);
        btnCanjearCupon = findViewById(R.id.btn_canjearCupon);

        SessionManager sessionManager = new SessionManager(this);
        usuarioLogeado = sessionManager.getUsuarioDetalles();

        if (usuarioLogeado == null) {
            Toast.makeText(this, "Debe iniciar sesión para editar el perfil.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        saldoPuntos = usuarioLogeado.getRecompensa();
        txtRecompensa.setText(saldoPuntos+" PTS");

        btnCanjearBolsa.setOnClickListener(v -> confirmarCambioRecompensa(1000, "Bolsa reutilizable"));
        btnCanjearCamiseta.setOnClickListener(v -> confirmarCambioRecompensa(20000, "Camiseta"));
        btnCanjearGorra.setOnClickListener(v -> confirmarCambioRecompensa(10000, "Gorra"));
        btnCanjearCupon.setOnClickListener(v -> confirmarCambioRecompensa(15000, "Cupón de descuento"));
    }

    private void confirmarCambioRecompensa(int costo, String recompensa) {
        String mensaje = "¿Estás seguro de canjear "+ recompensa+" por "+costo+" puntos?";
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage(mensaje)
                .setPositiveButton("Sí", (d, which) -> canjearRecompensa(costo, recompensa))
                .setNegativeButton("No", null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    private void canjearRecompensa(int costo, String recompensa) {
        if (saldoPuntos >= costo) {
            saldoPuntos -= costo;
            actualizarSaldo();
            Toast.makeText(this, "Recompensa canjeada: " + recompensa, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No tienes suficientes puntos para canjear " + recompensa, Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarSaldo() {
        txtRecompensa.setText(String.valueOf(saldoPuntos));
        usuarioLogeado.setRecompensa(saldoPuntos);

        ApiServicioReciclaje api = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        Call<Usuario> call = api.actualizarUsuario(usuarioLogeado.getIdUsuario(), usuarioLogeado);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadCanjearRecompensas.this, "Recompensa actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActividadCanjearRecompensas.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(ActividadCanjearRecompensas.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}