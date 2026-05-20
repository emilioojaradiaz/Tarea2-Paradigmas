package backend.modelo;

public class Ejercicio {
    private String codigo;
    private String nombre;
    private String tipo; // "Cardiovascular" o "Fuerza"
    private String nivelIntensidad; // "Básico", "Intermedio", "Avanzado", "Alto rendimiento"
    private int tiempoMinutos;
    private String descripcion;
    private int ultimaSemanaUso;

    public Ejercicio(String codigo, String nombre, String tipo, String nivelIntensidad, 
                     int tiempoMinutos, String descripcion, int ultimaSemanaUso) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nivelIntensidad = nivelIntensidad;
        this.tiempoMinutos = tiempoMinutos;
        this.descripcion = descripcion;
        this.ultimaSemanaUso = ultimaSemanaUso;
    }

    // Genera todos los Getters y Setters aquí...
    public String getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public String getNivelIntensidad() { return nivelIntensidad; }
    public int getTiempoMinutos() { return tiempoMinutos; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getUltimaSemanaUso() { return ultimaSemanaUso; }
}