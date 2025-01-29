package Models;

public class Evento {
    protected int id;
    protected String titulo;
    protected String descripcion;
    protected String lugar;
    protected String fecha;
    protected String organizador;


    public Evento(int id, String titulo, String descripcion, String lugar, String fecha, String organizador) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.fecha = fecha;
        this.organizador = organizador;
    }
    public Evento(){

    }

    @Override
    public String toString() {
        return titulo+": " + descripcion + ". Organizado por: "+ organizador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }
}
