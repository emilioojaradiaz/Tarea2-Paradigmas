package frontend.gui;

import backend.logica.GestorRutinas;
import backend.eventos.Suscriptor;
import backend.modelo.Ejercicio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// FIX 2: VentanaGeneracion ahora implementa Suscriptor para recibir errores directamente
public class VentanaGeneracion extends JFrame implements Suscriptor {
    private GestorRutinas gestor;

    public VentanaGeneracion(GestorRutinas gestor) {
        this.gestor = gestor;
        // FIX 2: Se suscribe para escuchar errores durante la generación
        this.gestor.suscribir(this);

        setTitle("Generar Nueva Rutina");
        setSize(370, 220);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCardio = new JLabel(" Cant. Cardiovascular:");
        JTextField txtCardio = new JTextField();

        JLabel lblFuerza = new JLabel(" Cant. Fuerza:");
        JTextField txtFuerza = new JTextField();

        JLabel lblIntensidad = new JLabel(" Nivel de Intensidad:");
        String[] intensidades = {"Básico", "Intermedio", "Avanzado", "Alto rendimiento"};
        JComboBox<String> comboIntensidad = new JComboBox<>(intensidades);

        JButton btnCancelar = new JButton("Cancelar");
        // FIX 5: Botón cancelar para cerrar sin generar
        btnCancelar.addActionListener(e -> {
            gestor.desuscribir(this); // se desuscribe al cerrar
            dispose();
        });

        JButton btnGenerar = new JButton("Crear Rutina");
        btnGenerar.addActionListener(e -> {
            try {
                int cardio = Integer.parseInt(txtCardio.getText().trim());
                int fuerza = Integer.parseInt(txtFuerza.getText().trim());

                if (cardio < 0 || fuerza < 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Las cantidades no pueden ser negativas.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String intensidad = (String) comboIntensidad.getSelectedItem();
                // FIX 2: Ya NO hace dispose() aquí; espera el evento onRutinaGenerada u onError
                gestor.generarRutina(cardio, fuerza, intensidad);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingresa números válidos.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(lblCardio);     add(txtCardio);
        add(lblFuerza);     add(txtFuerza);
        add(lblIntensidad); add(comboIntensidad);
        add(btnCancelar);   add(btnGenerar);
    }

    @Override
    public void onRutinaGenerada(List<Ejercicio> rutina) {
        // FIX 2: Solo cierra cuando la rutina fue generada exitosamente
        gestor.desuscribir(this);
        dispose();
    }

    @Override
    public void onError(String mensaje) {
        // FIX 2: Muestra el error directamente en esta ventana, sin cerrarla
        JOptionPane.showMessageDialog(this, mensaje, "Error al Generar Rutina", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onDatosCargados(int total, int tiempoTotal, int cantCardio, int cantFuerza,
                                int cantBasico, int cantIntermedio, int cantAvanzado, int cantAlto) {
        // No aplica en esta ventana
    }
}
