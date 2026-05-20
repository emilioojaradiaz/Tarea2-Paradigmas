package frontend.gui;

import backend.logica.GestorRutinas;
import javax.swing.*;
import java.awt.*;

public class VentanaGeneracion extends JFrame {
    private GestorRutinas gestor;

    public VentanaGeneracion(GestorRutinas gestor) {
        this.gestor = gestor;
        setTitle("Generar Nueva Rutina");
        setSize(350, 250);
        setLocationRelativeTo(null); 
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel lblCardio = new JLabel(" Cant. Cardiovascular:");
        JTextField txtCardio = new JTextField();

        JLabel lblFuerza = new JLabel(" Cant. Fuerza:");
        JTextField txtFuerza = new JTextField();

        JLabel lblIntensidad = new JLabel(" Nivel de Intensidad:");
        String[] intensidades = {"Básico", "Intermedio", "Avanzado", "Alto rendimiento"};
        JComboBox<String> comboIntensidad = new JComboBox<>(intensidades);

        JButton btnGenerar = new JButton("Crear Rutina");

        btnGenerar.addActionListener(e -> {
            try {
                int cardio = Integer.parseInt(txtCardio.getText().trim());
                int fuerza = Integer.parseInt(txtFuerza.getText().trim());
                String intensidad = (String) comboIntensidad.getSelectedItem();
                
                // ¡AQUÍ ES DONDE SE USA LA VARIABLE GESTOR!
                gestor.generarRutina(cardio, fuerza, intensidad);
                dispose(); 
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingresa números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(lblCardio); add(txtCardio);
        add(lblFuerza); add(txtFuerza);
        add(lblIntensidad); add(comboIntensidad);
        add(new JLabel("")); add(btnGenerar); 
    }
}