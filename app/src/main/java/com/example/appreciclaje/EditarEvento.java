package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import Models.Evento;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarEvento extends AppCompatActivity {
    EditText et_TituloEventoEdit, et_ContenidoEdit, et_fechaEdit, et_tipoEdit;
    Spinner sp_LugarEdit;
    Button btn_editarEventoEdit;
    int idEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_editar_evento);

        et_TituloEventoEdit = findViewById(R.id.et_TituloEventoEdit);
        et_ContenidoEdit = findViewById(R.id.et_ContenidoEdit);
        et_fechaEdit = findViewById(R.id.et_fechaEdit);
        et_tipoEdit = findViewById(R.id.et_tipoEdit);
        sp_LugarEdit = findViewById(R.id.sp_LugarEdit);
        btn_editarEventoEdit = findViewById(R.id.btn_editarEventoEdit);

        configurarSpinners();

        idEvento = getIntent().getIntExtra("id", -1);
        if (idEvento != -1) {
            cargarDatosEvento(idEvento);
        }

        btn_editarEventoEdit.setOnClickListener(v -> actualizarEvento());
    }

    private void cargarDatosEvento(int idEvento) {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        apiServicio.getEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Evento evento : response.body()) {
                        if (evento.getId() == idEvento) {
                            et_TituloEventoEdit.setText(evento.getTitulo());
                            et_ContenidoEdit.setText(evento.getDescripcion());
                            et_fechaEdit.setText(evento.getFecha());
                            et_tipoEdit.setText(evento.getOrganizador());
                            sp_LugarEdit.setSelection(((ArrayAdapter<String>) sp_LugarEdit.getAdapter()).getPosition(evento.getLugar()));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(EditarEvento.this, "Error al cargar datos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarEvento() {
        String titulo = et_TituloEventoEdit.getText().toString().trim();
        String contenido = et_ContenidoEdit.getText().toString().trim();
        String fecha = et_fechaEdit.getText().toString().trim();
        String organizador = et_tipoEdit.getText().toString().trim();
        String lugar = sp_LugarEdit.getSelectedItem().toString().toLowerCase();

        if (titulo.isEmpty() || contenido.isEmpty() || fecha.isEmpty() || organizador.isEmpty() || lugar.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Evento eventoActualizado = new Evento(idEvento, titulo, contenido, lugar, fecha, organizador);

        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
        apiServicio.PutEventos(idEvento, eventoActualizado).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarEvento.this, "Evento actualizado correctamente.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarEvento.this, ActividadCalendario.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditarEvento.this, "Error al actualizar el evento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                Toast.makeText(EditarEvento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configurarSpinners() {
        String[] tipos = {"San Pedro", "San Sebastían", "El Cumbe", "San Martín"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_LugarEdit.setAdapter(adapterTipo);
    }
}
