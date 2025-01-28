package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Adapters.AdapterEvento;
import Models.Evento;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadCalendario extends AppCompatActivity {
    Spinner sp_zonaEvento;
    ListView lv_evento;
    Button btn_filtrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_actividad_calendario);

        sp_zonaEvento = findViewById(R.id.sp_zonaEvento);
        lv_evento = findViewById(R.id.lv_evento);
        btn_filtrar = findViewById(R.id.btn_filtrar);

        configurarSpinners();
        mostrarEventos();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mostrarEventos();
    }

    private void configurarSpinners() {
        String[] tipos = {"San Pedro", "San Sebastían", "El Cumbe", "San Martín"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zonaEvento.setAdapter(adapterTipo);
    }

    private void mostrarEventos() {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

        Call<List<Evento>> call = apiServicio.getEventos();
        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> listaEventos = response.body();

                    AdapterEvento adapter = new AdapterEvento(ActividadCalendario.this, listaEventos);
                    ListView lvEventos = findViewById(R.id.lv_evento);
                    lvEventos.setAdapter(adapter);
                } else {
                    Toast.makeText(ActividadCalendario.this, "No se pudo conectar con la API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(ActividadCalendario.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}