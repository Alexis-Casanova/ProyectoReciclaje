package Models;

public class Punto {
    private String idPunto;
    private String nombre;
    private String direccion;
    private String referencia;

    public Punto(String idPunto, String nombre, String direccion, String referencia) {
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

    public String getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(String idPunto) {
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
