package Models;

public class Punto {
    private int idPunto;
    private String nombre;
    private String direccion;
    private String referencia;

    public Punto(int idPunto, String nombre, String direccion, String referencia) {
        this.idPunto = idPunto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
    }
    public Punto(){}

    @Override
    public String toString() {
        return nombre +", en: " + direccion;
    }

    public int getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(int idPunto) {
        this.idPunto = idPunto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
