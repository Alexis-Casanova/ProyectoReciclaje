package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Adapters.AdapterPublicacion;
import Models.Publicacion;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadInicioUsuario extends AppCompatActivity {

    ListView lv_postUsuario;
    TextView txt_tituloUsuario;
    ImageButton btn_editarUsuario, btn_detalleUsuario, btn_agregarPostUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_inicio_usuario);

        lv_postUsuario = findViewById(R.id.lv_usuario_post);
        txt_tituloUsuario = findViewById(R.id.txt_titulo_usuario);
        btn_agregarPostUsuario = findViewById(R.id.btn_agregar_postUsuario);
        btn_detalleUsuario = findViewById(R.id.btn_detalleUsuario);
        btn_editarUsuario = findViewById(R.id.btn_editar_usuario);

        btn_detalleUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, DetalleUsuario.class);
            startActivity(oIntento);
        });

        btn_agregarPostUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, RegistrarPublicacion.class);
            startActivity(oIntento);
        });

        btn_editarUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, EditarUsuario.class);
            startActivity(oIntento);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPublicaciones();
    }

    private void mostrarPublicaciones() {
        ApiServicioReciclaje ApiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class); //Creamos el servicio

        Call<List<Publicacion>> oCall = ApiServicio.GetPublicaciones();
        oCall.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<Publicacion> listaPublicaciones = response.body();
                    AdapterPublicacion adapter = new AdapterPublicacion(ActividadInicioUsuario.this, listaPublicaciones);
                    lv_postUsuario.setAdapter(adapter);
                } else{
                    Toast.makeText(ActividadInicioUsuario.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Toast.makeText(ActividadInicioUsuario.this, "Error API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}