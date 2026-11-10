import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Registro de equipos y eventos. Coordina mediante contratos, sin consultar tipos. */
public class Copa implements Iterable<Programable> {
    private final Map<String, Equipo> equipos;
    private final Map<Integer, Programable> eventos;
    private final Map<Integer, Puntuable> puntuables;
    private int siguienteId;

    public Copa() {
        equipos = new LinkedHashMap<>();
        eventos = new LinkedHashMap<>();
        puntuables = new LinkedHashMap<>();
        siguienteId = 1;
    }

    public Equipo registrarEquipo(String codigo, String nombre, String empresa) {
        Equipo nuevo = new Equipo(codigo, nombre, empresa);
        if (equipos.containsKey(nuevo.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un equipo con el código " + nuevo.getCodigo() + ".");
        }
        equipos.put(nuevo.getCodigo(), nuevo);
        return nuevo;
    }

    public Equipo buscarEquipo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        Equipo equipo = equipos.get(codigo.trim().toUpperCase(Locale.ROOT));
        if (equipo == null) {
            throw new IllegalArgumentException("No existe el equipo con código " + codigo.trim() + ".");
        }
        return equipo;
    }

    public int crearFutbolSala(String codLocal, String codVisitante) {
        return agregarEncuentro(new EncuentroFutbolSala(buscarEquipo(codLocal), buscarEquipo(codVisitante)));
    }

    public int crearBaloncesto(String codLocal, String codVisitante) {
        return agregarEncuentro(new EncuentroBaloncesto(buscarEquipo(codLocal), buscarEquipo(codVisitante)));
    }

    public int crearExhibicion(String titulo) {
        return guardarEvento(new Exhibicion(titulo));
    }

    private int agregarEncuentro(Encuentro e) {
        int id = guardarEvento(e);
        puntuables.put(id, e); // Misma referencia; no se duplica el encuentro.
        return id;
    }

    private int guardarEvento(Programable e) {
        int id = siguienteId;
        int siguiente = Math.addExact(siguienteId, 1);
        eventos.put(id, e);
        siguienteId = siguiente;
        return id;
    }

    public void programarEvento(int id, LocalDate fecha, int hora, String sede) {
        Programable evento = eventos.get(id);
        if (evento == null) {
            throw new IllegalArgumentException("No existe un evento con ID " + id + ".");
        }
        evento.programar(new Programacion(fecha, hora, sede), this);
    }

    public void validarProgramacion(Programable e, Programacion nueva) {
        if (e == null || nueva == null) {
            throw new IllegalArgumentException("El evento y la programación son obligatorios.");
        }
        boolean pertenece = false;
        for (Programable registrado : eventos.values()) {
            if (registrado == e) { pertenece = true; break; }
        }
        if (!pertenece) {
            throw new IllegalArgumentException("El evento no pertenece a esta copa.");
        }
        for (Map.Entry<Integer, Programable> entrada : eventos.entrySet()) {
            Programable otro = entrada.getValue();
            Programacion actual = otro.getProgramacion();
            if (otro == e || actual == null) { continue; }
            if (actual.getFecha().equals(nueva.getFecha()) && actual.getHora() == nueva.getHora()) {
                if (actual.getSede().equalsIgnoreCase(nueva.getSede())) {
                    throw new IllegalArgumentException("La sede ya está ocupada por el evento " + entrada.getKey() + ".");
                }
                for (Equipo equipo : e.getEquiposOcupados()) {
                    if (otro.getEquiposOcupados().contains(equipo)) {
                        throw new IllegalArgumentException("El equipo " + equipo.getNombre()
                                + " ya participa en el encuentro " + entrada.getKey() + " a esa fecha y hora.");
                    }
                }
            }
        }
    }

    public void registrarResultado(int id, int a, int b) {
        Puntuable encuentro = puntuables.get(id);
        if (encuentro == null) {
            if (eventos.containsKey(id)) {
                throw new IllegalArgumentException("El evento " + id + " no admite resultados.");
            }
            throw new IllegalArgumentException("No existe un evento con ID " + id + ".");
        }
        encuentro.registrarResultado(a, b);
    }

    public List<Equipo> tablaPosiciones() {
        List<Equipo> tabla = new ArrayList<>(equipos.values());
        Collections.sort(tabla);
        return Collections.unmodifiableList(tabla);
    }

    public Map<Integer, Programable> listarEventos() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(eventos));
    }

    @Override
    public Iterator<Programable> iterator() {
        List<Programable> calendario = new ArrayList<>();
        for (Programable evento : eventos.values()) {
            if (evento.getProgramacion() != null) { calendario.add(evento); }
        }
        Collections.sort(calendario);
        return Collections.unmodifiableList(calendario).iterator();
    }
}
