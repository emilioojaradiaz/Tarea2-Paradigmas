package backend.logica;

import backend.modelo.Ejercicio;
import backend.eventos.Suscriptor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorRutinas {
    private List<Ejercicio> inventarioEjercicios;
    private List<Suscriptor> suscriptores;

    // FIX 1: semanaActual ahora es un campo configurable, no hardcodeado
    private int semanaActual;

    public GestorRutinas() {
        inventarioEjercicios = new ArrayList<>();
        suscriptores = new ArrayList<>();
        this.semanaActual = 1;
    }

    // FIX 1: Permite configurar la semana actual desde afuera
    public void setSemanaActual(int semana) { this.semanaActual = semana; }
    public int getSemanaActual() { return semanaActual; }

    public void suscribir(Suscriptor s) {
        if (!suscriptores.contains(s)) {
            suscriptores.add(s);
        }
    }

    // FIX 2: Método para desuscribirse (necesario cuando VentanaGeneracion se cierra)
    public void desuscribir(Suscriptor s) {
        suscriptores.remove(s);
    }

    // --- REQUERIMIENTO 1: CARGA DE ARCHIVO ---
    public void cargarEjerciciosDesdeArchivo(String rutaArchivo) {
        inventarioEjercicios.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                // FIX 4: Ignorar encabezado si el CSV lo tiene
                if (linea.trim().toLowerCase().startsWith("codigo") ||
                    linea.trim().toLowerCase().startsWith("código")) {
                    continue;
                }
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(",");

                if (datos.length != 7) {
                    throw new IllegalArgumentException(
                        "La línea " + numeroLinea + " no tiene los 7 datos requeridos.");
                }

                Ejercicio ej = new Ejercicio(
                    datos[0].trim(),
                    datos[1].trim(),
                    datos[2].trim(),
                    datos[3].trim(),
                    Integer.parseInt(datos[4].trim()),
                    datos[5].trim(),
                    Integer.parseInt(datos[6].trim())
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

        for (Ejercicio ej : inventarioEjercicios) {
            boolean intensidadCoincide = ej.getNivelIntensidad().equalsIgnoreCase(intensidadRequerida);

            // FIX 1: Usa semanaActual dinámico
            boolean noEsSemanaConsecutiva = Math.abs(semanaActual - ej.getUltimaSemanaUso()) > 1;

            if (intensidadCoincide && noEsSemanaConsecutiva) {
                if (ej.getTipo().equalsIgnoreCase("Cardiovascular") && encontradosCardio < cantCardio) {
                    rutinaGenerada.add(ej);
                    encontradosCardio++;
                } else if (ej.getTipo().equalsIgnoreCase("Fuerza") && encontradosFuerza < cantFuerza) {
                    rutinaGenerada.add(ej);
                    encontradosFuerza++;
                }
            }

            if (encontradosCardio == cantCardio && encontradosFuerza == cantFuerza) break;
        }

        if (encontradosCardio < cantCardio || encontradosFuerza < cantFuerza) {
            notificarError(
                "No hay suficientes ejercicios que cumplan con estos requisitos.\n" +
                "Cardio encontrado: " + encontradosCardio + "/" + cantCardio +
                " | Fuerza encontrada: " + encontradosFuerza + "/" + cantFuerza
            );
        } else {
            // FIX 3: Actualizar la semana de uso de cada ejercicio seleccionado
            for (Ejercicio ej : rutinaGenerada) {
                ej.setUltimaSemanaUso(semanaActual);
            }
            // Notificar con copia de la lista para evitar modificaciones externas
            List<Suscriptor> copia = new ArrayList<>(suscriptores);
            for (Suscriptor s : copia) {
                s.onRutinaGenerada(rutinaGenerada);
            }
        }
    }

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
        // Notificar con copia para evitar ConcurrentModificationException
        List<Suscriptor> copia = new ArrayList<>(suscriptores);
        for (Suscriptor s : copia) {
            s.onError(msj);
        }
    }
}
