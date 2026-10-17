package eventos;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la version orientada a eventos del control de gastos.
 * Su unica tarea es crear la ventana y entregarle el control al usuario.
 */
public class AppGastos {

    public static void main(String[] args) {
        // Swing exige crear la interfaz desde su hilo de eventos (Event Dispatch Thread).
        SwingUtilities.invokeLater(() -> {
            VentanaGastos ventana = new VentanaGastos();
            ventana.setVisible(true);
        });
    }
}
