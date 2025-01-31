package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Adapters.AdapterPublicacion;
import Models.Publicacion;
import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadInicioAdmin extends AppCompatActivity {

    ListView lv_popst;
    ImageButton btn_agregarUbicaciones, btn_addEvent, btn_salir;
    public static Publicacion publicacionObtenida = new Publicacion();
    public static List<Publicacion> listaPublicaciones;
    private Usuario usuarioLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_inicio_admin);

        lv_popst = findViewById(R.id.lv_popst);
        btn_agregarUbicaciones = findViewById(R.id.btn_agregarUbicaciones);
        btn_addEvent = findViewById(R.id.btn_addEvent);
        btn_salir = findViewById(R.id.btn_salirAdmi);


        btn_salir.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioAdmin.this, MainActivity.class);
            oIntento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(oIntento);
            Toast.makeText(ActividadInicioAdmin.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        });

        lv_popst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publicacionObtenida = listaPublicaciones.get(position);
                Intent oIntento = new Intent(ActividadInicioAdmin.this,ActividadDetalleRecompensa.class);
                startActivity(oIntento);
            }
        });

        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oIntento = new Intent(ActividadInicioAdmin.this,ActividadCalendarioAdmin.class);
                startActivity(oIntento);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPublicaciones();
    }

    private void mostrarPublicaciones() {
        ApiServicioReciclaje ApiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class); //Creamos el servicio

        Call<List<Publicacion>> oCall = ApiServicio.GetPublicaciones();
        oCall.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listaPublicaciones = response.body();
                    AdapterPublicacion adapter = new AdapterPublicacion(ActividadInicioAdmin.this, listaPublicaciones);
                    lv_popst.setAdapter(adapter);
                } else{
                    Toast.makeText(ActividadInicioAdmin.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Toast.makeText(ActividadInicioAdmin.this, "Error API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
