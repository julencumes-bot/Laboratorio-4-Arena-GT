import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Entrada del programa: interacción por consola y carga inicial del laboratorio. */
public class Aplicacion {
    private final Copa copa;
    private final Scanner entrada;

    public Aplicacion() {
        copa = new Copa();
        entrada = new Scanner(System.in, "UTF-8");
    }

    public static void main(String[] args) {
        Aplicacion aplicacion = new Aplicacion();
        aplicacion.cargarDatosIniciales();
        aplicacion.ejecutarMenu();
    }

    private void cargarDatosIniciales() {
        copa.registrarEquipo("FAR", "Faro", "Empresa Norte");
        copa.registrarEquipo("SOL", "Sol", "Empresa Norte");
        copa.registrarEquipo("LUN", "Luna", "Empresa Sur");
        copa.registrarEquipo("RIO", "Río", "Empresa Sur");

        int futbol = copa.crearFutbolSala("FAR", "SOL");
        copa.programarEvento(futbol, LocalDate.of(2026, 11, 12), 9, "Cancha A");
        copa.registrarResultado(futbol, 3, 1);

        int baloncesto = copa.crearBaloncesto("SOL", "FAR");
        copa.programarEvento(baloncesto, LocalDate.of(2026, 11, 12), 14, "Gimnasio A");
        copa.registrarResultado(baloncesto, 78, 74);

        int empate = copa.crearFutbolSala("LUN", "RIO");
        copa.programarEvento(empate, LocalDate.of(2026, 11, 13), 9, "Cancha A");
        copa.registrarResultado(empate, 2, 2);

        int pendiente = copa.crearBaloncesto("LUN", "FAR");
        copa.programarEvento(pendiente, LocalDate.of(2026, 11, 14), 10, "Gimnasio A");

        int exhibicion = copa.crearExhibicion("Clínica de baloncesto");
        copa.programarEvento(exhibicion, LocalDate.of(2026, 11, 12), 9, "Auditorio");
    }

