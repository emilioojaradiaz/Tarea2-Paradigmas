package frontend.gui;

import backend.modelo.Ejercicio;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class VentanaRevision extends JFrame {
    private List<Ejercicio> rutina;
    private int indiceActual;

    private JLabel lblNombre, lblTipo, lblIntensidad, lblTiempo, lblContador;
    private JTextArea txtDescripcion;
    private JButton btnVolver, btnSiguiente, btnCancelar;

    public VentanaRevision(List<Ejercicio> rutina) {
        this.rutina = rutina;
        this.indiceActual = 0;

        setTitle("Revisión de la Rutina");
        setSize(430, 340);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior con contador
        lblContador = new JLabel("", SwingConstants.CENTER);
        lblContador.setFont(new Font("Arial", Font.BOLD, 12));
        lblContador.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Panel central con los datos del ejercicio
        JPanel panelDatos = new JPanel(new GridLayout(4, 1, 2, 2));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblNombre     = new JLabel();
        lblTipo       = new JLabel();
        lblIntensidad = new JLabel();
        lblTiempo     = new JLabel();
        panelDatos.add(lblNombre);
        panelDatos.add(lblTipo);
        panelDatos.add(lblIntensidad);
        panelDatos.add(lblTiempo);

        txtDescripcion = new JTextArea();
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(lblContador, BorderLayout.NORTH);
        panelCentro.add(panelDatos, BorderLayout.CENTER);
        panelCentro.add(new JScrollPane(txtDescripcion), BorderLayout.SOUTH);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnVolver    = new JButton("← Anterior");
        btnSiguiente = new JButton("Siguiente →");
        // FIX 5: Botón cancelar para salir de la revisión en cualquier momento
        btnCancelar  = new JButton("Cancelar");

        btnVolver.addActionListener(e -> navegar(-1));
        btnSiguiente.addActionListener(e -> navegar(1));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnVolver);
        panelBotones.add(btnSiguiente);

        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarVista();
    }

    private void navegar(int direccion) {
        if (direccion == 1 && indiceActual == rutina.size() - 1) {
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
        lblContador.setText("Ejercicio " + (indiceActual + 1) + " de " + rutina.size());
        lblNombre.setText("  Ejercicio: " + ej.getNombre());
        lblTipo.setText("  Tipo: " + ej.getTipo());
        lblIntensidad.setText("  Intensidad: " + ej.getNivelIntensidad());
        lblTiempo.setText("  Tiempo: " + ej.getTiempoMinutos() + " min");
        txtDescripcion.setText("Descripción:\n" + ej.getDescripcion());

        btnVolver.setEnabled(indiceActual > 0);

        if (indiceActual == rutina.size() - 1) {
            btnSiguiente.setText("Ver Resumen ✓");
        } else {
            btnSiguiente.setText("Siguiente →");
        }
    }
}
