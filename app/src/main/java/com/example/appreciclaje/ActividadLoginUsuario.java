package com.example.appreciclaje;

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

public class ActividadLoginUsuario extends AppCompatActivity {
    EditText txt_emailUser, txt_passwordUser;
    Button btn_ingresarUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_login_usuario);

        txt_emailUser = findViewById(R.id.txt_emailUser);
        txt_passwordUser = findViewById(R.id.txt_passwordUser);
        btn_ingresarUser = findViewById(R.id.btn_ingresarUser);
        btn_ingresarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_emailUser.getText().toString().trim();
                String pass = txt_passwordUser.getText().toString().trim();

                if(!email.isEmpty() && !pass.isEmpty()){
                    Login(email, pass);
                }else{
                    Toast.makeText(ActividadLoginUsuario.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
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
                            //Para validar la contraseña con SHA256
                            Usuario aux = new Usuario();
                            aux.setContrasena(pass);

                            if(oU.getContrasena().equalsIgnoreCase(aux.getContrasena())){
                                usuarioLoggeado = oU;
                                break;
                            }
                        }
                    }
                    if(usuarioLoggeado !=null){
                        if(usuarioLoggeado.getRol().equalsIgnoreCase("usuario")){
                            SessionManager sessionManager = new SessionManager(ActividadLoginUsuario.this);
                            sessionManager.crearLoginSession(usuarioLoggeado);
                            Toast.makeText(ActividadLoginUsuario.this, "Bienvenido " + usuarioLoggeado.getNombre(), Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(ActividadLoginUsuario.this, "Tu rol no es 'usuario'. Verifica tus credenciales", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ActividadLoginUsuario.this, "Email/Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActividadLoginUsuario.this,
                            "No se pudo obtener la lista de usuarios",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ActividadLoginUsuario.this, "Error de la API: "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}