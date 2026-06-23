import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main extends JFrame {

    public Main() {
        setTitle("Sistema Integral Residuos Solidos - Todas las Tablas");
        setSize(1100, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        // TABLAS REFERENCIALES
        tabbedPane.addTab("Toxicidad", new FormReferencial(nivelToxicidad()));
        tabbedPane.addTab("Envases", new FormReferencial(tipoEnvase()));
        tabbedPane.addTab("Tratamientos", new FormReferencial(tipoTratamiento()));
        tabbedPane.addTab("Transportes", new FormReferencial(tipoTransporte()));
        tabbedPane.addTab("Regiones", new FormReferencial(region()));
        tabbedPane.addTab("Res. Estandar", new FormReferencial(residuoEstandar()));
        tabbedPane.addTab("Constituyentes", new FormReferencial(constituyente()));

        // TABLAS MAESTRAS
        tabbedPane.addTab("Emp. Productoras", new FormReferencial(empresaProductora()));
        tabbedPane.addTab("Emp. Transportistas", new FormReferencial(empresaTransportista()));
        tabbedPane.addTab("Destinos", new FormReferencial(destino()));
        tabbedPane.addTab("Residuos", new FormReferencial(residuo()));

        // TABLAS TRANSACCIONALES Y ASOCIATIVAS
        tabbedPane.addTab("Comp. Residuos", new FormReferencial(residuoConstituyente()));
        tabbedPane.addTab("Traslados", new FormReferencial(traslado()));
        tabbedPane.addTab("Rutas Transporte", new FormReferencial(trasladoTransportista()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    // TABLAS REFERENCIALES

    private static TablaConfig nivelToxicidad() {
        return new TablaConfig("TR_Nivel_Toxicidad", "TOXICIDAD", "Nivel de Toxicidad", "estReg", Arrays.asList(
            new CampoConfig("ID_Toxicidad", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nivel", "Nivel", "Nivel", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripcion", "Descripcion", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoEnvase() {
        return new TablaConfig("TR_Tipo_Envase", "ENVASES", "Tipos de Envase", "estReg", Arrays.asList(
            new CampoConfig("ID_Envase", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Envase", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripcion", "Descripcion", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoTratamiento() {
        return new TablaConfig("TR_Tipo_Tratamiento", "TRATAMIENTOS", "Tipos de Tratamiento", "estReg", Arrays.asList(
            new CampoConfig("ID_Tratamiento", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Tratamiento", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripcion", "Descripcion", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoTransporte() {
        return new TablaConfig("TR_Tipo_Transporte", "TRANSPORTE", "Tipos de Transporte", "estReg", Arrays.asList(
            new CampoConfig("ID_Tipo_Transporte", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Transporte", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripcion", "Descripcion", true, true, false, 250)
        ));
    }

    private static TablaConfig region() {
        return new TablaConfig("Region", "REGIONES", "Regiones", "estReg", Arrays.asList(
            new CampoConfig("ID_Region", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Region", "Nombre", "Region", true, true, false, 250)
        ));
    }

    private static TablaConfig residuoEstandar() {
        return new TablaConfig("Residuo_Estandarizado", "RES. ESTANDAR", "Catalogo Estandarizado", "estReg", Arrays.asList(
            new CampoConfig("Cod_Estandar", "Codigo", "Cod", true, false, true, 60),
            new CampoConfig("Nombre_Estandar", "Nombre", "Nombre", true, true, false, 250)
        ));
    }

    private static TablaConfig constituyente() {
        return new TablaConfig("Constituyente", "CONSTITUYENTES", "Elementos Constituyentes", "estReg", Arrays.asList(
            new CampoConfig("Cod_Constituyente", "Codigo", "Cod", true, false, true, 80),
            new CampoConfig("Nombre_Constituyente", "Nombre", "Nombre", true, true, false, 200),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 200)
        ));
    }

    // TABLAS MAESTRAS
    private static TablaConfig empresaProductora() {
        return new TablaConfig("Empresa_Productora", "PRODUCTORAS", "Empresas Productoras", "estReg", Arrays.asList(
            new CampoConfig("NIF_Empresa", "NIF", "NIF", true, false, true, 100),
            new CampoConfig("Nombre_Empresa", "Razon Social", "Nombre", true, true, false, 180),
            new CampoConfig("Ciudad_Empresa", "Ciudad", "Ciudad", true, true, false, 100),
            new CampoConfig("Actividad", "Actividad", "Actividad", true, true, false, 120),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 150)
        ));
    }

    private static TablaConfig empresaTransportista() {
        return new TablaConfig("Empresa_Transportista", "TRANSPORTISTAS", "Empresas de Transporte", "estReg", Arrays.asList(
            new CampoConfig("NIF_Transportista", "NIF", "NIF", true, false, true, 100),
            new CampoConfig("Nombre_Transportista", "Razon Social", "Nombre", true, true, false, 180),
            new CampoConfig("Ciudad_Transportista", "Ciudad", "Ciudad", true, true, false, 100),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 150)
        ));
    }

    private static TablaConfig destino() {
        return new TablaConfig("Destino", "DESTINOS", "Instalaciones de Destino", "estReg", Arrays.asList(
            new CampoConfig("Cod_Destino", "Cod Destino", "Codigo", true, false, true, 80),
            new CampoConfig("ID_Region", "Region", "Region", true, true, false, 80, "Region", "ID_Region", "Nombre_Region"),
            new CampoConfig("Nombre_Destino", "Planta", "Nombre", true, true, false, 150),
            new CampoConfig("Ciudad_Destino", "Ciudad", "Ciudad", true, true, false, 100),
            new CampoConfig("Capacidad_Maxima", "Cap Maxima", "Cap Max", true, true, false, 80),
            new CampoConfig("Capacidad_Actual", "Cap Actual", "Cap Act", true, true, false, 80),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 100)
        ));
    }

    private static TablaConfig residuo() {
        return new TablaConfig("Residuo", "RESIDUOS", "Catalogo de Residuos", "estReg", Arrays.asList(
            // Cod_Residuo es ahora la UNICA llave primaria
            new CampoConfig("Cod_Residuo", "Cod Residuo", "Cod Res", true, false, true, 80),
            new CampoConfig("NIF_Empresa", "Productora", "Empresa", true, true, false, 120, "Empresa_Productora", "NIF_Empresa", "Nombre_Empresa"),
            new CampoConfig("Cod_Estandar", "Estandar", "Estandar", true, true, false, 100, "Residuo_Estandarizado", "Cod_Estandar", "Nombre_Estandar"),
            new CampoConfig("ID_Toxicidad", "Toxicidad", "Toxicidad", true, true, false, 80, "TR_Nivel_Toxicidad", "ID_Toxicidad", "Nivel"),
            new CampoConfig("Cantidad_Total", "Cantidad", "Cant", true, true, false, 80),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 100)
        ));
    }

    // TABLAS TRANSACCIONALES Y ASOCIATIVAS

    private static TablaConfig residuoConstituyente() {
        return new TablaConfig("Residuo_Constituyente", "COMPOSICION", "Composicion Quimica", "estReg", Arrays.asList(
            // Llave compuesta (2 campos marcados como esClave = true)
            new CampoConfig("Cod_Residuo", "Residuo", "Residuo", true, false, true, 100, "Residuo", "Cod_Residuo", "Cod_Residuo"),
            new CampoConfig("Cod_Constituyente", "Constituyente", "Constituyente", true, false, true, 120, "Constituyente", "Cod_Constituyente", "Nombre_Constituyente"),
            new CampoConfig("Cantidad", "Cantidad", "Cant", true, true, false, 60)
        ));
    }

    private static TablaConfig traslado() {
        return new TablaConfig("Traslado", "TRASLADOS", "Movimientos de Residuos", "estReg", Arrays.asList(
            new CampoConfig("ID_Traslado", "Nro Traslado", "Nro.", true, false, true, 60),
            // NIF_Empresa ya no esta. Relacion directa solo con Residuo.
            new CampoConfig("Cod_Residuo", "Residuo", "Residuo", true, true, false, 80, "Residuo", "Cod_Residuo", "Cod_Residuo"), 
            new CampoConfig("Cod_Destino", "Destino", "Destino", true, true, false, 100, "Destino", "Cod_Destino", "Nombre_Destino"),
            new CampoConfig("Fecha_Envio", "F. Envio", "Envio", true, true, false, 80),
            new CampoConfig("Cantidad_Trasladada", "Cantidad", "Cant", true, true, false, 60),
            new CampoConfig("ID_Envase", "Envase", "Envase", true, true, false, 100, "TR_Tipo_Envase", "ID_Envase", "Nombre_Envase"),
            new CampoConfig("ID_Tratamiento", "Tratamiento", "Trat", true, true, false, 100, "TR_Tipo_Tratamiento", "ID_Tratamiento", "Nombre_Tratamiento"),
            new CampoConfig("Fecha_Llegada", "F. Llegada", "Llegada", true, true, false, 80),
            new CampoConfig("Otros_Datos", "Otros Datos", "Otros", true, true, false, 80)
        ));
    }

    private static TablaConfig trasladoTransportista() {
        return new TablaConfig("Traslado_Transportista", "RUTAS", "Transporte Asignado", "estReg", Arrays.asList(
            // Llave compuesta (2 campos marcados como esClave = true)
            new CampoConfig("ID_Traslado", "Nro Traslado", "Traslado", true, false, true, 80, "Traslado", "ID_Traslado", "ID_Traslado"),
            new CampoConfig("NIF_Transportista", "Transportista", "Transportista", true, false, true, 150, "Empresa_Transportista", "NIF_Transportista", "Nombre_Transportista"),
            new CampoConfig("ID_Tipo_Transporte", "Transporte", "Transporte", true, true, false, 120, "TR_Tipo_Transporte", "ID_Tipo_Transporte", "Nombre_Transporte"),
            new CampoConfig("Kms_Recorridos", "Kilometros", "Kms", true, true, false, 60),
            new CampoConfig("Costo", "Costo", "Costo", true, true, false, 60)
        ));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}