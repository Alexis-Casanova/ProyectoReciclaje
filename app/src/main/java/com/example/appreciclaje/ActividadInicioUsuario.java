package com.example.appreciclaje;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.stream.Collectors;

import Adapters.AdapterPublicacion;
import Models.Publicacion;
import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadInicioUsuario extends AppCompatActivity {

    ListView lv_postUsuario;
    TextView txt_titulo_usuario;
    ImageButton btn_detalleUsuario, btn_agregarPostUsuario, btn_ubiUser, btn_calendarUser;
    ImageButton btn_buscarUsuario, btn_limpiarBuscarUsuario, btn_logout;
    Spinner sp_barrioUsuario;
    private String buscarTipo = "";
    private Usuario usuarioLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_inicio_usuario);

        lv_postUsuario = findViewById(R.id.lv_usuario_post);
        txt_titulo_usuario = findViewById(R.id.txt_titulo_usuario);
        btn_agregarPostUsuario = findViewById(R.id.btn_agregar_postUsuario);
        btn_detalleUsuario = findViewById(R.id.btn_detalleUsuario);
        btn_buscarUsuario = findViewById(R.id.btn_buscarUsuario);
        btn_limpiarBuscarUsuario = findViewById(R.id.btn_limpiarBuscarUsuario);
        btn_ubiUser = findViewById(R.id.btn_ubiUser);
        btn_calendarUser = findViewById(R.id.btn_calendarUser);
        btn_logout = findViewById(R.id.btn_logOutIniUsuario);
        sp_barrioUsuario = findViewById(R.id.sp_usuario_barrio);

        configurarSpinners();

        SessionManager sessionManager = new SessionManager(this);
        usuarioLogeado = sessionManager.getUsuarioDetalles();

        if (usuarioLogeado == null) {
            Toast.makeText(this, "Debe iniciar sesión para editar el perfil.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        txt_titulo_usuario.setText("Bienvenido, " + usuarioLogeado.getNombre().split(" ")[0] + "!");


        btn_detalleUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, DetalleUsuario.class);
            startActivity(oIntento);
        });

        btn_agregarPostUsuario.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, RegistrarPublicacion.class);
            startActivity(oIntento);
        });

        btn_buscarUsuario.setOnClickListener(v -> {
            buscarTipo = sp_barrioUsuario.getSelectedItem().toString();
            mostrarPublicaciones();
        });

        btn_limpiarBuscarUsuario.setOnClickListener(v -> {
            buscarTipo = "";
            sp_barrioUsuario.setSelection(0);
            mostrarPublicaciones();
        });

        btn_ubiUser.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, ActividadPunto.class);
            startActivity(oIntento);
        });

        btn_calendarUser.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, ActividadCalendario.class);
            startActivity(oIntento);
        });

        btn_logout.setOnClickListener(v -> {
            Intent oIntento = new Intent(ActividadInicioUsuario.this, MainActivity.class);
            oIntento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(oIntento);
            Toast.makeText(ActividadInicioUsuario.this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        });
        mostrarPublicaciones();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPublicaciones();
    }

    private void configurarSpinners() {
        String[] barrios = {"Consejo", "Reciclar"};
        ArrayAdapter<String> adapterBarrio = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, barrios);
        adapterBarrio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_barrioUsuario.setAdapter(adapterBarrio);
    }

    private void mostrarPublicaciones() {
        ApiServicioReciclaje apiServicio = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

        Call<List<Publicacion>> oCall = apiServicio.GetPublicaciones();
        oCall.enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(Call<List<Publicacion>> call, Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Publicacion> listaPublicaciones = response.body();

                    if (!buscarTipo.isEmpty()) {
                        listaPublicaciones = listaPublicaciones.stream()
                                .filter(p -> p.getTipo().equalsIgnoreCase(buscarTipo))
                                .collect(Collectors.toList());
                    }

                    AdapterPublicacion adapter = new AdapterPublicacion(ActividadInicioUsuario.this, listaPublicaciones);
                    lv_postUsuario.setAdapter(adapter);
                } else {
                    Toast.makeText(ActividadInicioUsuario.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Publicacion>> call, Throwable t) {
                Toast.makeText(ActividadInicioUsuario.this, "Error API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
