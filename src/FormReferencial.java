import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FormReferencial extends JPanel {

    private final TablaConfig config;
    private final Map<String, JComponent> campos = new LinkedHashMap<>();
    private JTextField txtEstado;

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private int filaSeleccionada = -1;
    private int flagActualizar = 0;
    private String modoActual = "NONE";

    private JButton btnAdicionar = new JButton("Adicionar");
    private JButton btnModificar = new JButton("Modificar");
    private JButton btnEliminar = new JButton("Eliminar");
    private JButton btnCancelar = new JButton("Cancelar");
    private JButton btnInactivar = new JButton("Inactivar");
    private JButton btnReactivar = new JButton("Reactivar");
    private JButton btnActualizar = new JButton("Actualizar");
    private JButton btnSalir = new JButton("Salir");

    public FormReferencial(TablaConfig config) {
        this.config = config;
        setLayout(new BorderLayout());
        initUI();
        cargarGrilla();
        setEstadoInicial();
    }

    private void initUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(6, 6));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), config.subtitulo,
                TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;
        for (CampoConfig cc : config.campos) {
            gbc.gridx = 0;
            gbc.gridy = fila;
            panelRegistro.add(new JLabel(cc.etiqueta + ":"), gbc);
            gbc.gridx = 1;

            if (cc.esFK) {
                JComboBox<String> combo = new JComboBox<>();
                combo.setEditable(true); 
                combo.setPreferredSize(new Dimension(250, 25));
                cargarOpcionesFK(combo, cc);
                campos.put(cc.columnaBD, combo);
                panelRegistro.add(combo, gbc);
            } else {
                JTextField txt = new JTextField(cc.esClave ? 10 : 25);
                campos.put(cc.columnaBD, txt);
                panelRegistro.add(txt, gbc);
            }
            fila++;
        }

        txtEstado = new JTextField(3);
        txtEstado.setPreferredSize(new Dimension(40, 22));
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelRegistro.add(new JLabel("Estado Registro"), gbc);
        gbc.gridx = 1;
        panelRegistro.add(txtEstado, gbc);

        String[] encabezados = new String[config.campos.size() + 1];
        int i = 0;
        for (CampoConfig cc : config.campos) {
            encabezados[i++] = cc.encabezadoGrilla;
        }
        encabezados[i] = "Estado";

        modeloTabla = new DefaultTableModel(encabezados, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);

        i = 0;
        for (CampoConfig cc : config.campos) {
            tabla.getColumnModel().getColumn(i++).setPreferredWidth(cc.anchoGrilla);
        }
        tabla.getColumnModel().getColumn(i).setPreferredWidth(60);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Tabla_" + config.titulo,
                TitledBorder.LEFT, TitledBorder.TOP));
        scroll.setPreferredSize(new Dimension(650, 200));

        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 6, 6));
        panelBotones.add(btnAdicionar); panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);  panelBotones.add(btnCancelar);
        panelBotones.add(btnInactivar); panelBotones.add(btnReactivar);
        panelBotones.add(btnActualizar);panelBotones.add(btnSalir);

        JPanel centro = new JPanel(new BorderLayout(4, 4));
        centro.add(panelRegistro, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        panelPrincipal.add(centro, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> accionAdicionar());
        btnModificar.addActionListener(e -> accionModificar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnCancelar.addActionListener(e -> accionCancelar());
        btnInactivar.addActionListener(e -> accionInactivar());
        btnReactivar.addActionListener(e -> accionReactivar());
        btnActualizar.addActionListener(e -> accionActualizar());
        btnSalir.addActionListener(e -> System.exit(0));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) filaSeleccionada = tabla.getSelectedRow();
        });
    }

    private String getValorCampo(CampoConfig cc) {
        JComponent comp = campos.get(cc.columnaBD);
        if (comp instanceof JTextField) {
            return ((JTextField) comp).getText().trim();
        } else if (comp instanceof JComboBox) {
            Object item = ((JComboBox<?>) comp).getSelectedItem();
            if (item == null) return "";
            String str = item.toString().trim();
            if (str.contains(" | ")) return str.split(" \\| ")[0].trim();
            return str;
        }
        return "";
    }

    private void setValorCampo(CampoConfig cc, String valorABuscar) {
        JComponent comp = campos.get(cc.columnaBD);
        if (comp instanceof JTextField) {
            ((JTextField) comp).setText(valorABuscar == null ? "" : valorABuscar.trim());
        } else if (comp instanceof JComboBox) {
            JComboBox<String> combo = (JComboBox<String>) comp;
            if (valorABuscar == null) {
                combo.setSelectedIndex(0);
                return;
            }
            String val = valorABuscar.trim();
            boolean encontrado = false;
            for (int i = 0; i < combo.getItemCount(); i++) {
                Object itemObj = combo.getItemAt(i);
                if (itemObj == null) continue;
                String item = itemObj.toString().trim();
                
                if (item.contains(" | ")) {
                    String codigoItem = item.split(" \\| ")[0].trim();
                    if (codigoItem.equalsIgnoreCase(val)) {
                        combo.setSelectedIndex(i);
                        encontrado = true;
                        break;
                    }
                } else if (item.equalsIgnoreCase(val)) {
                    combo.setSelectedIndex(i);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) combo.setSelectedItem(valorABuscar);
        }
    }

    private void cargarOpcionesFK(JComboBox<String> combo, CampoConfig cc) {
        String sql = "SELECT " + cc.colValorFK + ", " + cc.colMostrarFK + " FROM " + cc.tablaFK + " WHERE estReg = 'A'";
        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            combo.addItem(""); 
            while (rs.next()) {
                combo.addItem(rs.getString(1) + " | " + rs.getString(2));
            }
        } catch (Exception ex) {
            System.err.println("Aviso: No se pudo cargar FK para " + cc.etiqueta);
        }
    }

    private void refrescarCombosFK() {
        for (CampoConfig cc : config.campos) {
            if (cc.esFK) {
                JComponent comp = campos.get(cc.columnaBD);
                if (comp instanceof JComboBox) {
                    JComboBox<String> combo = (JComboBox<String>) comp;
                    combo.removeAllItems(); 
                    cargarOpcionesFK(combo, cc); 
                }
            }
        }
    }

    private void cargarGrilla() {
        modeloTabla.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT ");
        for (CampoConfig cc : config.campos) sql.append(cc.columnaBD).append(", ");
        
        List<CampoConfig> pks = config.camposClaves();
        StringBuilder order = new StringBuilder();
        for(int j=0; j<pks.size(); j++){
            order.append(pks.get(j).columnaBD);
            if(j < pks.size()-1) order.append(", ");
        }

        sql.append(config.columnaEstado).append(" FROM ").append(config.tabla).append(" ORDER BY ").append(order);

        try (Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                Object[] fila = new Object[config.campos.size() + 1];
                int i = 0;
                for (CampoConfig cc : config.campos) fila[i++] = rs.getString(cc.columnaBD);
                fila[i] = rs.getString(config.columnaEstado);
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar datos:\n" + ex.getMessage());
        }
    }

    private void limpiarRegistro() {
        for (CampoConfig cc : config.campos) setValorCampo(cc, "");
        txtEstado.setText("");
        filaSeleccionada = -1;
        tabla.clearSelection();
    }

    private void protegerTodosLosCampos(boolean proteger) {
        Color bgLock = new Color(220, 220, 220);
        Color bgEdit = Color.WHITE;
        for (JComponent comp : campos.values()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setEditable(!proteger);
                comp.setBackground(proteger ? bgLock : bgEdit);
            } else if (comp instanceof JComboBox) {
                comp.setEnabled(!proteger);
            }
        }
        txtEstado.setEditable(false);
        txtEstado.setBackground(bgLock);
    }

    private void aplicarPermisosPorModo(boolean esModoAdicionar) {
        Color bgLock = new Color(220, 220, 220);
        Color bgEdit = Color.WHITE;
        for (CampoConfig cc : config.campos) {
            JComponent comp = campos.get(cc.columnaBD);
            boolean editable = esModoAdicionar ? cc.editableEnAdicionar : cc.editableEnModificar;
            
            if (comp instanceof JTextField) {
                ((JTextField) comp).setEditable(editable);
                comp.setBackground(editable ? bgEdit : bgLock);
            } else if (comp instanceof JComboBox) {
                comp.setEnabled(editable);
            }
        }
        txtEstado.setEditable(false);
        txtEstado.setBackground(bgLock);
    }

    private void accionAdicionar() {
        refrescarCombosFK(); 
        limpiarRegistro();
        txtEstado.setText("A");
        aplicarPermisosPorModo(true);
        campos.get(config.camposClaves().get(0).columnaBD).requestFocus();
        flagActualizar = 1;
        modoActual = "ADD";
    }

    private void accionModificar() {
        if (!hayFilaSeleccionada()) return;
        refrescarCombosFK(); 
        cargarFilaEnRegistro();
        aplicarPermisosPorModo(false);
        flagActualizar = 1;
        modoActual = "MOD";
    }

    private void accionEliminar() {
        if (!hayFilaSeleccionada()) return;
        cargarFilaEnRegistro();
        txtEstado.setText("*");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "DEL";
    }

    private void accionInactivar() {
        if (!hayFilaSeleccionada()) return;
        cargarFilaEnRegistro();
        txtEstado.setText("I");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "INA";
    }

    private void accionReactivar() {
        if (!hayFilaSeleccionada()) return;
        cargarFilaEnRegistro();
        txtEstado.setText("A");
        protegerTodosLosCampos(true);
        flagActualizar = 1;
        modoActual = "REA";
    }

    private void accionActualizar() {
        if (flagActualizar == 0) return;
        
        if (modoActual.equals("ADD") || modoActual.equals("MOD")) {
            for (CampoConfig cc : config.campos) {
                if (getValorCampo(cc).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El campo \"" + cc.etiqueta + "\" no puede estar vacio.", "Validacion", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        try (Connection con = Conexion.getConexion()) {
            
            if (modoActual.equals("ADD")) {
                List<CampoConfig> pks = config.camposClaves();
                StringBuilder whereCheck = new StringBuilder();
                for (int j = 0; j < pks.size(); j++) {
                    whereCheck.append(pks.get(j).columnaBD).append(" = ?");
                    if (j < pks.size() - 1) whereCheck.append(" AND ");
                }
                
                String sqlCheck = "SELECT COUNT(*) FROM " + config.tabla + " WHERE " + whereCheck;
                try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                    for (int j = 0; j < pks.size(); j++) {
                        psCheck.setString(j + 1, getValorCampo(pks.get(j)));
                    }
                    try (ResultSet rsCheck = psCheck.executeQuery()) {
                        if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, "Esta llave (o combinacion) ya existe en el registro. Ingrese datos unicos.", "Registro Duplicado", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            }

            switch (modoActual) {
                case "ADD": ejecutarInsert(con); break;
                case "MOD": ejecutarUpdate(con); break;
                case "DEL": case "INA": case "REA": ejecutarEstado(con); break;
            }
            cargarGrilla(); 
            setEstadoInicial();
            JOptionPane.showMessageDialog(this, "Operacion realizada con exito.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            mostrarError("Error al actualizar:\n" + ex.getMessage());
        }
    }

    private void accionCancelar() { setEstadoInicial(); }
    private void setEstadoInicial() { limpiarRegistro(); flagActualizar = 0; modoActual = "NONE"; protegerTodosLosCampos(true); }

    private void ejecutarInsert(Connection con) throws SQLException {
        StringBuilder cols = new StringBuilder();
        StringBuilder qs = new StringBuilder();
        for (CampoConfig cc : config.campos) { cols.append(cc.columnaBD).append(", "); qs.append("?, "); }
        cols.append(config.columnaEstado); qs.append("?");
        
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO " + config.tabla + " (" + cols + ") VALUES (" + qs + ")")) {
            int idx = 1;
            for (CampoConfig cc : config.campos) ps.setString(idx++, getValorCampo(cc));
            ps.setString(idx, txtEstado.getText().trim());
            ps.executeUpdate();
        }
    }

    private void ejecutarUpdate(Connection con) throws SQLException {
        StringBuilder set = new StringBuilder();
        for (CampoConfig cc : config.campos) if (cc.editableEnModificar) set.append(cc.columnaBD).append(" = ?, ");
        if (set.length() == 0) return;
        set.setLength(set.length() - 2); 

        List<CampoConfig> pks = config.camposClaves();
        StringBuilder where = new StringBuilder();
        for (int j = 0; j < pks.size(); j++) {
            where.append(pks.get(j).columnaBD).append(" = ?");
            if (j < pks.size() - 1) where.append(" AND ");
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE " + config.tabla + " SET " + set + " WHERE " + where)) {
            int idx = 1;
            for (CampoConfig cc : config.campos) if (cc.editableEnModificar) ps.setString(idx++, getValorCampo(cc));
            for (CampoConfig pk : pks) ps.setString(idx++, getValorCampo(pk));
            ps.executeUpdate();
        }
    }

    private void ejecutarEstado(Connection con) throws SQLException {
        List<CampoConfig> pks = config.camposClaves();
        StringBuilder where = new StringBuilder();
        for (int j = 0; j < pks.size(); j++) {
            where.append(pks.get(j).columnaBD).append(" = ?");
            if (j < pks.size() - 1) where.append(" AND ");
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE " + config.tabla + " SET " + config.columnaEstado + " = ? WHERE " + where)) {
            ps.setString(1, txtEstado.getText().trim());
            int idx = 2;
            for (CampoConfig pk : pks) ps.setString(idx++, getValorCampo(pk));
            ps.executeUpdate();
        }
    }

    private void cargarFilaEnRegistro() {
        if (filaSeleccionada < 0 || filaSeleccionada >= tabla.getRowCount()) return;
        int i = 0;
        for (CampoConfig cc : config.campos) {
            Object obj = modeloTabla.getValueAt(filaSeleccionada, i++);
            setValorCampo(cc, obj == null ? "" : obj.toString());
        }
        Object estObj = modeloTabla.getValueAt(filaSeleccionada, i);
        txtEstado.setText(estObj == null ? "" : estObj.toString());
    }

    private boolean hayFilaSeleccionada() {
        if (filaSeleccionada < 0) { 
            JOptionPane.showMessageDialog(this, "Seleccione un registro.", "Aviso", JOptionPane.WARNING_MESSAGE); 
            return false; 
        }
        return true;
    }
    private void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg, "Error BD", JOptionPane.ERROR_MESSAGE); }
}