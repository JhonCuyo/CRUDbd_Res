import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main extends JFrame {

    public Main() {
        setTitle("Sistema Integral Residuos Sólidos - Todas las Tablas");
        setSize(1000, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("Toxicidad", new FormReferencial(nivelToxicidad()));
        tabbedPane.addTab("Envases", new FormReferencial(tipoEnvase()));
        tabbedPane.addTab("Tratamientos", new FormReferencial(tipoTratamiento()));
        tabbedPane.addTab("Transportes", new FormReferencial(tipoTransporte()));
        tabbedPane.addTab("Regiones", new FormReferencial(region()));
        tabbedPane.addTab("Res. Estándar", new FormReferencial(residuoEstandar()));
        tabbedPane.addTab("Constituyentes", new FormReferencial(constituyente()));

        tabbedPane.addTab("Emp. Productoras", new FormReferencial(empresaProductora()));
        tabbedPane.addTab("Emp. Transportistas", new FormReferencial(empresaTransportista()));
        tabbedPane.addTab("Destinos", new FormReferencial(destino()));
        tabbedPane.addTab("Catálogo Residuos", new FormReferencial(residuo()));

        tabbedPane.addTab("Traslados", new FormReferencial(traslado()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private static TablaConfig nivelToxicidad() {
        return new TablaConfig("TR_Nivel_Toxicidad", "TOXICIDAD", "Registro de Nivel", "estReg", Arrays.asList(
            new CampoConfig("ID_Toxicidad", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nivel", "Nivel", "Nivel", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripción", "Descripción", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoEnvase() {
        return new TablaConfig("TR_Tipo_Envase", "ENVASES", "Registro Envase", "estReg", Arrays.asList(
            new CampoConfig("ID_Envase", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Envase", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripción", "Descripción", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoTratamiento() {
        return new TablaConfig("TR_Tipo_Tratamiento", "TRATAMIENTOS", "Registro Tratamiento", "estReg", Arrays.asList(
            new CampoConfig("ID_Tratamiento", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Tratamiento", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripción", "Descripción", true, true, false, 250)
        ));
    }

    private static TablaConfig tipoTransporte() {
        return new TablaConfig("TR_Tipo_Transporte", "TRANSPORTE", "Registro Tipo Transporte", "estReg", Arrays.asList(
            new CampoConfig("ID_Tipo_Transporte", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Transporte", "Nombre", "Nombre", true, true, false, 150),
            new CampoConfig("Descripcion", "Descripción", "Descripción", true, true, false, 250)
        ));
    }

    private static TablaConfig region() {
        return new TablaConfig("Region", "REGIONES", "Registro Regiones", "estReg", Arrays.asList(
            new CampoConfig("ID_Region", "ID", "ID", true, false, true, 60),
            new CampoConfig("Nombre_Region", "Nombre", "Región", true, true, false, 250)
        ));
    }

    private static TablaConfig residuoEstandar() {
        return new TablaConfig("Residuo_Estandarizado", "RES. ESTANDAR", "Registro Estandarizado", "estReg", Arrays.asList(
            new CampoConfig("Cod_Estandar", "Código", "Cod", true, false, true, 60),
            new CampoConfig("Nombre_Estandar", "Nombre", "Nombre", true, true, false, 250)
        ));
    }

    private static TablaConfig constituyente() {
        return new TablaConfig("Constituyente", "CONSTITUYENTES", "Registro de Constituyente", "estReg", Arrays.asList(
            new CampoConfig("Cod_Constituyente", "Código", "Cod", true, false, true, 80),
            new CampoConfig("Nombre_Constituyente", "Nombre", "Nombre", true, true, false, 200),
            new CampoConfig("Otros_Datos", "Otros", "Otros", true, true, false, 200)
        ));
    }

    // --- TABLAS MAESTRAS ---
    private static TablaConfig empresaProductora() {
        return new TablaConfig("Empresa_Productora", "PRODUCCTORAS", "Empresas Productoras", "estReg", Arrays.asList(
            new CampoConfig("NIF_Empresa", "NIF", "NIF", true, false, true, 100),
            new CampoConfig("Nombre_Empresa", "Razón Social", "Nombre", true, true, false, 180),
            new CampoConfig("Ciudad_Empresa", "Ciudad", "Ciudad", true, true, false, 100),
            new CampoConfig("Actividad", "Actividad", "Actividad", true, true, false, 120)
        ));
    }

    private static TablaConfig empresaTransportista() {
        return new TablaConfig("Empresa_Transportista", "TRANSPORTISTAS", "Empresas de Transporte", "estReg", Arrays.asList(
            new CampoConfig("NIF_Transportista", "NIF", "NIF", true, false, true, 100),
            new CampoConfig("Nombre_Transportista", "Razón Social", "Nombre", true, true, false, 180),
            new CampoConfig("Ciudad_Transportista", "Ciudad", "Ciudad", true, true, false, 100)
        ));
    }

    private static TablaConfig destino() {
        return new TablaConfig("Destino", "DESTINOS", "Instalaciones de Destino", "estReg", Arrays.asList(
            new CampoConfig("Cod_Destino", "Cod Destino", "Código", true, false, true, 80),
            new CampoConfig("ID_Region", "Región", "Región", true, true, false, 80, "Region", "ID_Region", "Nombre_Region"),
            new CampoConfig("Nombre_Destino", "Planta", "Nombre", true, true, false, 150),
            new CampoConfig("Ciudad_Destino", "Ciudad", "Ciudad", true, true, false, 100),
            new CampoConfig("Capacidad_Maxima", "Cap Max", "Cap Max", true, true, false, 80),
            new CampoConfig("Capacidad_Actual", "Cap Act", "Cap Act", true, true, false, 80)
        ));
    }

    private static TablaConfig residuo() {
        return new TablaConfig("Residuo", "RESIDUOS", "Maestro de Residuos", "estReg", Arrays.asList(
            new CampoConfig("Cod_Residuo", "Cod Residuo", "Cod Res", true, false, true, 80),
            new CampoConfig("NIF_Empresa", "Productora", "Empresa", true, true, false, 100, "Empresa_Productora", "NIF_Empresa", "Nombre_Empresa"),
            new CampoConfig("Cod_Estandar", "Estándar", "Estándar", true, true, false, 80, "Residuo_Estandarizado", "Cod_Estandar", "Nombre_Estandar"),
            new CampoConfig("ID_Toxicidad", "Toxicidad", "Toxicidad", true, true, false, 80, "TR_Nivel_Toxicidad", "ID_Toxicidad", "Nivel"),
            new CampoConfig("Cantidad_Total", "Cantidad", "Cant", true, true, false, 80)
        ));
    }

    private static TablaConfig traslado() {
        return new TablaConfig("Traslado", "TRASLADOS", "Movimientos de Residuos", "estReg", Arrays.asList(
            new CampoConfig("ID_Traslado", "Nro Traslado", "Nro.", true, false, true, 50),
            new CampoConfig("NIF_Empresa", "Empresa", "Empresa", true, true, false, 80, "Empresa_Productora", "NIF_Empresa", "Nombre_Empresa"),
            new CampoConfig("Cod_Residuo", "Residuo", "Residuo", true, true, false, 80, "Residuo", "Cod_Residuo", "Cod_Residuo"),
            new CampoConfig("Cod_Destino", "Destino", "Destino", true, true, false, 80, "Destino", "Cod_Destino", "Nombre_Destino"),
            new CampoConfig("Fecha_Envio", "F. Envío (YYYY-MM-DD)", "F. Env", true, true, false, 80),
            new CampoConfig("Cantidad_Trasladada", "Cantidad", "Cant", true, true, false, 60),
            new CampoConfig("ID_Envase", "Envase", "Envase", true, true, false, 80, "TR_Tipo_Envase", "ID_Envase", "Nombre_Envase"),
            new CampoConfig("ID_Tratamiento", "Tratamiento", "Trat", true, true, false, 80, "TR_Tipo_Tratamiento", "ID_Tratamiento", "Nombre_Tratamiento")
        ));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}