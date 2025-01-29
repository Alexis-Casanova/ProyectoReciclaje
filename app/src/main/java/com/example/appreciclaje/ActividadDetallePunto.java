package com.example.appreciclaje;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import Models.Punto;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadDetallePunto extends AppCompatActivity {
    TextView lb_nombrePuntoDet, lb_direccionPuntoDet, lb_referenciaPuntoDet;
    Button btn_editarPunto, btn_eliminarPunto;
    int idPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalle_punto);

        lb_nombrePuntoDet = findViewById(R.id.lb_nombrePuntoDet);
        lb_direccionPuntoDet = findViewById(R.id.lb_direccionPuntoDet);
        lb_referenciaPuntoDet = findViewById(R.id.lb_referenciaPuntoDet);
        btn_editarPunto = findViewById(R.id.btn_editarPunto);
        btn_eliminarPunto = findViewById(R.id.btn_eliminarPunto);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPunto = extras.getInt("id");
            lb_nombrePuntoDet.setText(extras.getString("nombre"));
            lb_direccionPuntoDet.setText(extras.getString("direccion"));
            lb_referenciaPuntoDet.setText(extras.getString("referencia"));
        }

        btn_editarPunto.setOnClickListener(v -> {
            Intent intent = new Intent(ActividadDetallePunto.this, EditarPunto.class);
            intent.putExtra("id", idPunto);
            startActivity(intent);
        });

        btn_eliminarPunto.setOnClickListener(v -> eliminarPunto(idPunto));
    }

    private void eliminarPunto(int id) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este punto?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);
                    apiServicio.DeletePuntos(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ActividadDetallePunto.this, "Punto eliminado correctamente.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActividadDetallePunto.this, ActividadPunto.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ActividadDetallePunto.this, "Error al eliminar el punto.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ActividadDetallePunto.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}