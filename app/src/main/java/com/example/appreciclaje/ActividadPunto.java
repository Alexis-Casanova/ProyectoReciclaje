package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Adapters.AdapterPunto;
import Models.Punto;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadPunto extends AppCompatActivity {

    ListView lv_Puntos;
    Button btn_NuevoPunto, btn_EliminarPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_punto);

        lv_Puntos = findViewById(R.id.lv_Punto);
        btn_NuevoPunto = findViewById(R.id.btn_NuevoPunto);
        btn_EliminarPunto = findViewById(R.id.btn_EliminarPunto);

        btn_NuevoPunto.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadPunto.this, RegistrarPunto.class);
            startActivity(oIntento);
        });

        mostrarPuntos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPuntos();
    }

    private void mostrarPuntos() {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        Call<List<Punto>> call = apiServicio.getPuntos();
        call.enqueue(new Callback<List<Punto>>() {
            @Override
            public void onResponse(Call<List<Punto>> call, Response<List<Punto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Punto> listaPuntos = response.body();
                    AdapterPunto adapter = new AdapterPunto(ActividadPunto.this, listaPuntos);
                    lv_Puntos.setAdapter(adapter);
                } else {
                    Toast.makeText(ActividadPunto.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Punto>> call, Throwable t) {
                Toast.makeText(ActividadPunto.this, "Error API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}