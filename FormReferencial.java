
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormReferencial extends JFrame {

    private final TablaConfig config;
    private final Map<String, JTextField> campos = new LinkedHashMap<>();
    private JTextField txtEstado;

    // Grilla
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private int filaSeleccionada = -1;

    // Flag de actualización y modo actual
    private int flagActualizar = 0;
    private String modoActual = "NONE";

    // Botones
    private JButton btnAdicionar = new JButton("Adicionar");
    private JButton btnModificar = new JButton("Modificar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnCancelar = new JButton("Cancelar");
    private JButton btnInactivar = new JButton("Inactivar");
    private JButton btnReactivar = new JButton("Reactivar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnSalir = new JButton("Salir");

    public FormReferencial(TablaConfig config) {
        super(config.titulo);
        this.config = config;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(580, 480 + (config.campos.size() > 2 ? (config.campos.size() - 2) * 30 : 0));
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        cargarGrilla();
        setEstadoInicial();
    }

    private void initUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(6, 6));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // ── Panel de Registro ────────────────────────────────────────────────
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), config.subtitulo,
                TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;
        for (CampoConfig cc : config.campos) {
            JTextField txt = new JTextField(cc.esClave ? 10 : 30);
            campos.put(cc.columnaBD, txt);

            gbc.gridx = 0;
            gbc.gridy = fila;
            panelRegistro.add(new JLabel(cc.etiqueta + ":"), gbc);
            gbc.gridx = 1;
            panelRegistro.add(txt, gbc);
            fila++;
        }

        // Estado de Registro
        txtEstado = new JTextField(3);
        txtEstado.setPreferredSize(new Dimension(40, 22));
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelRegistro.add(new JLabel("Estado Registro"), gbc);
        gbc.gridx = 1;
        panelRegistro.add(txtEstado, gbc);

        //Panel de Grilla
        String[] encabezados = new String[config.campos.size() + 1];
        int i = 0;
        for (CampoConfig cc : config.campos) {
            encabezados[i++] = cc.encabezadoGrilla;
        }
        encabezados[i] = "Estado Registro";

        modeloTabla = new DefaultTableModel(encabezados, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        i = 0;
        for (CampoConfig cc : config.campos) {
            tabla.getColumnModel().getColumn(i++).setPreferredWidth(cc.anchoGrilla);
        }
        tabla.getColumnModel().getColumn(i).setPreferredWidth(100); // Estado Registro

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Tabla_" + config.titulo,
                TitledBorder.LEFT, TitledBorder.TOP));
        scroll.setPreferredSize(new Dimension(540, 140));

        // Insercion de los botones en el panelBotones
        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 6, 6));
        panelBotones.add(btnAdicionar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);
        panelBotones.add(btnInactivar);
        panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnSalir);

        // Ensamblado
        JPanel centro = new JPanel(new BorderLayout(4, 4));
        centro.add(panelRegistro, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(centro, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);

        // Eventos
        btnAdicionar.addActionListener(e -> accionAdicionar());
        btnModificar.addActionListener(e -> accionModificar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCancelar.addActionListener(e -> accionCancelar());
        btnInactivar.addActionListener(e -> accionInactivar());
        btnReactivar.addActionListener(e -> accionReactivar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnSalir.addActionListener(e -> accionSalir());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filaSeleccionada = tabla.getSelectedRow();
            }
        });
    }

    private void setEstadoInicial() {
        limpiarRegistro();
        flagActualizar = 0;
        modoActual = "NONE";
        protegerTodosLosCampos(true);
    }

    private void limpiarRegistro() {
        for (JTextField txt : campos.values()) {
            txt.setText("");
        }
        txtEstado.setText("");
        filaSeleccionada = -1;
        tabla.clearSelection();
    }

    /**
     * Bloquea y desbloquea los campos del panel de registro (incluye
     * estado de registro).
     */
    private void protegerTodosLosCampos(boolean proteger) {
        Color bgLock = new Color(220, 220, 220);
        Color bgEdit = Color.WHITE;
        for (JTextField txt : campos.values()) {
            txt.setEditable(!proteger);
            txt.setBackground(proteger ? bgLock : bgEdit);
        }
        txtEstado.setEditable(false);
        txtEstado.setBackground(bgLock); // estado SIEMPRE protegido para el usuario
    }

    /**
     * Aplica permisos de edicion campo por campo según el modo
     */
    private void aplicarPermisosPorModo(boolean esModoAdicionar) {
        Color bgLock = new Color(220, 220, 220);
        Color bgEdit = Color.WHITE;
        for (CampoConfig cc : config.campos) {
            JTextField txt = campos.get(cc.columnaBD);
            boolean editable = esModoAdicionar ? cc.editableEnAdicionar : cc.editableEnModificar;
            txt.setEditable(editable);
            txt.setBackground(editable ? bgEdit : bgLock);
        }
        txtEstado.setEditable(false);
        txtEstado.setBackground(bgLock);
    }

    // Aqui lo único que cambia entre tablas es esta consulta que esconstruida automáticamente a partir de config.campos

    private void cargarGrilla() {
        modeloTabla.setRowCount(0);

        StringBuilder sql = new StringBuilder("SELECT ");
        for (CampoConfig cc : config.campos) {
            sql.append(cc.columnaBD).append(", ");
        }
        sql.append(config.columnaEstado)
                .append(" FROM ").append(config.tabla)
                .append(" ORDER BY ").append(config.campoClave().columnaBD);

        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                Object[] fila = new Object[config.campos.size() + 1];
                int i = 0;
                for (CampoConfig cc : config.campos) {
                    fila[i++] = rs.getString(cc.columnaBD);
                }
                fila[i] = rs.getString(config.columnaEstado);
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar datos:\n" + ex.getMessage());
        }
    }

    /**
     * ADICIONAR: blanquea registro, estado = A, habilita campos según config
     */
    private void accionAdicionar() {
        limpiarRegistro();
        txtEstado.setText("A");
        aplicarPermisosPorModo(true);
        campos.get(config.campoClave().columnaBD).requestFocus();
        flagActualizar = 1;
        modoActual = "ADD";
    }

    /**
     * MODIFICAR: carga fila seleccionada; habilita solo campos editables en modificacion
     */
    private void accionModificar() {
        if (!hayFilaSeleccionada()) {
            return;
        }
        cargarFilaEnRegistro();
        aplicarPermisosPorModo(false);
        flagActualizar = 1;
        modoActual = "MOD";
    }

    /**
     * ELIMINAR: carga fila, todo protegido, estado Es *
     */
    private void accionEliminar() {
        if (!hayFilaSeleccionada()) {
            return;
        }
        cargarFilaEnRegistro();
        txtEstado.setText("*");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "DEL";
    }

    /**
     * INACTIVAR: carga fila, todo protegido, estado es I
     */
    private void accionInactivar() {
        if (!hayFilaSeleccionada()) {
            return;
        }
        cargarFilaEnRegistro();
        txtEstado.setText("I");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "INA";
    }

    /**
     * REACTIVAR: carga fila, todo protegido, estado es A
     */
    private void accionReactivar() {
        if (!hayFilaSeleccionada()) {
            return;
        }
        cargarFilaEnRegistro();
        txtEstado.setText("A");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "REA";
    }

    /**
     * ACTUALIZAR: verifica flagActualizar y ejecuta la operacion
     * correspondiente
     */
    private void accionActualizar() {
        if (flagActualizar == 0) {
            JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado un comando para actualizar un registro de la BD.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que ningún campo esté vacío
        for (CampoConfig cc : config.campos) {
            if (campos.get(cc.columnaBD).getText().trim().isEmpty()) {
                mostrarError("El campo \"" + cc.etiqueta + "\" no puede estar vacio.");
                return;
            }
        }

        try (Connection con = Conexion.getConexion()) {
            switch (modoActual) {
                case "ADD":
                    ejecutarInsert(con);
                    break;
                case "MOD":
                    ejecutarUpdate(con);
                    break;
                case "DEL":
                case "INA":
                case "REA":
                    ejecutarEstado(con);
                    break;
            }
            cargarGrilla();
            setEstadoInicial();
        } catch (Exception ex) {
            mostrarError("Error al actualizar:\n" + ex.getMessage());
        }
    }

    /**
     * CANCELAR: borra registro y vuelve al estado inicial
     */
    private void accionCancelar() {
        setEstadoInicial();
    }

    /**
     * SALIR: cierra el programa
     */
    private void accionSalir() {
        accionCancelar();
        dispose();
        System.exit(0);
    }

    private void ejecutarInsert(Connection con) throws SQLException {
        StringBuilder cols = new StringBuilder();
        StringBuilder qs = new StringBuilder();
        for (CampoConfig cc : config.campos) {
            cols.append(cc.columnaBD).append(", ");
            qs.append("?, ");
        }
        cols.append(config.columnaEstado);
        qs.append("?");

        String sql = "INSERT INTO " + config.tabla + " (" + cols + ") VALUES (" + qs + ")";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int idx = 1;
            for (CampoConfig cc : config.campos) {
                ps.setString(idx++, campos.get(cc.columnaBD).getText().trim());
            }
            ps.setString(idx, txtEstado.getText().trim());
            ps.executeUpdate();
        }
    }

    private void ejecutarUpdate(Connection con) throws SQLException {
        StringBuilder set = new StringBuilder();
        for (CampoConfig cc : config.campos) {
            if (cc.editableEnModificar) {
                set.append(cc.columnaBD).append(" = ?, ");
            }
        }
        if (set.length() == 0) {
            return; // nada editable

                }set.setLength(set.length() - 2); // quita la ultima coma

        String sql = "UPDATE " + config.tabla + " SET " + set
                + " WHERE " + config.campoClave().columnaBD + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int idx = 1;
            for (CampoConfig cc : config.campos) {
                if (cc.editableEnModificar) {
                    ps.setString(idx++, campos.get(cc.columnaBD).getText().trim());
                }
            }
            ps.setString(idx, campos.get(config.campoClave().columnaBD).getText().trim());
            ps.executeUpdate();
        }
    }

    private void ejecutarEstado(Connection con) throws SQLException {
        String sql = "UPDATE " + config.tabla + " SET " + config.columnaEstado + " = ?"
                + " WHERE " + config.campoClave().columnaBD + " = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtEstado.getText().trim());
            ps.setString(2, campos.get(config.campoClave().columnaBD).getText().trim());
            ps.executeUpdate();
        }
    }

    private void cargarFilaEnRegistro() {
        int i = 0;
        for (CampoConfig cc : config.campos) {
            campos.get(cc.columnaBD).setText(modeloTabla.getValueAt(filaSeleccionada, i++).toString());
        }
        txtEstado.setText(modeloTabla.getValueAt(filaSeleccionada, i).toString());
    }

    private boolean hayFilaSeleccionada() {
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccione un registro.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
