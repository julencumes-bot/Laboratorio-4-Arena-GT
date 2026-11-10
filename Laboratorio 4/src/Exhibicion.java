import java.util.Collections;
import java.util.Set;

/** Actividad no competitiva: únicamente cumple el contrato del calendario. */
public class Exhibicion implements Programable {
    private final String titulo;
    private Programacion programacion;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la exhibición no puede estar vacío.");
        }
        this.titulo = titulo.trim();
        this.programacion = null;
    }

    @Override
    public void programar(Programacion nueva, Copa copa) {
        if (copa == null) {
            throw new IllegalArgumentException("La copa es obligatoria.");
        }
        copa.validarProgramacion(this, nueva);
        programacion = nueva;
    }

    @Override
    public Programacion getProgramacion() { return programacion; }

    @Override
    public Set<Equipo> getEquiposOcupados() { return Collections.emptySet(); }

    @Override
    public String descripcion() { return "Exhibición: " + titulo; }

    public EstadoExhibicion getEstado() {
        return programacion == null ? EstadoExhibicion.SIN_PROGRAMAR : EstadoExhibicion.PROGRAMADA;
    }
}
