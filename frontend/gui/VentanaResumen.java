package frontend.gui;

import backend.modelo.Ejercicio;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class VentanaResumen extends JFrame {
    
    public VentanaResumen(List<Ejercicio> rutina) {
        setTitle("Resumen Final de la Rutina");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        int cardio = 0, fuerza = 0;
        int basico = 0, intermedio = 0, avanzado = 0, alto = 0;
        int tiempoTotal = 0;

        // Calculamos los totales
        for (Ejercicio ej : rutina) {
            tiempoTotal += ej.getTiempoMinutos();
            if (ej.getTipo().equalsIgnoreCase("Cardiovascular")) cardio++;
            else fuerza++;

            switch (ej.getNivelIntensidad().toLowerCase()) {
                case "básico": basico++; break;
                case "intermedio": intermedio++; break;
                case "avanzado": avanzado++; break;
                case "alto rendimiento": alto++; break;
            }
        }

        String textoHTML = String.format("<html><div style='text-align: center; padding: 10px;'>" +
                "<h2>¡Rutina Generada!</h2>" +
                "<b>Tiempo Total:</b> %d min<br><br>" +
                "<b>Por Tipo:</b><br>" +
                "Cardio: %d | Fuerza: %d<br><br>" +
                "<b>Por Intensidad:</b><br>" +
                "Básico: %d | Intermedio: %d<br>" +
                "Avanzado: %d | Alto Rendimiento: %d" +
                "</div></html>", 
                tiempoTotal, cardio, fuerza, basico, intermedio, avanzado, alto);

        JLabel lblResumen = new JLabel(textoHTML);
        lblResumen.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        add(lblResumen, BorderLayout.CENTER);
        add(btnCerrar, BorderLayout.SOUTH);
    }
}