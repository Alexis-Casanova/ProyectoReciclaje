package com.example.appreciclaje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.stream.Collectors;

import Adapters.AdapterPublicacion;
import Models.Publicacion;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ListView lv_postMain;
    ImageButton btn_loginUser, btn_loginAdmi, btn_registro, btn_calendar,btn_ubicaciones,btn_buscarMain, btn_limpiarBuscarMain;
    Spinner sp_barrioMain;
    Toolbar tb_main;
    String buscarBarrio = "";

    @SuppressLint("MissingInflatedId")
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
        btn_buscarMain = findViewById(R.id.btn_buscarMain);
        btn_limpiarBuscarMain = findViewById(R.id.btn_limpiarBuscarMain);
        sp_barrioMain = findViewById(R.id.sp_barrioMain);
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

        btn_ubicaciones.setOnClickListener(v ->{
            Intent oIntento = new Intent(MainActivity.this, ActividadPunto.class);
            startActivity(oIntento);
        });


        btn_buscarMain.setOnClickListener(v ->{
            buscarBarrio = sp_barrioMain.getSelectedItem().toString();
            mostrarPublicaciones();
        });
        btn_limpiarBuscarMain.setOnClickListener(v ->{
            buscarBarrio = "";
            sp_barrioMain.setSelection(0);
            mostrarPublicaciones();
        });
        configurarSpinners();
        mostrarPublicaciones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPublicaciones();
    }

    private void configurarSpinners() {
        String[] tipos = {"San Pedro", "San Sebastían", "El Cumbe", "San Martín"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_barrioMain.setAdapter(adapterTipo);
    }

    private void mostrarPublicaciones() {
        ApiServicioReciclaje ApiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class); //Creamos el servicio

        Call<List<Publicacion>> oCall = ApiServicio.GetPublicaciones();
        oCall.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<Publicacion> listaPublicaciones = response.body();

                    if (!buscarBarrio.isEmpty()) {
                        listaPublicaciones = listaPublicaciones.stream()
                                .filter(p -> p.getLugar().equalsIgnoreCase(buscarBarrio))
                                .collect(Collectors.toList());                    }

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