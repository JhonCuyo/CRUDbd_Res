
public class CampoConfig {

    public final String columnaBD;
    public final String etiqueta; 
    public final String encabezadoGrilla;
    public final boolean editableEnAdicionar;
    public final boolean editableEnModificar;
    public final boolean esClave;
    public final int anchoGrilla;        
    public final boolean esFK;
    public final String tablaFK;
    public final String colValorFK;
    public final String colMostrarFK;

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
        this.esFK                = false;
        this.tablaFK             = null;
        this.colValorFK          = null;
        this.colMostrarFK        = null;
    }

    public CampoConfig(String columnaBD, String etiqueta, String encabezadoGrilla,
                        boolean editableEnAdicionar, boolean editableEnModificar,
                        boolean esClave, int anchoGrilla, 
                        String tablaFK, String colValorFK, String colMostrarFK) {
        this.columnaBD           = columnaBD;
        this.etiqueta            = etiqueta;
        this.encabezadoGrilla    = encabezadoGrilla;
        this.editableEnAdicionar = editableEnAdicionar;
        this.editableEnModificar = editableEnModificar;
        this.esClave             = esClave;
        this.anchoGrilla         = anchoGrilla;
        this.esFK                = true;
        this.tablaFK             = tablaFK;
        this.colValorFK          = colValorFK;
        this.colMostrarFK        = colMostrarFK;
    }
}