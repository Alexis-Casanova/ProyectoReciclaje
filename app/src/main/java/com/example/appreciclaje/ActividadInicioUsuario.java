package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.stream.Collectors;

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
    ImageButton btn_buscarUsuario, btn_limpiarBuscarUsuario;
    Spinner sp_barrioUsuario;
    private String buscarTipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_inicio_usuario);

        lv_postUsuario = findViewById(R.id.lv_usuario_post);
        txt_tituloUsuario = findViewById(R.id.txt_titulo_usuario);
        btn_agregarPostUsuario = findViewById(R.id.btn_agregar_postUsuario);
        btn_detalleUsuario = findViewById(R.id.btn_detalleUsuario);
        btn_buscarUsuario = findViewById(R.id.btn_buscarUsuario);
        btn_limpiarBuscarUsuario = findViewById(R.id.btn_limpiarBuscarUsuario);
        sp_barrioUsuario = findViewById(R.id.sp_usuario_barrio);

        configurarSpinners();

        btn_detalleUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, DetalleUsuario.class);
            startActivity(oIntento);
        });

        btn_agregarPostUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, RegistrarPublicacion.class);
            startActivity(oIntento);
        });

        btn_buscarUsuario.setOnClickListener(v -> {
            buscarTipo = sp_barrioUsuario.getSelectedItem().toString();
            mostrarPublicaciones();
        });

        btn_limpiarBuscarUsuario.setOnClickListener(v -> {
            buscarTipo = "";
            sp_barrioUsuario.setSelection(0);
            mostrarPublicaciones();
        });

        mostrarPublicaciones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPublicaciones();
    }

    private void configurarSpinners() {
        String[] barrios = {"Consejo", "Reciclar"};
        ArrayAdapter<String> adapterBarrio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barrios);
        adapterBarrio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_barrioUsuario.setAdapter(adapterBarrio);
    }

    private void mostrarPublicaciones() {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

        Call<List<Publicacion>> oCall = apiServicio.GetPublicaciones();
        oCall.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Publicacion> listaPublicaciones = response.body();

                    if (!buscarTipo.isEmpty()) {
                        listaPublicaciones = listaPublicaciones.stream()
                                .filter(p -> p.getTipo().equalsIgnoreCase(buscarTipo))
                                .collect(Collectors.toList());
                    }

                    AdapterPublicacion adapter = new AdapterPublicacion(ActividadInicioUsuario.this, listaPublicaciones);
                    lv_postUsuario.setAdapter(adapter);
                } else {
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
