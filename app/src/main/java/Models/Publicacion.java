package Models;

public class Publicacion {
    protected int idPublicacion;
    protected String contenido;
    protected String lugar;
    protected String fecha;
    protected String tipo;
    protected String rutaPublicacion;
    protected int usuarioId;
    protected Usuario usuario;

    public Publicacion(int idPublicacion, String contenido, String lugar, String fecha, String tipo, String rutaPublicacion, int usuarioId, Usuario usuario) {
        this.idPublicacion = idPublicacion;
        this.contenido = contenido;
        this.lugar = lugar;
        this.fecha = fecha;
        this.tipo = tipo;
        this.rutaPublicacion = rutaPublicacion;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
    }

    public Publicacion(int idPublicacion, String contenido, String lugar, String fecha, String tipo, int usuarioId, Usuario usuario) {
        this.idPublicacion = idPublicacion;
        this.contenido = contenido;
        this.lugar = lugar;
        this.fecha = fecha;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
    }

    public Publicacion(){

    }



    @Override
    public String toString() {
        return "("+idPublicacion+") "+ contenido + ", " + fecha +", " +tipo;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRutaPublicacion() {
        return rutaPublicacion;
    }

    public void setRutaPublicacion(String rutaPublicacion) {
        this.rutaPublicacion = rutaPublicacion;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
