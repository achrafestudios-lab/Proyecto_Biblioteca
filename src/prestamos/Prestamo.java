package prestamos;

public class Prestamo {
    private int codigoLibro;
    private int codigoSocio;
    private String fechaInicio;
    private String fechaFin;
    private String fechaDevolucion;

    // Constructor
    public Prestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin, String fechaDevolucion) {
        this.codigoLibro = codigoLibro;
        this.codigoSocio = codigoSocio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaDevolucion = fechaDevolucion;
    }

    // toString
    @Override
    public String toString() {
        return "Préstamo [CódigoLibro = " + codigoLibro +
                ", CódigoSocio = " + codigoSocio +
                ", FechaInicio = " + fechaInicio +
                ", FechaFin = " + fechaFin +
                ", FechaDevolucion = " + fechaDevolucion + "]";
    }

}
