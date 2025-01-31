package com.example.appreciclaje;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import ViewModels.RespuestaObtenida;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarUsuario extends AppCompatActivity {
    EditText txtNombre, txtLugar, txtDni;
    ImageView imgUsuario;
    ImageButton btnActualizar;
    private String rutaImagenSeleccionada;
    private Usuario usuarioLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_editar_usuario);

        txtNombre = findViewById(R.id.txtNombreEditarUsuario);
        txtLugar = findViewById(R.id.txtLugarEditarUsuario);
        txtDni = findViewById(R.id.txtDNIEditarUsuario);
        imgUsuario = findViewById(R.id.imgEditarUsuario);
        btnActualizar = findViewById(R.id.btnEditarUsuario);

        SessionManager sessionManager = new SessionManager(this);
        usuarioLogeado = sessionManager.getUsuarioDetalles();

        if (usuarioLogeado == null) {
            Toast.makeText(this, "Debe iniciar sesión para editar el perfil.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosUsuario();

        imgUsuario.setOnClickListener(v -> seleccionarImagen());
        btnActualizar.setOnClickListener(v -> obtenerRutaExterna());
    }

    private void cargarDatosUsuario() {
        txtNombre.setText(usuarioLogeado.getNombre());
        txtLugar.setText(usuarioLogeado.getLugar());
        txtDni.setText(usuarioLogeado.getDni());

        if (usuarioLogeado.getRutaUsuario() != null) {
            Glide.with(this)
                    .load(usuarioLogeado.getRutaUsuario())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imgUsuario);
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityIfNeeded(Intent.createChooser(intent, "Seleccionar Imagen"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uriImagen = data.getData();
            imgUsuario.setImageURI(uriImagen);
            rutaImagenSeleccionada = obtenerRutaNativa(uriImagen);
        }
    }

    private String obtenerRutaNativa(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String ruta = cursor.getString(columnIndex);
            cursor.close();
            return ruta;
        }
        return null;
    }

    private void obtenerRutaExterna() {
        int usuarioId = usuarioLogeado.getIdUsuario();
        String nombre = txtNombre.getText().toString();
        String lugar = txtLugar.getText().toString();
        String dni = txtDni.getText().toString();

        if (nombre.isEmpty() || lugar.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuarioActualizado = new Usuario(
                usuarioId, nombre, usuarioLogeado.getEmail(), dni, lugar,
                usuarioLogeado.getRol(), usuarioLogeado.getRecompensa(), usuarioLogeado.getRutaUsuario(), usuarioLogeado.getContrasena()
        );

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar actualización")
                .setMessage("¿Estás seguro de que deseas actualizar este usuario?")
                .setPositiveButton("Sí", (dialogInterface, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

                    if (rutaImagenSeleccionada != null) {
                        File archivoImagen = new File(rutaImagenSeleccionada);
                        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData(
                                "file", archivoImagen.getName(), RequestBody.create(MediaType.parse("image/*"), archivoImagen)
                        );

                        Call<RespuestaObtenida> callImagen = apiServicio.respuestaImagen(multipartBody);
                        callImagen.enqueue(new Callback<RespuestaObtenida>() {
                            @Override
                            public void onResponse(Call<RespuestaObtenida> call, Response<RespuestaObtenida> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    usuarioActualizado.setRutaUsuario(response.body().getUrl());
                                    actualizarUsuarioEnServidor(apiServicio, usuarioActualizado);
                                } else {
                                    Toast.makeText(EditarUsuario.this, "Error al subir la imagen.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaObtenida> call, Throwable t) {
                                Toast.makeText(EditarUsuario.this, "Error al subir la imagen: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        actualizarUsuarioEnServidor(apiServicio, usuarioActualizado);
                    }
                })
                .setNegativeButton("Cancelar", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    private void actualizarUsuarioEnServidor(ApiServicioReciclaje apiServicio, Usuario usuario) {
        Call<Usuario> callActualizar = apiServicio.actualizarUsuario(usuario.getIdUsuario(), usuario);
        callActualizar.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarUsuario.this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditarUsuario.this, DetalleUsuario.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditarUsuario.this, "Error al actualizar usuario. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(EditarUsuario.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}