
import java.util.List;

public class TablaConfig {

    public final String tabla; 
    public final String titulo;  
    public final String subtitulo;
    public final String columnaEstado;
    public final List<CampoConfig> campos;

    public TablaConfig(String tabla, String titulo, String subtitulo,
            String columnaEstado, List<CampoConfig> campos) {
        this.tabla = tabla;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.columnaEstado = columnaEstado;
        this.campos = campos;
    }

    public CampoConfig campoClave() {
        for (CampoConfig c : campos) {
            if (c.esClave) {
                return c;
            }
        }
        return campos.get(0);
    }
}
