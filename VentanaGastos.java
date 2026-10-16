import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

/** Vista de escritorio: delega las operaciones de negocio en GestorGastos. */
public final class VentanaGastos extends JFrame {
    private static final long serialVersionUID = 1L;
    private final GestorGastos gestor;
    private final ModeloTablaGastos modelo = new ModeloTablaGastos();
    private final JTextField concepto = new JTextField();
    private final JTextField monto = new JTextField();
    private final JComboBox<String> categoria = new JComboBox<>(new String[] {
            "Alimentación", "Transporte", "Servicios", "Entretenimiento", "Salud", "Otros"});
    private final JTextField busqueda = new JTextField(18);
    private final JTextField umbral = new JTextField(10);
    private final JLabel total = new JLabel();
    private final JLabel estado = new JLabel(" ");

    public VentanaGastos(GestorGastos gestor) {
        super("Sistema de Control de Gastos Personales");
        this.gestor = gestor;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setSize(1120, 720);
        JPanel contenido = new JPanel(new BorderLayout(20, 20));
        contenido.setBorder(new EmptyBorder(24, 24, 24, 24));
        contenido.setBackground(new Color(244, 247, 251));
        setContentPane(contenido);

        JPanel cabecera = new JPanel(new BorderLayout(0, 8));
        cabecera.setOpaque(false);
        JLabel titulo = new JLabel("Control de gastos personales");
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(new JLabel("Registra tus gastos y consulta tus movimientos en un solo lugar."), BorderLayout.CENTER);
        total.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        total.setForeground(new Color(24, 100, 130));
        cabecera.add(total, BorderLayout.SOUTH);
        contenido.add(cabecera, BorderLayout.NORTH);
        contenido.add(crearFormulario(), BorderLayout.WEST);
        contenido.add(crearListado(), BorderLayout.CENTER);
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.add(new JLabel("Los datos se conservan solo durante esta sesión."), BorderLayout.WEST);
        pie.add(boton("Salir", this::dispose), BorderLayout.EAST);
        contenido.add(pie, BorderLayout.SOUTH);
        mostrarTodos();
        setLocationRelativeTo(null);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(270, 400));
        JLabel titulo = new JLabel("Nuevo gasto");
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titulo);
        agregarCampo(panel, "Concepto", concepto);
        agregarCampo(panel, "Monto ($)", monto);
        categoria.setEditable(true);
        agregarCampo(panel, "Categoría", categoria);
        JLabel ayuda = new JLabel("Ejemplo: 1250,50 o 1250.50");
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(ayuda);
        panel.add(Box.createVerticalStrut(20));
        JButton registrar = boton("Registrar gasto", this::registrar);
        registrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(registrar);
        panel.add(Box.createVerticalGlue());
        getRootPane().setDefaultButton(registrar);
        return panel;
    }

    private void agregarCampo(JPanel panel, String nombre, JComponent campo) {
        panel.add(Box.createVerticalStrut(16));
        JLabel etiqueta = new JLabel(nombre);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(etiqueta);
        panel.add(Box.createVerticalStrut(6));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        panel.add(campo);
    }

    private JPanel crearListado() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        JPanel filtros = new JPanel(new GridLayout(2, 1, 0, 6));
        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscar.add(new JLabel("Concepto:"));
        buscar.add(busqueda);
        buscar.add(boton("Buscar", () -> mostrar(gestor.buscarPorConcepto(busqueda.getText()), "Búsqueda")));
        busqueda.addActionListener(e -> ejecutar(() -> mostrar(gestor.buscarPorConcepto(busqueda.getText()), "Búsqueda")));
        JPanel filtrar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtrar.add(new JLabel("Mayor a $"));
        filtrar.add(umbral);
        filtrar.add(boton("Filtrar", () -> mostrar(gestor.filtrarMayoresA(leerImporte(umbral.getText())), "Filtro por monto")));
        filtrar.add(boton("Mayor gasto", () -> mostrar(gestor.obtenerGastosDeMayorValor(), "Gastos de mayor valor")));
        filtrar.add(boton("Ver todos", this::mostrarTodos));
        filtros.add(buscar);
        filtros.add(filtrar);
        panel.add(filtros, BorderLayout.NORTH);
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(32);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.setDefaultRenderer(BigDecimal.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override protected void setValue(Object valor) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setText(valor == null ? "" : moneda((BigDecimal) valor));
            }
        });
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(estado, BorderLayout.SOUTH);
        return panel;
    }

    private JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> ejecutar(accion));
        return boton;
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Revisa los datos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void registrar() {
        Object seleccion = categoria.getEditor().getItem();
        gestor.registrarGasto(concepto.getText(), leerImporte(monto.getText()),
                seleccion == null ? "" : seleccion.toString());
        concepto.setText("");
        monto.setText("");
        mostrarTodos();
        estado.setText("Gasto registrado correctamente. " + modelo.getRowCount() + " gastos en total.");
        concepto.requestFocusInWindow();
    }

    private BigDecimal leerImporte(String texto) {
        String entrada = texto.strip();
        if (!entrada.matches("[+-]?[0-9]+([.,][0-9]{1,2})?")) {
            throw new IllegalArgumentException("Ingresa un monto con hasta dos decimales, sin separadores de miles.");
        }
        return new BigDecimal(entrada.replace(',', '.'));
    }

    private void mostrarTodos() {
        mostrar(gestor.listarGastos(), "Todos los gastos");
    }

    private void mostrar(List<Gasto> gastos, String contexto) {
        modelo.actualizar(gastos);
        total.setText("Total acumulado: " + moneda(gestor.calcularTotal()));
        estado.setText(contexto + " · " + gastos.size() + " resultados"
                + (gastos.isEmpty() ? " · No hay gastos para mostrar." : ""));
    }

    private static String moneda(BigDecimal valor) {
        return String.format(Locale.US, "$ %,.2f", valor);
    }
}
