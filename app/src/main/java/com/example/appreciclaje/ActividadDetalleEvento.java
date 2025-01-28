package com.example.appreciclaje;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActividadDetalleEvento extends AppCompatActivity {
    TextView lb_tituloEventoDet, lb_descripEvento, lb_horarioEventoDet, lb_lugarEventoDet, lb_organizadorEvento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_detalle_evento);

        lb_tituloEventoDet = findViewById(R.id.lb_tituloEventoDet);
        lb_descripEvento = findViewById(R.id.lb_descripEvento);
        lb_horarioEventoDet = findViewById(R.id.lb_horarioEventoDet);
        lb_lugarEventoDet = findViewById(R.id.lb_lugarEventoDet);
        lb_organizadorEvento = findViewById(R.id.lb_organizadorEvento);

        Bundle extras = getIntent().getExtras();

        lb_tituloEventoDet.setText(extras.getString("titulo"));
        lb_descripEvento.setText(extras.getString("descripcion"));
        lb_horarioEventoDet.setText(extras.getString("fecha"));
        lb_lugarEventoDet.setText(extras.getString("lugar"));
        lb_organizadorEvento.setText(extras.getString("organizador"));

    }
}