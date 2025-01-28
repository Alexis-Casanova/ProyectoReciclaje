package Network;



import java.util.List;

import Models.Publicacion;
import Models.Usuario;
import ViewModels.RespuestaObtenida;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
}