    private void ejecutarMenu() {
        System.out.println("ArenaGT Lite - Copa entre empresas");
        System.out.println("Datos iniciales: 4 equipos, 4 encuentros (3 finalizados) y 1 exhibición programada.");
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n1. Registrar equipo");
            System.out.println("2. Crear encuentro");
            System.out.println("3. Crear exhibición");
            System.out.println("4. Programar o reprogramar evento");
            System.out.println("5. Registrar resultado de encuentro");
            System.out.println("6. Mostrar tabla de posiciones");
            System.out.println("7. Mostrar calendario");
            System.out.println("8. Listar todos los eventos e identificadores");
            System.out.println("0. Salir");
            try {
                int opcion = leerEntero("Opción: ");
                switch (opcion) {
                    case 1: {
                        System.out.print("Código: ");
                        String codigo = entrada.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = entrada.nextLine();
                        System.out.print("Empresa: ");
                        String empresa = entrada.nextLine();
                        Equipo equipo = copa.registrarEquipo(codigo, nombre, empresa);
                        System.out.println("Equipo registrado: " + equipo.getCodigo() + " - " + equipo.getNombre());
                        break;
                    }
                    case 2: {
                        for (Equipo equipo : copa.tablaPosiciones()) {
                            System.out.println(equipo.getCodigo() + " - " + equipo.getNombre());
                        }
                        int deporte = leerEntero("Deporte (1. Fútbol sala / 2. Baloncesto): ");
                        if (deporte != 1 && deporte != 2) {
                            throw new IllegalArgumentException("Seleccione 1 para fútbol sala o 2 para baloncesto.");
                        }
                        System.out.print("Código del equipo local: ");
                        String local = entrada.nextLine();
                        System.out.print("Código del equipo visitante: ");
                        String visitante = entrada.nextLine();
                        // La elección del deporte solo decide la creación; nunca las reglas.
                        int id = deporte == 1 ? copa.crearFutbolSala(local, visitante)
                                : copa.crearBaloncesto(local, visitante);
                        System.out.println("Encuentro creado con ID " + id + ", sin programar.");
                        break;
                    }
                    case 3: {
                        System.out.print("Título de la exhibición: ");
                        int id = copa.crearExhibicion(entrada.nextLine());
                        System.out.println("Exhibición creada con ID " + id + ", sin programar.");
                        break;
                    }
                    case 4: {
                        for (Map.Entry<Integer, Programable> item : copa.listarEventos().entrySet()) {
                            System.out.println("ID " + item.getKey() + " | " + item.getValue().descripcion());
                        }
                        int id = leerEntero("ID del evento: ");
                        LocalDate fecha = leerFecha("Fecha (yyyy-MM-dd): ");
                        int hora = leerEntero("Hora de inicio (0 a 23): ");
                        System.out.print("Sede: ");
                        String sede = entrada.nextLine();
                        copa.programarEvento(id, fecha, hora, sede);
                        System.out.println("Programación guardada para el evento " + id + ".");
                        break;
                    }
                    case 5: {
                        for (Map.Entry<Integer, Programable> item : copa.listarEventos().entrySet()) {
                            System.out.println("ID " + item.getKey() + " | " + item.getValue().descripcion());
                        }
                        int id = leerEntero("ID del encuentro: ");
                        int a = leerEntero("Marcador del equipo local: ");
                        int b = leerEntero("Marcador del equipo visitante: ");
                        copa.registrarResultado(id, a, b);
                        System.out.println("Resultado registrado. Encuentro finalizado y puntos actualizados.");
                        break;
                    }
                    case 6: {
                        System.out.println("\nTABLA DE POSICIONES (puntos de tabla)");
                        System.out.printf("%-5s %-10s %-22s %-24s %s%n", "Pos.", "Código", "Equipo", "Empresa", "Puntos");
                        int posicion = 1;
                        for (Equipo equipo : copa.tablaPosiciones()) {
                            System.out.printf("%-5d %-10s %-22s %-24s %d%n", posicion++, equipo.getCodigo(),
                                    equipo.getNombre(), equipo.getEmpresa(), equipo.getPuntosTabla());
                        }
                        break;
                    }
                    case 7: {
                        System.out.println("\nCALENDARIO");
                        boolean hayEventos = false;
                        for (Programable evento : copa) {
                            hayEventos = true;
                            Programacion p = evento.getProgramacion();
                            System.out.printf("%s | %02d:00 | %s | %s%n", p.getFecha(), p.getHora(),
                                    p.getSede(), evento.descripcion());
                        }
                        if (!hayEventos) { System.out.println("No hay eventos programados."); }
                        break;
                    }
                    case 8: {
                        System.out.println("\nEVENTOS REGISTRADOS");
                        for (Map.Entry<Integer, Programable> item : copa.listarEventos().entrySet()) {
                            Programable evento = item.getValue();
                            Programacion p = evento.getProgramacion();
                            String horario = p == null ? "SIN_PROGRAMAR"
                                    : p.getFecha() + String.format(" %02d:00 | ", p.getHora()) + p.getSede();
                            System.out.println("ID " + item.getKey() + " | " + evento.descripcion() + " | " + horario);
                        }
                        break;
                    }
                    case 0:
                        continuar = false;
                        System.out.println("Gracias por usar ArenaGT Lite.");
                        break;
                    default:
                        System.out.println("Opción inválida. Seleccione una opción de 0 a 8.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Operación rechazada: " + ex.getMessage());
            } catch (ArithmeticException ex) {
                System.out.println("Operación rechazada: se excedería la capacidad de los números enteros.");
            } catch (NoSuchElementException ex) {
                // Fin de entrada (Ctrl+D/Ctrl+Z o archivo agotado): salir sin traza de error.
                System.out.println("\nFin de la entrada. Programa terminado.");
                continuar = false;
            }
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException ex) {
                System.out.println("Entrada inválida: escriba un número entero entre -2147483648 y 2147483647.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                if (!texto.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    throw new DateTimeParseException("Formato incorrecto", texto, 0);
                }
                return LocalDate.parse(texto);
            } catch (DateTimeParseException ex) {
                System.out.println("Fecha inválida: use yyyy-MM-dd con una fecha real, por ejemplo 2026-11-12.");
            }
        }
    }
}
