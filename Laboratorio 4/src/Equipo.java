import java.util.Locale;

/** Equipo registrado. Los puntos de tabla no son el marcador de un encuentro. */
public class Equipo implements Comparable<Equipo> {
    private final String codigo;
    private final String nombre;
    private final String empresa;
    private int puntosTabla;

    public Equipo(String codigo, String nombre, String empresa) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException("La empresa no puede estar vacía.");
        }
        this.codigo = codigo.trim().toUpperCase(Locale.ROOT);
        this.nombre = nombre.trim();
        this.empresa = empresa.trim();
        this.puntosTabla = 0;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEmpresa() { return empresa; }
    public int getPuntosTabla() { return puntosTabla; }

    // Acceso de paquete: no se ofrece un setter público del acumulado.
    void sumarPuntos(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos de tabla no pueden ser negativos.");
        }
        puntosTabla = Math.addExact(puntosTabla, puntos);
    }

    @Override
    public int compareTo(Equipo otro) {
        int comparacion = Integer.compare(otro.puntosTabla, puntosTabla);
        return comparacion != 0 ? comparacion : nombre.compareToIgnoreCase(otro.nombre);
    }
}
