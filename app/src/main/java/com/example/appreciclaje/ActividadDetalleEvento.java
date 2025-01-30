package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadDetalleEvento extends AppCompatActivity {
    TextView lb_tituloEventoDet, lb_descripEvento, lb_horarioEventoDet, lb_lugarEventoDet, lb_organizadorEvento;
    ImageButton btn_editarEvento, btn_eliminarEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_detalle_evento);

        lb_tituloEventoDet = findViewById(R.id.lb_tituloEventoDet);
        lb_descripEvento = findViewById(R.id.lb_descripEvento);
        lb_horarioEventoDet = findViewById(R.id.lb_horarioEventoDet);
        lb_lugarEventoDet = findViewById(R.id.lb_lugarEventoDet);
        lb_organizadorEvento = findViewById(R.id.lb_organizadorEvento);
        btn_editarEvento = findViewById(R.id.btnCrearUbicacion);
        btn_eliminarEvento = findViewById(R.id.btn_eliminarEvento);

        Bundle extras = getIntent().getExtras();

        assert extras != null;
        lb_tituloEventoDet.setText(extras.getString("titulo"));
        lb_descripEvento.setText(extras.getString("descripcion"));
        lb_horarioEventoDet.setText(extras.getString("fecha"));
        lb_lugarEventoDet.setText(extras.getString("lugar"));
        lb_organizadorEvento.setText(extras.getString("organizador"));

        btn_editarEvento.setOnClickListener(v->{
            Intent oIntento = new Intent(ActividadDetalleEvento.this, EditarEvento.class);
            oIntento.putExtra("id", extras.getInt("id"));
            startActivity(oIntento);
        });
        btn_eliminarEvento.setOnClickListener(v->{
            eliminarEvento(extras.getInt("id"));
        });

    }

    private void eliminarEvento(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este evento?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    apiServicio.DeleteEventos(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ActividadDetalleEvento.this, "Evento eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                Intent oIntento = new Intent(ActividadDetalleEvento.this, ActividadCalendarioAdmin.class);
                                startActivity(oIntento);
                                finish();
                            } else {
                                Toast.makeText(ActividadDetalleEvento.this, "Error al eliminar el evento.", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ActividadDetalleEvento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

}