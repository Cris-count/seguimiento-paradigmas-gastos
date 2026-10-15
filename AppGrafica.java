import javax.swing.SwingUtilities;

/** Punto de entrada de la interfaz grafica. */
public final class AppGrafica {
    private AppGrafica() { }

    public static void main(String[] args) {
        // Swing se crea y actualiza exclusivamente en su hilo de eventos.
        SwingUtilities.invokeLater(() -> new VentanaGastos(new GestorGastos()).setVisible(true));
    }
}
