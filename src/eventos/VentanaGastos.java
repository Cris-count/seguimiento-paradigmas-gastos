package eventos;

import javax.swing.JFrame;

/**
 * Ventana principal de la aplicacion. En las siguientes etapas aqui se
 * agregaran los componentes graficos y los listeners que responden a cada evento.
 */
public class VentanaGastos extends JFrame {

    public VentanaGastos() {
        super("Control de Gastos Personales - POE");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
