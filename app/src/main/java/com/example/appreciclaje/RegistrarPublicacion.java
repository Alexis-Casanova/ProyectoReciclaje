package com.example.appreciclaje;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import Models.Publicacion;
import Models.Usuario;
import Sesiones.SessionManager;
import ViewModels.RespuestaObtenida;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarPublicacion extends AppCompatActivity {
    Spinner spTipoPost, spLugarPost;
    EditText etDescripcionPost;
    ImageView imgPost;
    ImageButton btnPostear;
    private String rutaImagenSeleccionada;
    private Usuario usuarioLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_registrar_publicacion);

        spTipoPost = findViewById(R.id.sp_tipo_post);
        spLugarPost = findViewById(R.id.sp_lugar_post);
        etDescripcionPost = findViewById(R.id.et_descripcion_post);
        imgPost = findViewById(R.id.img_post);
        btnPostear = findViewById(R.id.btn_postear);

        configurarSpinners();

        SessionManager sessionManager = new SessionManager(this);
        usuarioLogeado = sessionManager.getUsuarioDetalles();

        if (usuarioLogeado == null) {
            Toast.makeText(this, "Debe iniciar sesión para publicar.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        imgPost.setOnClickListener(v -> seleccionarImagen());
        btnPostear.setOnClickListener(v -> obtenerRutaExterna());
    }

    private void configurarSpinners() {
        String[] tipos = {"Consejo", "Reciclar"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoPost.setAdapter(adapterTipo);

        String[] lugares = {"San Pedro", "San Sebastían", "El Cumbe", "San Martín"};
        ArrayAdapter<String> adapterLugar = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lugares);
        adapterLugar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLugarPost.setAdapter(adapterLugar);
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
            imgPost.setImageURI(uriImagen);
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
                    registrarPublicacion(rutaImagenSubida);
                } else {
                    Toast.makeText(RegistrarPublicacion.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaObtenida> call, Throwable t) {
                Toast.makeText(RegistrarPublicacion.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarPublicacion(String rutaImagenSubida) {
        String tipoPost = spTipoPost.getSelectedItem().toString().toLowerCase();
        String lugarPost = spLugarPost.getSelectedItem().toString().toLowerCase();
        String descripcionPost = etDescripcionPost.getText().toString();

        int usuarioId = usuarioLogeado.getIdUsuario();
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        Publicacion publicacion = new Publicacion(
                0,
                descripcionPost,
                lugarPost,
                fechaActual,
                tipoPost,
                rutaImagenSubida,
                usuarioId,
                null
        );

        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        Call<Publicacion> call = apiServicio.PostPublicaciones(publicacion);
        call.enqueue(new Callback<Publicacion>() {
            @Override
            public void onResponse(Call<Publicacion> call, Response<Publicacion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrarPublicacion.this, "Publicación realizada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarPublicacion.this, "Error al publicar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Publicacion> call, Throwable t) {
                Toast.makeText(RegistrarPublicacion.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
