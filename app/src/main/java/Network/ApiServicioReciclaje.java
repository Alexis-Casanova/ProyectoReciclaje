package Network;



import java.util.List;

import Models.Publicacion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiServicioReciclaje {

    // Recupero una lista de Publicaciones
    @GET("Publicaciones")
    Call<List<Publicacion>> GetPublicaciones();

    @POST("Publicaciones")
    Call<Publicacion> PostPublicaciones(@Body Publicacion publicacion);

    // Falta el PU y DELETE para Publicaciones

    // ACA LOS DEMAS PARA LAS DOS TABLAS QUE FALTAN
}
