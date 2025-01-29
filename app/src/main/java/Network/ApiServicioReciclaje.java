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

    // Publicaciones
    @GET("Publicaciones")
    Call<List<Publicacion>> GetPublicaciones();

    @POST("Publicaciones")
    Call<Publicacion> PostPublicaciones(@Body Publicacion publicacion);

    // Puntos
    @GET("Puntos")
    Call<List<Punto>> getPuntos();

    @POST("Puntos")
    Call<Punto> PostPuntos(@Body Punto punto);

    @PUT("Puntos/{id}")
    Call<Punto> PutPuntos(@Path("id") int id, @Body Punto punto);

    @DELETE("Puntos/{id}")
    Call<Void> DeletePuntos(@Path("id") int id);

    // Usuarios
    @GET("Usuarios")
    Call<List<Usuario>> getUsuarios();

    @POST("Usuarios")
    Call<Usuario> PostUsuarios(@Body Usuario usuario);

    @Multipart
    @POST("Usuarios/UploadImage")
    Call<RespuestaObtenida> respuestaImagen(@Part MultipartBody.Part file);

    @PUT("Usuarios/{id}")
    Call<Usuario> actualizarUsuario(@Path("id") int idUsuario, @Body Usuario usuario);

    @GET("Usuarios/{id}")
    Call<Usuario> obtenerUsuarioPorId(@Path("id") int idUsuario);

    //Eventos
    @GET("Eventos")
    Call<List<Evento>> getEventos();

    @POST("Eventos")
    Call<Evento> PostEventos(@Body Evento evento);

    @PUT("Eventos/{id}")
    Call<Evento> PutEventos(@Path("id") int idEvento, @Body Evento evento);

    @DELETE("Eventos/{id}")
    Call<Void> DeleteEventos(@Path("id") int idEvento);
}
