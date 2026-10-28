package ensamblador;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Uso: ensamblador.Main <ruta/Prog.asm>  ->  escribe Prog.hack en la misma carpeta. */
public final class Main {
    private Main() {}

    /** Separador de línea de la salida. Si el oficial en Windows genera CRLF, esto lo imita. */
    private static final String SALTO = System.lineSeparator();

    public static void main(String[] args) {
        // Salida estándar en UTF-8 para que "Línea" se vea igual en cualquier consola.
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

        if (args.length != 1) {
            out.println("Uso: ensamblador.sh <archivo.asm>");
            System.exit(2);
        }

        Path entrada = Paths.get(args[0]);
        List<String> lineas;
        try {
            lineas = leerLineas(entrada);
        } catch (IOException e) {
            out.println("No se pudo leer el archivo '" + args[0] + "': " + e.getMessage());
            System.exit(2);
            return;
        }

        List<String> codigo;
        try {
            codigo = Ensamblador.ensamblar(lineas);
        } catch (ErrorLinea e) {
            out.println(e.formato());
            System.exit(1);
            return;
        }

        // Solo se escribe si no hubo errores.
        StringBuilder sb = new StringBuilder(codigo.size() * 18);
        for (String instruccion : codigo) {
            sb.append(instruccion).append(SALTO);
        }
        try {
            Files.write(nombreSalida(entrada), sb.toString().getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            out.println("No se pudo escribir el archivo de salida: " + e.getMessage());
            System.exit(2);
        }
    }

    /** Prog.asm -> Prog.hack en la misma carpeta (resolveSibling maneja rutas con espacios). */
    static Path nombreSalida(Path entrada) {
        String nombre = entrada.getFileName().toString();
        int punto = nombre.lastIndexOf('.');
        String base = (punto > 0) ? nombre.substring(0, punto) : nombre;
        return entrada.resolveSibling(base + ".hack");
    }

    /**
     * Lee los bytes, los decodifica como ISO-8859-1 (nunca falla; un byte = un carácter),
     * separa por '\n', quita UN '\r' final por línea (CRLF) y descarta el elemento vacío
     * que deja el salto de línea final.
     */
    static List<String> leerLineas(Path ruta) throws IOException {
        String texto = new String(Files.readAllBytes(ruta), StandardCharsets.ISO_8859_1);
        List<String> lineas = new ArrayList<>(Arrays.asList(texto.split("\n", -1)));
        if (!lineas.isEmpty() && lineas.get(lineas.size() - 1).isEmpty()) {
            lineas.remove(lineas.size() - 1);
        }
        for (int i = 0; i < lineas.size(); i++) {
            String l = lineas.get(i);
            if (l.endsWith("\r")) {
                lineas.set(i, l.substring(0, l.length() - 1));
            }
        }
        return lineas;
    }
}
