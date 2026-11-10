# ArenaGT Lite

Laboratorio 4 de Polimorfismo · CC2008 Programación Orientada a Objetos · Universidad del Valle de Guatemala.

Aplicación de consola en Java para administrar equipos, encuentros de fútbol sala y baloncesto, exhibiciones, calendario y tabla de posiciones. Sigue las clases, interfaces, firmas y datos iniciales del análisis y diseño proporcionado. Todos los datos permanecen únicamente en memoria.

## Requisitos

- Java 17 o superior para ejecutar los archivos ya compilados de `bin/`.
- JDK 17 o superior, con `java` y `javac` disponibles, para recompilar. Los compilados incluidos tienen formato de Java 17 y se verificaron con OpenJDK 21.0.13.
- Archivos guardados en UTF-8.
- No requiere Maven, Gradle, dependencias externas, base de datos ni conexión a Internet.

## Ejecutar los archivos compilados

El ZIP incluye `bin/` con los archivos `.class` de la aplicación y las pruebas. Extrae todo el ZIP y abre una terminal en la carpeta que contiene este README:

```bash
java -Dfile.encoding=UTF-8 -cp bin Aplicacion
```

Este comando ejecuta el programa sin compilar. Para ejecutar las pruebas incluidas, usa `java -Dfile.encoding=UTF-8 -cp bin PruebasArenaGT`.

## Recompilar y ejecutar

Abre una terminal en la carpeta que contiene este README.

```bash
mkdir bin
javac --release 17 -encoding UTF-8 -d bin src/*.java
java -Dfile.encoding=UTF-8 -cp bin Aplicacion
```

Como `bin` ya viene incluida, omite el primer comando salvo que la hayas eliminado. En Windows, usa CMD o PowerShell; en CMD ejecuta `chcp 65001` si los acentos no se muestran bien.

También se incluyen estos accesos:

| Sistema | Iniciar el programa | Ejecutar las pruebas |
| --- | --- | --- |
| Windows CMD | `ejecutar.bat` | `probar.bat` |
| Windows PowerShell | `.\ejecutar.bat` | `.\probar.bat` |
| Linux o macOS | `bash ejecutar.sh` | `bash probar.sh` |

Los scripts compilan antes de ejecutar y se detienen si la compilación falla.

## Menú

1. Registrar equipo.
2. Crear encuentro, seleccionando deporte y códigos de equipo.
3. Crear exhibición.
4. Programar o reprogramar evento.
5. Registrar resultado de encuentro.
6. Mostrar tabla de posiciones.
7. Mostrar calendario.
8. Listar todos los eventos, incluidos los que aún no tienen fecha, con sus IDs.
0. Salir.

Las fechas se ingresan como `yyyy-MM-dd` y las horas como enteros de `0` a `23`. Los marcadores son enteros no negativos. Ante texto en un campo numérico o una fecha incorrecta se vuelve a pedir ese campo. Una operación rechazada por las reglas del modelo muestra el motivo y regresa al menú sin cambiar los datos.

Para probar el registro de resultados inmediatamente, usa la opción 5, ID `4`, marcador local `80` y visitante `70`. Se finaliza el encuentro Luna contra Faro y la tabla queda Faro 5, Luna 3, Sol 2 y Río 1. Cada nueva ejecución restablece los datos iniciales.

## Datos precargados

| Código | Equipo | Empresa | Puntos iniciales de tabla |
| --- | --- | --- | ---: |
| FAR | Faro | Empresa Norte | 4 |
| SOL | Sol | Empresa Norte | 2 |
| LUN | Luna | Empresa Sur | 1 |
| RIO | Río | Empresa Sur | 1 |

| ID | Evento | Fecha y hora | Sede | Resultado |
| --- | --- | --- | --- | --- |
| 1 | Fútbol sala Faro contra Sol | 2026-11-12 09:00 | Cancha A | 3 a 1 |
| 2 | Baloncesto Sol contra Faro | 2026-11-12 14:00 | Gimnasio A | 78 a 74 |
| 3 | Fútbol sala Luna contra Río | 2026-11-13 09:00 | Cancha A | 2 a 2 |
| 4 | Baloncesto Luna contra Faro | 2026-11-14 10:00 | Gimnasio A | Pendiente |
| 5 | Exhibición Clínica de baloncesto | 2026-11-12 09:00 | Auditorio | No aplica |

