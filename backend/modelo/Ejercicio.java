package backend.modelo;

public class Ejercicio {
    private String codigo;
    private String nombre;
    private String tipo;
    private String nivelIntensidad;
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

    public String getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public String getNivelIntensidad() { return nivelIntensidad; }
    public int getTiempoMinutos() { return tiempoMinutos; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getUltimaSemanaUso() { return ultimaSemanaUso; }

    // FIX 3: Setter para poder actualizar la semana de uso al generar una rutina
    public void setUltimaSemanaUso(int semana) { this.ultimaSemanaUso = semana; }
}
