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

import java.io.File;

import ViewModels.RespuestaObtenida;
import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarUsuario extends AppCompatActivity {
    EditText txtNomRegistro, txtLugarRegistro, txtEmailRegistro, txtDNIRegistro, txtContraRegistro;
    ImageButton btnRegistro;
    ImageView imgUsuarioRegistro;
    private String rutaImagenSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        txtNomRegistro = findViewById(R.id.txtNombreUsuarioRegistrar);
        txtLugarRegistro = findViewById(R.id.txtLugarRegistrarUsuario);
        txtEmailRegistro = findViewById(R.id.txtEmailRegistrarUsuario);
        txtDNIRegistro = findViewById(R.id.txtDNIRegistrarUsuario);
        txtContraRegistro = findViewById(R.id.txtContraRegistrarUsuario);
        btnRegistro = findViewById(R.id.btnRegistrarUsuario);
        imgUsuarioRegistro = findViewById(R.id.imgRegistrarUsuario);

        imgUsuarioRegistro.setOnClickListener(v -> seleccionarImagen());
        btnRegistro.setOnClickListener(v -> obtenerRutaExterna());
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
            imgUsuarioRegistro.setImageURI(uriImagen);
            rutaImagenSeleccionada = obtenerRutaNativa(uriImagen);
        }
    }

    private String obtenerRutaNativa(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int indiceRuta = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String ruta = cursor.getString(indiceRuta);
            cursor.close();
            return ruta;
        }
        return null;
    }

    private void obtenerRutaExterna() {
        if (rutaImagenSeleccionada == null) {
            Toast.makeText(this, "Selecciona una imagen para continuar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Subir imagen al servidor
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        File archivoImagen = new File(rutaImagenSeleccionada);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData(
                "file", archivoImagen.getName(), RequestBody.create(MediaType.parse("image/*"), archivoImagen)
        );

        Call<RespuestaObtenida> call = apiServicio.respuestaImagen(multipartBody);
        call.enqueue(new Callback<RespuestaObtenida>() {
            @Override
            public void onResponse(Call<RespuestaObtenida> call, Response<RespuestaObtenida> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String rutaImagenSubida = response.body().getUrl();
                    registrarUsuario(rutaImagenSubida);
                } else {
                    Toast.makeText(RegistrarUsuario.this, "Error al subir la imagen. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaObtenida> call, Throwable t) {
                Toast.makeText(RegistrarUsuario.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarUsuario(String rutaImagenSubida) {
        String nombre = txtNomRegistro.getText().toString();
        String lugar = txtLugarRegistro.getText().toString();
        String email = txtEmailRegistro.getText().toString();
        String dni = txtDNIRegistro.getText().toString();
        String contrasena = txtContraRegistro.getText().toString();

        if (nombre.isEmpty() || lugar.isEmpty() || email.isEmpty() || dni.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(0, nombre, email, dni, lugar, contrasena, "usuario", 0, rutaImagenSubida);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar registro")
                .setMessage("¿Estás seguro de que deseas registrar este usuario?")
                .setPositiveButton("Sí", (dialogInterface, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    Call<Usuario> call = apiServicio.PostUsuarios(usuario);
                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegistrarUsuario.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrarUsuario.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(RegistrarUsuario.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }
}