La tabla inicial es Faro 4, Sol 2, Luna 1 y Río 1. Luna precede a Río por nombre. El calendario recorre los IDs `5, 1, 2, 3, 4`.

## Organización y diseño

| Ruta | Contenido |
| --- | --- |
| `src/` | Las 12 clases, interfaces y enumeraciones de la aplicación. |
| `bin/` | Archivos `.class` compilados de la aplicación y las pruebas, compatibles con Java 17 o superior. |
| `tests/PruebasArenaGT.java` | 40 pruebas automáticas, sin bibliotecas externas. |
| `docs/Informe_ArenaGT_Lite.pdf` | Análisis, diseño UML incorporado y evidencia de las pruebas realizadas. |
| `docs/Resultados_pruebas.txt` | Salida real completa de la suite, con entradas, expectativas, resultados y comparación de datos ante rechazos. |
| `docs/Ejemplo_consola.txt` | Ejecución real de las consultas iniciales y un intento de resultado inválido. |
| `docs/Revision_diseno.txt` | Correspondencia con la guía y precisiones de implementación. |
| `docs/uml/` | Las seis vistas UML del documento original, incluidas también en el informe. |
| `ejecutar.*`, `probar.*` | Scripts de compilación, ejecución y pruebas. |

`Programable` extiende `Comparable<Programable>` y contiene la única implementación del orden cronológico en su método `compareTo` por defecto. `Puntuable` define la capacidad de registrar resultados. `Encuentro` es abstracta, implementa ambos contratos y centraliza el estado, los equipos, la programación y el resultado. `EncuentroFutbolSala` y `EncuentroBaloncesto` implementan las reglas deportivas; `Exhibicion` cumple únicamente `Programable`.

`Equipo` implementa `Comparable<Equipo>` para ordenar por puntos descendentes y nombre ascendente. `Copa` implementa `Iterable<Programable>` para entregar el calendario ordenado sin exponer sus mapas internos. La tabla y el calendario se forman sobre copias de las colecciones. Los dos índices de eventos guardan las mismas referencias, no copias de los encuentros.

La detección de conflictos usa `getEquiposOcupados`: dos equipos para los encuentros y un conjunto vacío para las exhibiciones. El modelo no usa `instanceof`, `getClass` ni conversiones para decidir el deporte o el tipo de evento. La selección del deporte solo interviene al crear un encuentro.

Los códigos se recortan y normalizan a mayúsculas. Las sedes se comparan sin distinguir mayúsculas; los nombres y sedes se ordenan con `compareToIgnoreCase`, como establece el análisis. El conflicto se determina por igualdad de fecha y hora de inicio; no se inventan duraciones ni restricciones sobre fechas pasadas.

## Pruebas reproducibles

```bash
javac --release 17 -encoding UTF-8 -d bin src/*.java tests/*.java
java -Dfile.encoding=UTF-8 -cp bin PruebasArenaGT
```

Para guardar una ejecución nueva sin reemplazar la evidencia incluida:

```bash
java -Dfile.encoding=UTF-8 -cp bin PruebasArenaGT > resultados_locales.txt
```

La ejecución incluida terminó con **40 aprobadas y 0 fallidas**. P01 a P16 corresponden a los casos previstos en el análisis original. P17 a P40 amplían la verificación con límites, encapsulamiento, desbordamiento y flujos reales del menú. Cada rechazo del modelo compara una copia de los valores antes y después: puntos, equipos, IDs, estado, marcador, programación y orden del calendario. La suite termina con error si alguna comprobación falla.

Las pruebas usan reflexión solamente para ejecutar la carga inicial privada de `Aplicacion` y acceder a su copa, conservando las visibilidades del diseño. La reflexión y las conversiones de comprobación pertenecen a las pruebas; no se usan para resolver las reglas del programa.

## Entrega

1. Copia el contenido de esta carpeta a la raíz de tu repositorio. Conserva `src/`, `bin/`, `tests/`, `docs/`, el README y los scripts.
2. Ejecuta el programa o las pruebas. Si modificas el código fuente, recompila para actualizar `bin/`.
3. Sube los archivos a un repositorio público en GitHub. El `.gitignore` permite incluir los archivos `.class` de `bin/`.
4. Entrega en Canvas `docs/Informe_ArenaGT_Lite.pdf` y la URL de tu repositorio público, como solicita la guía.

El ZIP contiene los archivos necesarios para esos pasos. La publicación del repositorio y la entrega en Canvas se realizan desde tus cuentas.
