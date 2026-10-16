package eventos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal de la aplicacion.
 *
 * Los componentes se declaran como atributos de la clase para que los
 * metodos controladores de cada evento puedan leer los campos y actualizar
 * la tabla y el resultado. Cada boton tiene su propio listener y su propio
 * metodo controlador, conectados en registrarEventos().
 */
public class VentanaGastos extends JFrame {

    // Opcion inicial del combo: obliga al usuario a elegir una categoria real.
    private static final String SIN_CATEGORIA = "Seleccione una categoría";

    private static final String[] CATEGORIAS = {
        SIN_CATEGORIA, "Alimentación", "Transporte", "Servicios",
        "Entretenimiento", "Salud", "Educación", "Otros"
    };

    private static final String[] COLUMNAS = {"N.°", "Concepto", "Categoría", "Monto"};

    // Muestra los montos con separador de miles colombiano, por ejemplo 20.000.
    private static final NumberFormat FORMATO_MONTO =
            NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"));

    // Almacen de los gastos; la ventana solo lo usa, no guarda los datos por su cuenta.
    private final GestorGastos gestor = new GestorGastos();

    // Entrada de datos del gasto (funcionalidad 1).
    private final JTextField txtConcepto = new JTextField(20);
    private final JTextField txtMonto = new JTextField(10);
    private final JComboBox<String> cmbCategoria = new JComboBox<>(CATEGORIAS);

    // Datos para las consultas (funcionalidades 5 y 6).
    private final JTextField txtMontoMinimo = new JTextField(10);
    private final JTextField txtBusqueda = new JTextField(15);

    // Botones: cada uno producira un evento distinto en las siguientes etapas.
    private final JButton btnRegistrar = new JButton("Registrar gasto");
    private final JButton btnMostrarTodos = new JButton("Mostrar todos");
    private final JButton btnCalcularTotal = new JButton("Calcular total");
    private final JButton btnMayorGasto = new JButton("Mayor gasto");
    private final JButton btnFiltrar = new JButton("Filtrar por monto");
    private final JButton btnBuscar = new JButton("Buscar concepto");

    // Presentacion de resultados (funcionalidad 2 y respuestas de las demas).
    // Las celdas no se editan a mano: los gastos solo cambian mediante los botones.
    private final DefaultTableModel modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tablaGastos = new JTable(modeloTabla);
    private final JLabel lblResultado = new JLabel("Registre un gasto para comenzar.");

    public VentanaGastos() {
        super("Control de Gastos Personales - POE");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construirInterfaz();
        registrarEventos();
    }

    /**
     * Conecta cada boton con el metodo que responde a su evento.
     * La lambda es el callback: Swing la ejecuta cuando el usuario hace clic.
     */
    private void registrarEventos() {
        btnRegistrar.addActionListener(evento -> registrarGasto());
        btnMostrarTodos.addActionListener(evento -> mostrarTodos());
        btnCalcularTotal.addActionListener(evento -> calcularTotal());
        btnMayorGasto.addActionListener(evento -> mostrarMayorGasto());
        btnFiltrar.addActionListener(evento -> filtrarPorMonto());
        btnBuscar.addActionListener(evento -> buscarPorConcepto());
    }

    /**
     * Controlador del evento "Registrar gasto": lee los campos, valida los datos,
     * guarda el gasto y actualiza la interfaz.
     */
    private void registrarGasto() {
        String concepto = txtConcepto.getText().trim();
        String textoMonto = txtMonto.getText().trim();
        String categoria = (String) cmbCategoria.getSelectedItem();

        if (concepto.isEmpty()) {
            mostrarError("Escriba el concepto del gasto.", txtConcepto);
            return;
        }
        if (textoMonto.isEmpty()) {
            mostrarError("Escriba el monto del gasto.", txtMonto);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(textoMonto);
        } catch (NumberFormatException e) {
            mostrarError("El monto debe ser un número, por ejemplo 20000.", txtMonto);
            return;
        }
        if (!Double.isFinite(monto) || monto <= 0) {
            mostrarError("El monto debe ser mayor que cero.", txtMonto);
            return;
        }
        if (SIN_CATEGORIA.equals(categoria)) {
            mostrarError("Seleccione una categoría.", cmbCategoria);
            return;
        }

        gestor.registrar(new Gasto(concepto, monto, categoria));
        mostrarGastos(gestor.obtenerTodos());
        lblResultado.setText("Gasto registrado: " + concepto + " por $" + FORMATO_MONTO.format(monto) + ".");
        limpiarFormulario();
    }

