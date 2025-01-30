package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Models.Punto;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPunto extends AppCompatActivity {
    EditText et_NombrePuntoEdit, et_DireccionPuntoEdit, et_ReferenciaPuntoEdit;
    ImageButton btn_editarPuntoEdit;
    int idPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_punto);

        et_NombrePuntoEdit = findViewById(R.id.et_NombrePuntoEdit);
        et_DireccionPuntoEdit = findViewById(R.id.et_DireccionPuntoEdit);
        et_ReferenciaPuntoEdit = findViewById(R.id.et_ReferenciaPuntoEdit);
        btn_editarPuntoEdit = findViewById(R.id.btn_editarPuntoEdit);

        idPunto = getIntent().getIntExtra("id", -1);
        if (idPunto != -1) {
            cargarDatosPunto(idPunto);
        }

        btn_editarPuntoEdit.setOnClickListener(v -> actualizarPunto());
    }

    private void cargarDatosPunto(int idPunto) {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        apiServicio.getPuntos().enqueue(new Callback<List<Punto>>() {
            @Override
            public void onResponse(Call<List<Punto>> call, Response<List<Punto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Punto punto : response.body()) {
                        if (punto.getIdPunto() == idPunto) {
                            et_NombrePuntoEdit.setText(punto.getNombre());
                            et_DireccionPuntoEdit.setText(punto.getDireccion());
                            et_ReferenciaPuntoEdit.setText(punto.getReferencia());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Punto>> call, Throwable t) {
                Toast.makeText(EditarPunto.this, "Error al cargar datos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarPunto() {
        String nombre = et_NombrePuntoEdit.getText().toString().trim();
        String direccion = et_DireccionPuntoEdit.getText().toString().trim();
        String referencia = et_ReferenciaPuntoEdit.getText().toString().trim();

        if (nombre.isEmpty() || direccion.isEmpty() || referencia.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar actualización")
                .setMessage("¿Estás seguro de que deseas actualizar este punto?")
                .setPositiveButton("Sí", (dialogInterface, which) -> {
                    Punto puntoActualizado = new Punto(idPunto, nombre, direccion, referencia);

                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    apiServicio.PutPuntos(idPunto, puntoActualizado).enqueue(new Callback<Punto>() {
                        @Override
                        public void onResponse(Call<Punto> call, Response<Punto> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(EditarPunto.this, "Punto actualizado correctamente.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditarPunto.this, ActividadPunto.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditarPunto.this, "Error al actualizar el punto.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Punto> call, Throwable t) {
                            Toast.makeText(EditarPunto.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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