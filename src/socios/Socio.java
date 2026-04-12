package socios;

public class Socio {
    private int codigo;
    private String dni;
    private String nombre;
    private String domicilio;
    private String telefono;
    private String correo;

    // Constructor
    public Socio(int codigo, String dni, String nombre, String domicilio, String telefono, String correo) {
        this.codigo = codigo;
        this.dni = dni;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.correo = correo;
    }

    // toString
    @Override
    public String toString() {
        return "Socio [Código = " + codigo + ", DNI = " + dni + ", Nombre = " + nombre + ", Domicilio = " + domicilio + ", Teléfono = " + telefono + ", Correo = " + correo + "]";
    }

    public static void main(String[] args) {
        Socio socio = new Socio(1, "12345678A", "Juan Pérez", "Calle Mayor 1", "600123456", "juan@email.com");
        System.out.println(socio);
    }
}
