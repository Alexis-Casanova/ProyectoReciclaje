package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleUsuario extends AppCompatActivity {
    TextView txtNombre, txtBarrio, txtEmail, txtDNI, txtRecompensas;
    ImageView imgUsuario;
    ImageButton btn_canjearUsuario, btn_editarUsuario, btn_eliminarUsuario;
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
        btn_canjearUsuario = findViewById(R.id.btn_CanjearUsuario);
        btn_editarUsuario = findViewById(R.id.btn_EditarUsuarioDetalle);
        btn_eliminarUsuario = findViewById(R.id.btn_eliminarUsuario);

        btn_editarUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(DetalleUsuario.this, EditarUsuario.class);
            startActivity(oIntento);
        });

        btn_canjearUsuario.setOnClickListener(v -> {
            Intent oInento = new Intent(DetalleUsuario.this, ActividadCanjearRecompensas.class);
            startActivity(oInento);
            Toast.makeText(this, "Recompensas canjeadas: " + usuarioLogeado.getRecompensa() + " puntos", Toast.LENGTH_SHORT).show();
        });

        obtenerUsuarioDesdeServidor();

        btn_eliminarUsuario.setOnClickListener(v -> {
            if (usuarioLogeado != null) {
                eliminarUsuario(usuarioLogeado.getIdUsuario());
            } else {
                Toast.makeText(this, "No se encontró el usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarUsuario(int id) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de que desea eliminar este usuario?")
                .setPositiveButton("Sí", (dialogInterface, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    apiServicio.DeleteUsuarios(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(DetalleUsuario.this, "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetalleUsuario.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DetalleUsuario.this, "Error al eliminar el usuario.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(DetalleUsuario.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerUsuarioDesdeServidor();
    }

    private void obtenerUsuarioDesdeServidor() {
        SessionManager sessionManager = new SessionManager(this);
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

        Call<Usuario> call = apiServicio.obtenerUsuarioPorId(sessionManager.getUsuarioDetalles().getIdUsuario());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    usuarioLogeado = response.body();
                    sessionManager.crearLoginSession(usuarioLogeado);
                    cargarDatosUsuario();
                } else {
                    Toast.makeText(DetalleUsuario.this, "Error al obtener datos del usuario.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(DetalleUsuario.this, "Error al conectar con el servidor.", Toast.LENGTH_SHORT).show();
            }
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
            imgUsuario.setImageResource(R.drawable.placeholder_image);
        }
    }
}
