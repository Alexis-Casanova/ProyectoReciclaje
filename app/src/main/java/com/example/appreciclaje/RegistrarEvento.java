package com.example.appreciclaje;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Models.Evento;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarEvento extends AppCompatActivity {
    EditText et_Contenido, et_fecha, et_tipo, et_TituloEvento;
    Spinner sp_LugarEvento;
    ImageButton btn_crearEvento;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_registrar_evento);

        et_Contenido = findViewById(R.id.et_Contenido);
        et_fecha = findViewById(R.id.et_fecha);
        et_tipo = findViewById(R.id.et_tipo);
        et_TituloEvento = findViewById(R.id.et_TituloEvento);
        sp_LugarEvento = findViewById(R.id.sp_Lugar);
        btn_crearEvento = findViewById(R.id.btn_crearEvento);

        configurarSpinners();
        btn_crearEvento.setOnClickListener(v->{
            registrarEvento();
        });
    }

    private void registrarEvento() {
        String titulo = et_TituloEvento.getText().toString().trim();
        String contenido = et_Contenido.getText().toString().trim();
        String fecha = et_fecha.getText().toString().trim();
        String organizador = et_tipo.getText().toString().trim();
        String lugar = sp_LugarEvento.getSelectedItem().toString();

        if (titulo.isEmpty() || fecha.isEmpty() || organizador.isEmpty() || lugar.isEmpty() || contenido.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Evento nuevoEvento = new Evento(0, titulo, contenido, lugar, fecha, organizador);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirmar registro")
                .setMessage("¿Estás seguro de que deseas registrar este evento?")
                .setPositiveButton("Sí", (dialogInterface, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    apiServicio.PostEventos(nuevoEvento).enqueue(new Callback<Evento>() {
                        @Override
                        public void onResponse(Call<Evento> call, Response<Evento> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegistrarEvento.this, "Evento registrado exitosamente.", Toast.LENGTH_SHORT).show();
                                limpiarFormulario();
                                Intent oIntento = new Intent(RegistrarEvento.this, ActividadCalendarioAdmin.class);
                                oIntento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(oIntento);
                            } else {
                                Toast.makeText(RegistrarEvento.this, "Error al registrar el evento.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Evento> call, Throwable t) {
                            Toast.makeText(RegistrarEvento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
    }

    private void limpiarFormulario() {
        et_Contenido.setText("");
        et_fecha.setText("");
        et_tipo.setText("");
        sp_LugarEvento.setSelection(0);
    }

    private void configurarSpinners() {
        String[] tipos = {"San Pedro", "San Sebastían", "El Cumbe", "San Martín"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_LugarEvento.setAdapter(adapterTipo);
    }
}