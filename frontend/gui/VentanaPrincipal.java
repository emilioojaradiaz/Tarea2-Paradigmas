package frontend.gui;

import backend.logica.GestorRutinas;
import backend.eventos.Suscriptor;
import backend.modelo.Ejercicio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame implements Suscriptor {
    private GestorRutinas gestor;
    private JLabel lblEstado;
    private JButton btnGenerarRutina;

    public VentanaPrincipal(GestorRutinas gestor) {
        this.gestor = gestor;
        this.gestor.suscribir(this);
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle("UNAB - Gestión de Rutinas Personalizadas");
        setSize(580, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Panel superior: carga de archivo + semana actual
        JPanel panelCarga = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnCargar = new JButton("Seleccionar Archivo de Ejercicios");
        btnCargar.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            int resultado = selector.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String ruta = selector.getSelectedFile().getAbsolutePath();
                gestor.cargarEjerciciosDesdeArchivo(ruta);
            }
        });

        // FIX 1: Campo para ingresar la semana actual
        JLabel lblSemana = new JLabel("Semana actual:");
        JSpinner spinnerSemana = new JSpinner(new SpinnerNumberModel(1, 1, 52, 1));
        spinnerSemana.setPreferredSize(new Dimension(60, 28));
        spinnerSemana.addChangeListener(e -> {
            gestor.setSemanaActual((int) spinnerSemana.getValue());
        });

        panelCarga.add(btnCargar);
        panelCarga.add(lblSemana);
        panelCarga.add(spinnerSemana);

        lblEstado = new JLabel(
            "<html><center>Por favor, cargue el archivo de ejercicios<br>para comenzar.</center></html>",
            SwingConstants.CENTER
        );
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 14));

        btnGenerarRutina = new JButton("Generar Rutina de Entrenamiento");
        btnGenerarRutina.setEnabled(false);
        btnGenerarRutina.setPreferredSize(new Dimension(220, 40));
        btnGenerarRutina.addActionListener(e -> {
            // FIX 2: VentanaGeneracion implementa Suscriptor para manejar errores localmente
            VentanaGeneracion vg = new VentanaGeneracion(gestor);
            vg.setVisible(true);
        });

        add(panelCarga, BorderLayout.NORTH);
        add(lblEstado, BorderLayout.CENTER);
        add(btnGenerarRutina, BorderLayout.SOUTH);
    }

    @Override
    public void onDatosCargados(int total, int tiempoTotal, int cantCardio, int cantFuerza,
                                int cantBasico, int cantIntermedio, int cantAvanzado, int cantAlto) {
        String resumen = String.format(
            "<html><div style='text-align: center; border: 1px solid gray; padding: 10px;'>" +
            "<h3>Estadísticas de Ejercicios Cargados</h3>" +
            "<b>Total Ejercicios:</b> %d<br>" +
            "<b>Tiempo Total Disponible:</b> %d min<br><br>" +
            "<b>Por Tipo:</b> Cardio (%d) - Fuerza (%d)<br>" +
            "<b>Por Intensidad:</b> Básico (%d) - Intermedio (%d) - Avanzado (%d) - Alto (%d)" +
            "</div></html>",
            total, tiempoTotal, cantCardio, cantFuerza,
            cantBasico, cantIntermedio, cantAvanzado, cantAlto
        );
        lblEstado.setText(resumen);
        btnGenerarRutina.setEnabled(true);
    }

    @Override
    public void onError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error en el Sistema", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void onRutinaGenerada(List<Ejercicio> rutina) {
        VentanaRevision vr = new VentanaRevision(rutina);
        vr.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorRutinas logica = new GestorRutinas();
            VentanaPrincipal vp = new VentanaPrincipal(logica);
            vp.setVisible(true);
        });
    }
}
