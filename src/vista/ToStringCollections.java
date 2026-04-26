package vista;

import java.util.List;
import java.util.Map;

public class ToStringCollections {
    /**
     * Este metodo convierte una lista en una cadena de texto
     * @param lista Lista de objetos a convertir
     * @return Devuelve una cadena con todos los elementos de la lista separados por saltos de línea
     */
    public static String toStringList(List<?> lista) {
        StringBuilder cadena = new StringBuilder();

        for (Object o: lista) {
            cadena.append(o).append("\n");
        }

        return cadena.toString();
    }

    /**
     * Este metodo convierte un mapa en una cadena de texto
     * @param mapa Mapa con los datos a convertir
     * @return Devuelve una cadena con las claves y valores del mapa formateados
     */
    public static String toStringMap(Map<?, Object> mapa) {
        StringBuilder cadena = new StringBuilder();

        for(Map.Entry<?, Object> entry : mapa.entrySet()) {
            cadena.append("Fecha inicio [").append(entry.getKey()).append("]").append(": ").append(entry.getValue()).append("\n");
        }
        return cadena.toString();
    }
}
