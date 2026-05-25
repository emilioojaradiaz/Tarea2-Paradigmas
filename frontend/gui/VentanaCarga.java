package frontend.gui;

import backend.logica.GestorRutinas;
import backend.eventos.Suscriptor;
import backend.modelo.Ejercicio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaCarga extends JFrame implements Suscriptor {
    private GestorRutinas gestor;

    public VentanaCarga(GestorRutinas gestor) {
        this.gestor = gestor;
        this.gestor.suscribir(this);

        setTitle("Cargar Ejercicios");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblSemana = new JLabel("Semana actual:");
        JSpinner spinnerSemana = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1));
        spinnerSemana.setPreferredSize(new Dimension(60, 28));
        spinnerSemana.addChangeListener(e -> {
            gestor.setSemanaActual((int) spinnerSemana.getValue());
        });

        JLabel lblArchivo = new JLabel("Archivo de ejercicios:");
        JButton btnSeleccionar = new JButton("Seleccionar archivo...");
        btnSeleccionar.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            int resultado = selector.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String ruta = selector.getSelectedFile().getAbsolutePath();
                gestor.cargarEjerciciosDesdeArchivo(ruta);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            gestor.desuscribir(this);
            dispose();
        });

        add(lblSemana);       add(spinnerSemana);
        add(lblArchivo);      add(btnSeleccionar);
        add(new JLabel(""));  add(btnCancelar);
    }

    @Override
    public void onDatosCargados(int total, int tiempoTotal, int cantCardio, int cantFuerza,
                                int cantBasico, int cantIntermedio, int cantAvanzado, int cantAlto) {
        // Carga exitosa: cierra esta ventana y la principal se actualiza sola
        JOptionPane.showMessageDialog(this,
            "¡Ejercicios cargados correctamente!\n" + total + " ejercicios disponibles.",
            "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
        gestor.desuscribir(this);
        dispose();
    }

    @Override
    public void onError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error al cargar", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onRutinaGenerada(List<Ejercicio> rutina) {
        // No aplica en esta ventana
    }
}