package comparator;

import libros.Libro;

import java.util.Comparator;

public class OrdenarPorCodigoLibroComparator implements Comparator<Libro> {
    @Override
    public int compare(Libro o1, Libro o2) {
        return o1.getCodigo() - o2.getCodigo();
    }
}
