package Network;



import java.util.List;

import Models.Evento;
import Models.Publicacion;
import Models.Punto;
import Models.Usuario;
import ViewModels.RespuestaObtenida;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiServicioReciclaje {

    // Recupero una lista de Publicaciones
    @GET("Publicaciones")
    Call<List<Publicacion>> GetPublicaciones();

    @POST("Publicaciones")
    Call<Publicacion> PostPublicaciones(@Body Publicacion publicacion);

    //Recupero una lista de Ubicaciones
    @GET("Puntos")
    Call<List<Punto>> getPuntos();
    //Envio una Ubicacion:
    @POST("Puntos")
    Call<Punto> PostPuntos(@Body Punto punto);
    // Editar una Ubicacion
    @PUT("Puntos/{id}")
    Call<Punto> PutPuntos(@Path("id") int id, @Body Punto punto);

    // Eliminar una Ubicacion
    @DELETE("Puntos/{id}")
    Call<Void> DeletePuntos(@Path("id") int id);

    // Falta el PU y DELETE para Publicaciones

    // ACA LOS DEMAS PARA LAS DOS TABLAS QUE FALTAN

    @GET("Usuarios")
    Call<List<Usuario>> getUsuarios();

    @POST("Usuarios")
    Call<Usuario> PostUsuarios(@Body Usuario usuario);

    @Multipart
    @POST("Usuarios/UploadImage")
    Call<RespuestaObtenida> respuestaImagen(@Part MultipartBody.Part file);

    // Actualizar un usuario existente
    @PUT("Usuarios/{id}")
    Call<Usuario> actualizarUsuario(@Path("id") int idUsuario, @Body Usuario usuario);

    @GET("Usuarios/{id}")
    Call<Usuario> obtenerUsuarioPorId(@Path("id") int idUsuario);

    @GET("Eventos")
    Call<List<Evento>> getEventos();

    @POST("Eventos")
    Call<Evento> PostEventos(@Body Evento evento);

    @PUT("Eventos/{id}")
    Call<Evento> PutEventos(@Path("id") int idEvento, @Body Evento evento);

    @retrofit2.http.DELETE("Eventos/{id}")
    Call<Void> DeleteEventos(@Path("id") int idEvento);
}
