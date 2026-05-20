package frontend.gui;

import backend.modelo.Ejercicio;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class VentanaRevision extends JFrame {
    private List<Ejercicio> rutina;
    private int indiceActual;

    private JLabel lblNombre, lblTipo, lblIntensidad, lblTiempo;
    private JTextArea txtDescripcion;
    private JButton btnVolver, btnSiguiente;

    public VentanaRevision(List<Ejercicio> rutina) {
        this.rutina = rutina;
        this.indiceActual = 0;

        setTitle("Revisión de la Rutina");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel central con los datos del ejercicio
        JPanel panelDatos = new JPanel(new GridLayout(4, 1));
        lblNombre = new JLabel();
        lblTipo = new JLabel();
        lblIntensidad = new JLabel();
        lblTiempo = new JLabel();
        
        panelDatos.add(lblNombre);
        panelDatos.add(lblTipo);
        panelDatos.add(lblIntensidad);
        panelDatos.add(lblTiempo);

        txtDescripcion = new JTextArea();
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelDatos, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(txtDescripcion), BorderLayout.CENTER);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel inferior con los botones
        JPanel panelBotones = new JPanel();
        btnVolver = new JButton("Volver");
        btnSiguiente = new JButton("Siguiente");

        btnVolver.addActionListener(e -> navegar(-1));
        btnSiguiente.addActionListener(e -> navegar(1));

        panelBotones.add(btnVolver);
        panelBotones.add(btnSiguiente);

        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargamos el primer ejercicio
        actualizarVista();
    }

    private void navegar(int direccion) {
        if (direccion == 1 && indiceActual == rutina.size() - 1) {
            // Si le dio a "Resumen de la rutina"
            VentanaResumen vr = new VentanaResumen(rutina);
            vr.setVisible(true);
            dispose();
        } else {
            indiceActual += direccion;
            actualizarVista();
        }
    }

    private void actualizarVista() {
        Ejercicio ej = rutina.get(indiceActual);
        lblNombre.setText(" Ejercicio: " + ej.getNombre());
        lblTipo.setText(" Tipo: " + ej.getTipo());
        lblIntensidad.setText(" Intensidad: " + ej.getNivelIntensidad());
        lblTiempo.setText(" Tiempo: " + ej.getTiempoMinutos() + " min");
        txtDescripcion.setText("Descripción:\n" + ej.getDescripcion());

        // Lógica de habilitar/deshabilitar botones
        btnVolver.setEnabled(indiceActual > 0);

        if (indiceActual == rutina.size() - 1) {
            btnSiguiente.setText("Resumen de la rutina");
        } else {
            btnSiguiente.setText("Siguiente");
        }
    }
}