package Sesiones;

import android.content.Context;
import android.content.SharedPreferences;

import Models.Usuario;

public class SessionManager {
    private static final String PRE_NAME = "MySession";
    private static final String ID_USUARIO = "idUsuario";
    private static final String NOMBRE = "nombre";
    private static final String EMAIL = "email";
    private static final String DNI = "dni";
    private static final String LUGAR = "lugar";
    private static final String CONTRASENA = "contrasena";
    private static final String ROL = "rol";
    private static final String RECOMPENSA = "recompensa";
    private static final String RUTAUSUARIO = "rutaUsuario";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        this.editor = pref.edit();
    }

    public void crearLoginSession(Usuario oU){
        editor.putInt(ID_USUARIO, oU.getIdUsuario());
        editor.putString(NOMBRE, oU.getNombre());
        editor.putString(EMAIL, oU.getEmail());
        editor.putString(DNI, oU.getDni());
        editor.putString(LUGAR, oU.getLugar());
        editor.putString(CONTRASENA, oU.getContrasena());
        editor.putString(ROL, oU.getRol());
        editor.putInt(RECOMPENSA, oU.getRecompensa());
        editor.putString(RUTAUSUARIO, oU.getRutaUsuario());
        editor.commit();
    }

    public Usuario getUsuarioDetalles(){
        Usuario oU = new Usuario();
        oU.setIdUsuario(pref.getInt(ID_USUARIO, 0));
        oU.setNombre(pref.getString(NOMBRE, null));
        oU.setEmail(pref.getString(EMAIL, null));
        oU.setDni(pref.getString(DNI, null));
        oU.setLugar(pref.getString(LUGAR, null));
        oU.setContrasenaText(pref.getString(CONTRASENA, null));
        oU.setRol(pref.getString(ROL, null));
        oU.setRecompensa(pref.getInt(RECOMPENSA, 0));
        oU.setRutaUsuario(pref.getString(RUTAUSUARIO, null));
        return oU;
    }

    public boolean estaLogeado(){
        return pref.getString(EMAIL, null) != null;
    }
    public void logoutUsuario(){
        editor.clear();
        editor.commit();
    }
}
