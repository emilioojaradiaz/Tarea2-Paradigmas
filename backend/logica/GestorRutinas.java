package backend.logica;

import backend.modelo.Ejercicio;
import backend.eventos.Suscriptor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorRutinas {
    // El sistema debe mantener los ejercicios en memoria usando un vector/lista [cite: 68]
    private List<Ejercicio> inventarioEjercicios;
    private List<Suscriptor> suscriptores;

    public GestorRutinas() {
        inventarioEjercicios = new ArrayList<>();
        suscriptores = new ArrayList<>();
    }

    // Método para conectar la interfaz gráfica con la lógica (Patrón Observador) 
    public void suscribir(Suscriptor s) {
        suscriptores.add(s);
    }

    // --- REQUERIMIENTO 1: CARGA DE ARCHIVO ---
    public void cargarEjerciciosDesdeArchivo(String rutaArchivo) {
        inventarioEjercicios.clear();
        // Manejo de excepciones por archivo inexistente [cite: 69]
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                
                // Validación de información incompleta o formato incorrecto [cite: 69]
                if (datos.length != 7) {
                    throw new IllegalArgumentException("La línea no tiene los 7 datos requeridos."); 
                }
                
                Ejercicio ej = new Ejercicio(
                    datos[0].trim(),                 // Código
                    datos[1].trim(),                 // Nombre
                    datos[2].trim(),                 // Tipo
                    datos[3].trim(),                 // Intensidad
                    Integer.parseInt(datos[4].trim()), // Tiempo
                    datos[5].trim(),                 // Descripción
                    Integer.parseInt(datos[6].trim())  // Última semana de uso
                );
                inventarioEjercicios.add(ej);
            }
            notificarCargaExitosa();

        } catch (IOException e) {
            notificarError("Error: El archivo no existe o no se puede leer.");
        } catch (IllegalArgumentException e) {
            notificarError("Error de formato en el archivo: " + e.getMessage());
        }
    }

    // --- REQUERIMIENTO 2: GENERACIÓN DE RUTINA ---
    public void generarRutina(int cantCardio, int cantFuerza, String intensidadRequerida) {
        List<Ejercicio> rutinaGenerada = new ArrayList<>();
        int encontradosCardio = 0;
        int encontradosFuerza = 0;
        
        // Asumimos una semana actual fija (ej. semana 10) para calcular si son consecutivas
        int semanaActual = 10; 

        for (Ejercicio ej : inventarioEjercicios) {
            boolean intensidadCoincide = ej.getNivelIntensidad().equalsIgnoreCase(intensidadRequerida);
            
            // Restricción: No repetición en semanas consecutivas [cite: 44]
            boolean noEsSemanaConsecutiva = Math.abs(semanaActual - ej.getUltimaSemanaUso()) > 1;

            if (intensidadCoincide && noEsSemanaConsecutiva) {
                if (ej.getTipo().equalsIgnoreCase("Cardiovascular") && encontradosCardio < cantCardio) {
                    rutinaGenerada.add(ej);
                    encontradosCardio++;
                } 
                else if (ej.getTipo().equalsIgnoreCase("Fuerza") && encontradosFuerza < cantFuerza) {
                    rutinaGenerada.add(ej);
                    encontradosFuerza++;
                }
            }
            
            if (encontradosCardio == cantCardio && encontradosFuerza == cantFuerza) {
                break; // Ya encontramos todo lo solicitado
            }
        }

        if (encontradosCardio < cantCardio || encontradosFuerza < cantFuerza) {
            notificarError("No hay suficientes ejercicios en el archivo que cumplan con estos requisitos.");
        } else {
            // Notificamos al frontend que la rutina está lista y le enviamos la lista 
            for (Suscriptor s : suscriptores) {
                s.onRutinaGenerada(rutinaGenerada);
            }
        }
    }

    // --- MÉTODOS PRIVADOS DE NOTIFICACIÓN ---
    private void notificarCargaExitosa() {
        int total = inventarioEjercicios.size();
        int tiempoTotal = 0;
        int cantCardio = 0, cantFuerza = 0;
        int cantBasico = 0, cantInter = 0, cantAvanzado = 0, cantAlto = 0;

        for (Ejercicio ej : inventarioEjercicios) {
            tiempoTotal += ej.getTiempoMinutos();
            if (ej.getTipo().equalsIgnoreCase("Cardiovascular")) cantCardio++;
            else cantFuerza++;

            switch (ej.getNivelIntensidad().toLowerCase()) {
                case "básico": cantBasico++; break;
                case "intermedio": cantInter++; break;
                case "avanzado": cantAvanzado++; break;
                case "alto rendimiento": cantAlto++; break;
            }
        }

        for (Suscriptor s : suscriptores) {
            s.onDatosCargados(total, tiempoTotal, cantCardio, cantFuerza, 
                              cantBasico, cantInter, cantAvanzado, cantAlto);
        }
    }

    private void notificarError(String msj) {
        for (Suscriptor s : suscriptores) {
            s.onError(msj);
        }
    }
}