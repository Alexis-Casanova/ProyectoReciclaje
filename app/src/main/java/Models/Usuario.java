package Models;

public class Usuario {
     protected int idUsuario;
     protected String nombre;
     protected String email;
     protected String dni;
     protected String lugar;
     protected String contrasena;
     protected String rol;
     protected String rutaUsuario;

     public Usuario(int idUsuario, String nombre, String email, String dni, String lugar, String contrasena, String rol, String rutaUsuario) {
          this.idUsuario = idUsuario;
          this.nombre = nombre;
          this.email = email;
          this.dni = dni;
          this.lugar = lugar;
          this.contrasena = contrasena;
          this.rol = rol;
          this.rutaUsuario = rutaUsuario;
     }

     public Usuario(int idUsuario, String nombre, String email, String dni, String lugar, String contrasena, String rol) {
          this.idUsuario = idUsuario;
          this.nombre = nombre;
          this.email = email;
          this.dni = dni;
          this.lugar = lugar;
          this.contrasena = contrasena;
          this.rol = rol;
     }

     public Usuario(){

     }

     @Override
     public String toString() {
          return     nombre + "rol: '" + rol + '\''
                  + rutaUsuario;
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

     public String getContrasena() {
          return contrasena;
     }

     public void setContrasena(String contrasena) {
          this.contrasena = contrasena;
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
