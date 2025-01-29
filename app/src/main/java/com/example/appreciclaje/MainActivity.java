package com.example.appreciclaje;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

import Adapters.AdapterPublicacion;
import Models.Publicacion;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ListView lv_postMain;
    ImageButton btn_loginUser, btn_loginAdmi, btn_registro, btn_calendar, btn_agregarEvento,btn_ubicaciones;
    Toolbar tb_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_ubicaciones = findViewById(R.id.btn_ubi);
        lv_postMain = findViewById(R.id.lv_post);
        btn_loginAdmi=findViewById(R.id.btn_admi);
        btn_loginUser=findViewById(R.id.btn_usuario);
        btn_registro = findViewById(R.id.btn_registro);
        btn_calendar = findViewById(R.id.btn_calendar);
        btn_agregarEvento= findViewById(R.id.btn_agregarEvento);
        tb_main = findViewById(R.id.tb_main);
        setSupportActionBar(tb_main);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        btn_loginUser.setOnClickListener(v -> {
            Intent oIntento = new Intent(MainActivity.this, ActividadLoginUsuario.class);
            startActivity(oIntento);
        });

        btn_loginAdmi.setOnClickListener(v -> {
            Intent oIntento = new Intent(MainActivity.this, ActividadLoginAdministrador.class);
            startActivity(oIntento);
        });

        btn_registro.setOnClickListener(v -> {
            Intent oIntento = new Intent(MainActivity.this, RegistrarUsuario.class);
            startActivity(oIntento);
        });
        btn_calendar.setOnClickListener(v ->{
            Intent oIntento = new Intent(MainActivity.this, ActividadCalendario.class);
            startActivity(oIntento);
        });
        btn_agregarEvento.setOnClickListener(v ->{
            Intent oIntento = new Intent(MainActivity.this, RegistrarEvento.class);
            startActivity(oIntento);
        });
        btn_ubicaciones.setOnClickListener(v ->{
            Intent oIntento = new Intent(MainActivity.this, ActividadPunto.class);
            startActivity(oIntento);
        });

        mostrarPublicaciones();
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
                    List<Publicacion> listaPublicaciones = response.body();
                    //lv_postMain.setAdapter(new ArrayAdapter<Publicacion>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, listaPublicaciones));
                    AdapterPublicacion adapter = new AdapterPublicacion(MainActivity.this, listaPublicaciones);
                    lv_postMain.setAdapter(adapter);
                } else{
                    Toast.makeText(MainActivity.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}