    /**
     * Controlador del evento "Mostrar todos": vuelve a presentar en la tabla
     * todos los gastos guardados e informa cuantos hay.
     */
    private void mostrarTodos() {
        List<Gasto> todos = gestor.obtenerTodos();
        mostrarGastos(todos);

        if (todos.isEmpty()) {
            lblResultado.setText("No hay gastos registrados.");
        } else if (todos.size() == 1) {
            lblResultado.setText("Se muestra 1 gasto registrado.");
        } else {
            lblResultado.setText("Se muestran " + todos.size() + " gastos registrados.");
        }
    }

    /**
     * Controlador del evento "Calcular total": pide al gestor la suma de todos los
     * gastos registrados (no solo los visibles en la tabla) y la muestra en Resultado.
     */
    private void calcularTotal() {
        if (gestor.obtenerTodos().isEmpty()) {
            lblResultado.setText("No hay gastos registrados. El total es $0.");
            return;
        }
        double total = gestor.calcularTotal();
        lblResultado.setText("Gasto total acumulado: $" + FORMATO_MONTO.format(total) + ".");
    }

    /**
     * Controlador del evento "Mayor gasto": pide al gestor el gasto de mayor monto
     * entre todos los registrados y muestra su concepto, categoria y monto.
     */
    private void mostrarMayorGasto() {
        Gasto mayor = gestor.obtenerMayorGasto();
        if (mayor == null) {
            lblResultado.setText("No hay gastos registrados para comparar.");
            return;
        }
        lblResultado.setText("Mayor gasto: " + mayor.getConcepto()
                + " | Categoría: " + mayor.getCategoria()
                + " | Monto: $" + FORMATO_MONTO.format(mayor.getMonto()) + ".");
    }

    /**
     * Controlador del evento "Filtrar por monto": valida el monto minimo escrito,
     * pide al gestor los gastos que lo superan y muestra solo esos en la tabla.
     */
    private void filtrarPorMonto() {
        String textoMinimo = txtMontoMinimo.getText().trim();
        if (textoMinimo.isEmpty()) {
            mostrarError("Escriba el monto mínimo para filtrar.", txtMontoMinimo);
            return;
        }

        double montoMinimo;
        try {
            montoMinimo = Double.parseDouble(textoMinimo);
        } catch (NumberFormatException e) {
            mostrarError("El monto mínimo debe ser un número, por ejemplo 20000.", txtMontoMinimo);
            return;
        }
        // "NaN" e "Infinity" se leen como numeros en Java, pero no son montos validos.
        if (!Double.isFinite(montoMinimo)) {
            mostrarError("El monto mínimo debe ser un número, por ejemplo 20000.", txtMontoMinimo);
            return;
        }
        // Se acepta 0 (muestra todos los gastos); solo se rechazan los negativos.
        if (montoMinimo < 0) {
            mostrarError("El monto mínimo no puede ser negativo.", txtMontoMinimo);
            return;
        }

        if (gestor.obtenerTodos().isEmpty()) {
            mostrarGastos(gestor.obtenerTodos());
            lblResultado.setText("No hay gastos registrados para filtrar.");
            return;
        }

        List<Gasto> filtrados = gestor.filtrarPorMontoMayorA(montoMinimo);
        mostrarGastos(filtrados);

        String limite = "$" + FORMATO_MONTO.format(montoMinimo);
        if (filtrados.isEmpty()) {
            lblResultado.setText("Ningún gasto supera " + limite + ".");
        } else if (filtrados.size() == 1) {
            lblResultado.setText("1 gasto supera " + limite + ". Use \"Mostrar todos\" para ver la lista completa.");
        } else {
            lblResultado.setText(filtrados.size() + " gastos superan " + limite
                    + ". Use \"Mostrar todos\" para ver la lista completa.");
        }
    }

