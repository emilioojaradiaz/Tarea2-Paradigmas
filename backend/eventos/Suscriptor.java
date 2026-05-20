package backend.eventos;

import backend.modelo.Ejercicio;
import java.util.List;

/**
 * Interfaz que implementarán las ventanas (Frontend) para 
 * escuchar las respuestas del Backend de manera asincrónica.
 */
public interface Suscriptor {
    
    // 1. Evento: Cuando el archivo de ejercicios se carga correctamente [cite: 34]
    void onDatosCargados(int total, int tiempoTotal, int cantCardio, int cantFuerza, 
                         int cantBasico, int cantIntermedio, int cantAvanzado, int cantAlto);
    
    // 2. Evento: Cuando ocurre cualquier error (archivo no existe, formato malo, etc.) [cite: 69]
    void onError(String mensaje);

    // 3. Evento: Cuando el backend termina de armar la rutina solicitada [cite: 46]
    // Recibe la lista final de ejercicios para que la interfaz los muestre uno por uno.
    void onRutinaGenerada(List<Ejercicio> rutina);
}