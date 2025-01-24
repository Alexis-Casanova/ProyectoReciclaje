package Models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class Usuario {

     protected int idUsuario;
     protected String nombre;
     protected String email;
     protected String dni;
     protected String lugar;
     protected String contrasena;
     protected String rol;
     protected int recompensa;
     protected String rutaUsuario;

     public Usuario(int idUsuario, String nombre, String email, String dni, String lugar,
                    String contrasenaEnTextoPlano, String rol, int recompensa, String rutaUsuario) {
          this.idUsuario = idUsuario;
          this.nombre = nombre;
          this.email = email;
          this.dni = dni;
          this.lugar = lugar;
          this.contrasena = getSHA256(contrasenaEnTextoPlano);
          this.rol = rol;
          this.recompensa = recompensa;
          this.rutaUsuario = rutaUsuario;
     }

     public Usuario(int idUsuario, String nombre, String email, String dni, String lugar,
                    String contrasenaEnTextoPlano, String rol,int recompensa) {
          this.idUsuario = idUsuario;
          this.nombre = nombre;
          this.email = email;
          this.dni = dni;
          this.lugar = lugar;
          this.contrasena = getSHA256(contrasenaEnTextoPlano);
          this.rol = rol;
          this.recompensa = recompensa;
     }

     public Usuario() {
     }

     /**
      * Toma un texto plano y devuelve su hash SHA-256 en formato hexadecimal.
      */
     private static String getSHA256(String passString) {
          try {
               // Obtenemos una instancia de SHA-256
               MessageDigest digest = MessageDigest.getInstance("SHA-256");

               // Convertimos el string a bytes (UTF-8)
               byte[] hash = digest.digest(passString.getBytes(StandardCharsets.UTF_8));

               // Convertimos cada byte del array en su representación hexadecimal
               StringBuilder hexString = new StringBuilder();
               for (byte b : hash) {
                    // %02x formatea el byte en hexadecimal, con 2 dígitos y relleno de cero en caso necesario
                    hexString.append(String.format("%02x", b));
               }

               // Retornamos el hash en forma de string
               return hexString.toString();
          } catch (NoSuchAlgorithmException e) {
               // SHA-256 debería existir siempre en Java, pero por seguridad:
               e.printStackTrace();
               return null;
          }
     }

     @Override
     public String toString() {
          return nombre + " rol: '" + rol +" Con recompensas: "+ recompensa + "'  " + rutaUsuario;
     }

     public int getRecompensa() {
          return recompensa;
     }

     public void setRecompensa(int recompensa) {
          this.recompensa = recompensa;
     }

     public int getIdUsuario() {
          return idUsuario;
     }
     public void setIdUsuario(int idUsuario) {
          this.idUsuario = idUsuario;
     }

     public String getNombre() {
          return nombre;
     }
     public void setNombre(String nombre) {
          this.nombre = nombre;
     }

     public String getEmail() {
          return email;
     }
     public void setEmail(String email) {
          this.email = email;
     }

     public String getDni() {
          return dni;
     }
     public void setDni(String dni) {
          this.dni = dni;
     }

     public String getLugar() {
          return lugar;
     }
     public void setLugar(String lugar) {
          this.lugar = lugar;
     }

     /**
      * Devuelve la contraseña hasheada (SHA-256).
      * Recuerda que NO es la contraseña original.
      */
     public String getContrasena() {
          return contrasena;
     }

     /**
      * Asigna una contraseña en TEXTO PLANO, pero internamente
      * la almacena hasheada con SHA-256.
      */
     public void setContrasena(String contrasenaEnTextoPlano) {
          this.contrasena = getSHA256(contrasenaEnTextoPlano);
     }

     public String getRol() {
          return rol;
     }
     public void setRol(String rol) {
          this.rol = rol;
     }

     public String getRutaUsuario() {
          return rutaUsuario;
     }
     public void setRutaUsuario(String rutaUsuario) {
          this.rutaUsuario = rutaUsuario;
     }
}
