import java.util.Arrays;
import javax.swing.SwingUtilities;

public class Main {

    private static TablaConfig nivelTox() {
        return new TablaConfig(
            "tr_nivel_toxicidad",
            "NIVEL TOXICIDAD",  
            "Registro de Nivel de Toxicidad",
            "estReg",
            Arrays.asList(
                new CampoConfig("ID_Toxicidad", "ID Toxicidad",      "Codigo",      true,  false, true,  60),
                new CampoConfig("Nivel", "Nivel", "Nivel", true,  true,  false, 260),
                new CampoConfig("Descripcion", "Descripcion", "Descripcion", true,  true,  false, 260)
            )
        );
    }

    private static TablaConfig tipoEnvase() {
        return new TablaConfig(
            "tr_tipo_envase",
            "TIPO DE ENVASE",  
            "Registro de Tipo de Envase",
            "estReg",
            Arrays.asList(
                new CampoConfig("ID_Envase", "ID Envase", "Codigo", true,  false, true,  60),
                new CampoConfig("Nombre_Envase", "Nombre Envase", "Nombre", true,  true,  false, 260),
                new CampoConfig("Descripcion", "Descripcion", "Descripcion", true,  true,  false, 260)
            )
        );
    }
    private static TablaConfig tipoTratamiento() {
        return new TablaConfig(
            "tr_tipo_tratamiento",
            "TIPO TRATAMIENTO",  
            "Registro de Tipo de Tratamiento",
            "estReg",
            Arrays.asList(
                new CampoConfig("ID_Tratamiento", "ID Tratamiento", "Codigo",      true,  false, true,  60),
                new CampoConfig("Nombre_Tratamiento", "Nombre Tratamiento", "Nombre", true,  true,  false, 260),
                new CampoConfig("Descripcion", "Descripcion", "Descripcion", true,  true,  false, 260)
            )
        );
    }
    private static TablaConfig tipoTransporte() {
        return new TablaConfig(
            "tr_tipo_transporte",
            "TIPO TRANSPORTE",  
            "Registro de Tipo de Transporte",
            "estReg",
            Arrays.asList(
                new CampoConfig("ID_Tipo_Transporte", "ID Tipo", "Codigo",      true,  false, true,  60),
                new CampoConfig("Nombre_Transporte", "Nombre transporte", "Nombre", true,  true,  false, 260),
                new CampoConfig("Descripcion", "Descripcion", "Descripcion", true,  true,  false, 260)
            )
        );
    }

    public static void main(String[] args) {
        TablaConfig niveltox = nivelTox();
        TablaConfig envase = tipoEnvase();
        TablaConfig tratamiento = tipoTratamiento();
        TablaConfig tipoTransporte = tipoTransporte();
        //SwingUtilities.invokeLater(() -> new FormReferencial(niveltox).setVisible(true));
         //SwingUtilities.invokeLater(() -> new FormReferencial(envase).setVisible(true));
         //SwingUtilities.invokeLater(() -> new FormReferencial(tratamiento).setVisible(true));
         SwingUtilities.invokeLater(() -> new FormReferencial(tipoTransporte).setVisible(true));
    }
}
