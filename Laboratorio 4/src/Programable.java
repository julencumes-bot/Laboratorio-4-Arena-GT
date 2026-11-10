import java.util.Set;

/** Contrato común de los eventos que pueden aparecer en el calendario. */
public interface Programable extends Comparable<Programable> {
    void programar(Programacion nueva, Copa copa);
    Programacion getProgramacion();
    Set<Equipo> getEquiposOcupados();
    String descripcion();

    /** Único criterio de orden cronológico para todos los tipos de evento. */
    @Override
    default int compareTo(Programable otro) {
        Programacion propia = getProgramacion();
        Programacion ajena = otro.getProgramacion();
        if (propia == null) {
            return ajena == null ? 0 : 1;
        }
        if (ajena == null) {
            return -1;
        }
        int comparacion = propia.getFecha().compareTo(ajena.getFecha());
        if (comparacion == 0) {
            comparacion = Integer.compare(propia.getHora(), ajena.getHora());
        }
        if (comparacion == 0) {
            comparacion = propia.getSede().compareToIgnoreCase(ajena.getSede());
        }
        return comparacion;
    }
}
