package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

import Adapters.AdapterEvento;
import Models.Evento;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadCalendarioAdmin extends AppCompatActivity {

    Spinner sp_zonaEvento;
    ListView lv_evento;
    Button btn_filtrar;

    List<Evento> listaEventos = new ArrayList<>();

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

        lv_evento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evento evento = listaEventos.get(position);
                Intent oIntento = new Intent(ActividadCalendarioAdmin.this, ActividadDetalleEvento.class);
                oIntento.putExtra("titulo", evento.getTitulo());
                oIntento.putExtra("id", evento.getId());
                oIntento.putExtra("descripcion", evento.getDescripcion());
                oIntento.putExtra("fecha", evento.getFecha());
                oIntento.putExtra("lugar", evento.getLugar());
                oIntento.putExtra("organizador", evento.getOrganizador());
                startActivity(oIntento);
            }
        });

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
                    listaEventos = response.body();

                    AdapterEvento adapter = new AdapterEvento(ActividadCalendarioAdmin.this, listaEventos);
                    ListView lvEventos = findViewById(R.id.lv_evento);
                    lvEventos.setAdapter(adapter);
                } else {
                    Toast.makeText(ActividadCalendarioAdmin.this, "No se pudo conectar con la API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(ActividadCalendarioAdmin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}