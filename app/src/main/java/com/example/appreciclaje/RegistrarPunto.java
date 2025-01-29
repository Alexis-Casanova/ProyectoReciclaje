package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Models.Punto;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarPunto extends AppCompatActivity {
    EditText etDireccionPunto, etReferenciaPunto, etNombrePunto;
    Button btnCrearPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_punto);

        etDireccionPunto = findViewById(R.id.etDireccionUbi);
        etReferenciaPunto = findViewById(R.id.etReferenciaUbi);
        etNombrePunto = findViewById(R.id.etNombreUbi);
        btnCrearPunto = findViewById(R.id.btnCrearUbicacion);

        btnCrearPunto.setOnClickListener(v -> registrarPunto());
    }

    private void registrarPunto() {
        String direccion = etDireccionPunto.getText().toString();
        String referencia = etReferenciaPunto.getText().toString();
        String nombre = etNombrePunto.getText().toString();

        if (direccion.isEmpty() || referencia.isEmpty() || nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Punto punto = new Punto(0, nombre, direccion, referencia);

        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        Call<Punto> call = apiServicio.PostPuntos(punto);
        call.enqueue(new Callback<Punto>() {
            @Override
            public void onResponse(Call<Punto> call, Response<Punto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrarPunto.this, "Ubicación registrada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarPunto.this, "Error al registrar la ubicación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Punto> call, Throwable t) {
                Toast.makeText(RegistrarPunto.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}