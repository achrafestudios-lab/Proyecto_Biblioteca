package libros;

public class Libro {
    private int codigo;
    private String isbn;
    private String titulo;
    private String escritor;
    private int anioPublicacion;
    private double puntuacion;

    // Constructor
    public Libro(int codigo, String isbn, String titulo, String escritor, int anioPublicacion, double puntuacion) {
        this.codigo = codigo;
        this.isbn = isbn;
        this.titulo = titulo;
        this.escritor = escritor;
        this.anioPublicacion = anioPublicacion;
        this.puntuacion = puntuacion;
    }

    // toString
    @Override
    public String toString() {
        return "Libro [Código = " + codigo + ", ISBN = " + isbn + ", Título = " + titulo + ", Escritor = " + escritor + ", AñoPublicación = " + anioPublicacion + ", Puntuación = " + String.format("%.1f", puntuacion) + "]";
    }

    public static void main(String[] args) {
        Libro libro = new Libro(1, "2220123456789", "The Hobbit", "J.R.R. Tolkien", 1937, 6.5);
        System.out.println(libro);
    }
}
