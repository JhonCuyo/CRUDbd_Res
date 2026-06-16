
public class CampoConfig {

    public final String columnaBD;
    public final String etiqueta; 
    public final String encabezadoGrilla;
    public final boolean editableEnAdicionar;
    public final boolean editableEnModificar;
    public final boolean esClave;
    public final int anchoGrilla;        

    public CampoConfig(String columnaBD, String etiqueta, String encabezadoGrilla,
                        boolean editableEnAdicionar, boolean editableEnModificar,
                        boolean esClave, int anchoGrilla) {
        this.columnaBD           = columnaBD;
        this.etiqueta            = etiqueta;
        this.encabezadoGrilla    = encabezadoGrilla;
        this.editableEnAdicionar = editableEnAdicionar;
        this.editableEnModificar = editableEnModificar;
        this.esClave             = esClave;
        this.anchoGrilla         = anchoGrilla;
    }

    public CampoConfig(String columnaBD, String etiqueta, String encabezadoGrilla,
                        boolean editableEnAdicionar, boolean editableEnModificar,
                        boolean esClave) {
        this(columnaBD, etiqueta, encabezadoGrilla, editableEnAdicionar, editableEnModificar, esClave, 120);
    }
}
