package com.example.appreciclaje;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Models.Usuario;
import Network.ApiServicioReciclaje;
import Network.RetrofitClient;
import Sesiones.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadLoginAdministrador extends AppCompatActivity {
    EditText txt_emailAdmin, txt_passwordAdmin;
    Button btn_ingresarAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_login_administrador);

        txt_emailAdmin = findViewById(R.id.txt_emailAdmi);
        txt_passwordAdmin = findViewById(R.id.txt_passwordAdmi);
        btn_ingresarAdmin = findViewById(R.id.btn_ingresarAdmi);
        btn_ingresarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_emailAdmin.getText().toString().trim();
                String pass = txt_passwordAdmin.getText().toString().trim();

                if(!email.isEmpty() && !pass.isEmpty()){
                    Login(email, pass);
                }else{
                    Toast.makeText(ActividadLoginAdministrador.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }
    private void Login(String email, String pass) {
        ApiServicioReciclaje apiSercico = RetrofitClient.getCliente().create(ApiServicioReciclaje.class);

        Call<List<Usuario>> call = apiSercico.getUsuarios();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Usuario> listaUsarios = response.body();

                    Usuario usuarioLoggeado = null;
                    for (Usuario oU:
                            listaUsarios) {
                        if(oU.getEmail().equalsIgnoreCase(email)){
                            Usuario aux = new Usuario();
                            aux.setContrasena(pass);

                            if(oU.getContrasena().equalsIgnoreCase(aux.getContrasena())){
                                usuarioLoggeado = oU;
                                break;
                            }
                        }
                    }
                    if(usuarioLoggeado !=null){
                        if(usuarioLoggeado.getRol().equalsIgnoreCase("administrador")){
                            SessionManager sessionManager = new SessionManager(ActividadLoginAdministrador.this);
                            sessionManager.crearLoginSession(usuarioLoggeado);
                            Toast.makeText(ActividadLoginAdministrador.this, "Bienvenido JEFE!!! " + usuarioLoggeado.getNombre(), Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(ActividadLoginAdministrador.this, "Tu rol no es 'administrador'. Verifica tus credenciales", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActividadLoginAdministrador.this, "Email/Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActividadLoginAdministrador.this,
                            "No se pudo obtener la lista de usuarios",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ActividadLoginAdministrador.this, "Error de la API: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}