    /**
     * Controlador del evento "Buscar concepto": valida el texto escrito, pide al
     * gestor los gastos cuyo concepto lo contiene y muestra solo esos en la tabla.
     */
    private void buscarPorConcepto() {
        String texto = txtBusqueda.getText().trim();
        if (texto.isEmpty()) {
            mostrarError("Escriba una palabra o parte del concepto para buscar.", txtBusqueda);
            return;
        }

        if (gestor.obtenerTodos().isEmpty()) {
            mostrarGastos(gestor.obtenerTodos());
            lblResultado.setText("No hay gastos registrados para buscar.");
            return;
        }

        List<Gasto> encontrados = gestor.buscarPorConcepto(texto);
        mostrarGastos(encontrados);

        String buscado = "\"" + texto + "\"";
        if (encontrados.isEmpty()) {
            lblResultado.setText("Ningún gasto contiene " + buscado + " en su concepto.");
        } else if (encontrados.size() == 1) {
            lblResultado.setText("1 gasto contiene " + buscado
                    + ". Use \"Mostrar todos\" para ver la lista completa.");
        } else {
            lblResultado.setText(encontrados.size() + " gastos contienen " + buscado
                    + ". Use \"Mostrar todos\" para ver la lista completa.");
        }
    }

    /** Vuelve a llenar la tabla con la lista recibida; primero la vacia para no duplicar filas. */
    private void mostrarGastos(List<Gasto> lista) {
        modeloTabla.setRowCount(0);
        int numero = 1;
        for (Gasto gasto : lista) {
            modeloTabla.addRow(new Object[] {
                numero,
                gasto.getConcepto(),
                gasto.getCategoria(),
                "$" + FORMATO_MONTO.format(gasto.getMonto())
            });
            numero++;
        }
    }

    private void limpiarFormulario() {
        txtConcepto.setText("");
        txtMonto.setText("");
        cmbCategoria.setSelectedIndex(0);
        txtConcepto.requestFocusInWindow();
    }

    /** Avisa al usuario que un dato no es valido y lleva el cursor al campo que debe corregir. */
    private void mostrarError(String mensaje, JComponent campo) {
        JOptionPane.showMessageDialog(this, mensaje, "Dato no válido", JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
    }

    /** Organiza la ventana en tres zonas: formularios arriba, tabla al centro y resultado abajo. */
    private void construirInterfaz() {
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 10, 0));
        panelSuperior.add(crearPanelRegistro());
        panelSuperior.add(crearPanelConsultas());

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(panelSuperior, BorderLayout.NORTH);
        contenido.add(crearPanelTabla(), BorderLayout.CENTER);
        contenido.add(crearPanelResultado(), BorderLayout.SOUTH);

        setContentPane(contenido);
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar gasto"));
        panel.add(new JLabel("Concepto:"));
        panel.add(txtConcepto);
        panel.add(new JLabel("Monto:"));
        panel.add(txtMonto);
        panel.add(new JLabel("Categoría:"));
        panel.add(cmbCategoria);
        panel.add(new JLabel());
        panel.add(btnRegistrar);
        return panel;
    }

    private JPanel crearPanelConsultas() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Consultas"));
        panel.add(new JLabel("Monto mínimo:"));
        panel.add(txtMontoMinimo);
        panel.add(new JLabel("Buscar concepto:"));
        panel.add(txtBusqueda);
        panel.add(btnFiltrar);
        panel.add(btnBuscar);
        panel.add(btnCalcularTotal);
        panel.add(btnMayorGasto);
        return panel;
    }

    private JScrollPane crearPanelTabla() {
        tablaGastos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaGastos.getTableHeader().setReorderingAllowed(false);

        JScrollPane panel = new JScrollPane(tablaGastos);
        panel.setBorder(BorderFactory.createTitledBorder("Gastos registrados"));
        return panel;
    }

    private JPanel crearPanelResultado() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        panel.add(lblResultado, BorderLayout.CENTER);
        panel.add(btnMostrarTodos, BorderLayout.EAST);
        return panel;
    }
}
