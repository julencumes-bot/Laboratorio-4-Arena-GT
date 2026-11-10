import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/** Datos y ciclo de vida comunes; cada deporte resuelve sus reglas abstractas. */
public abstract class Encuentro implements Programable, Puntuable {
    private final Equipo local;
    private final Equipo visitante;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private EstadoEncuentro estado;
    private Programacion programacion;

    protected Encuentro(Equipo local, Equipo visitante) {
        if (local == null || visitante == null) {
            throw new IllegalArgumentException("Se necesitan dos equipos registrados.");
        }
        if (local.getCodigo().equals(visitante.getCodigo())) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");
        }
        this.local = local;
        this.visitante = visitante;
        this.marcadorLocal = null;
        this.marcadorVisitante = null;
        this.estado = EstadoEncuentro.SIN_PROGRAMAR;
        this.programacion = null;
    }

    @Override
    public final void programar(Programacion nueva, Copa copa) {
        if (estado == EstadoEncuentro.FINALIZADO) {
            throw new IllegalStateException("Un encuentro finalizado no puede reprogramarse.");
        }
        if (copa == null) {
            throw new IllegalArgumentException("La copa es obligatoria.");
        }
        copa.validarProgramacion(this, nueva);
        programacion = nueva;
        estado = EstadoEncuentro.PROGRAMADO;
    }

    /** Método plantilla: primero valida todo y luego confirma el resultado completo. */
    @Override
    public final void registrarResultado(int a, int b) {
        if (estado == EstadoEncuentro.SIN_PROGRAMAR) {
            throw new IllegalStateException("Debe programar el encuentro antes de registrar el resultado.");
        }
        if (estado == EstadoEncuentro.FINALIZADO) {
            throw new IllegalStateException("El encuentro ya está finalizado y no admite otro resultado.");
        }
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("El marcador debe contener enteros no negativos.");
        }
        validarMarcador(a, b);
        int puntosLocal = calcularPuntos(a, b);
        int puntosVisitante = calcularPuntos(b, a);
        if (puntosLocal < 0 || puntosVisitante < 0) {
            throw new IllegalArgumentException("El deporte no puede repartir puntos de tabla negativos.");
        }
        // Comprobar AMBAS sumas antes de modificar algún equipo evita un resultado parcial.
        Math.addExact(local.getPuntosTabla(), puntosLocal);
        Math.addExact(visitante.getPuntosTabla(), puntosVisitante);
        local.sumarPuntos(puntosLocal);
        visitante.sumarPuntos(puntosVisitante);
        marcadorLocal = a;
        marcadorVisitante = b;
        estado = EstadoEncuentro.FINALIZADO;
    }

    @Override
    public Programacion getProgramacion() { return programacion; }

    @Override
    public Set<Equipo> getEquiposOcupados() {
        Set<Equipo> ocupados = new LinkedHashSet<>();
        ocupados.add(local);
        ocupados.add(visitante);
        return Collections.unmodifiableSet(ocupados);
    }

    @Override
    public String descripcion() {
        String texto = nombreDeporte() + ": " + local.getNombre() + " contra "
                + visitante.getNombre() + " [" + estado + "]";
        if (estado == EstadoEncuentro.FINALIZADO) {
            texto += " | Marcador: " + marcadorLocal + " a " + marcadorVisitante;
        }
        return texto;
    }

    public EstadoEncuentro getEstado() { return estado; }

    protected abstract void validarMarcador(int a, int b);
    protected abstract int calcularPuntos(int propios, int ajenos);
    protected abstract String nombreDeporte();
}
