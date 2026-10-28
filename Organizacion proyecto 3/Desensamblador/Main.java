package desensamblador;

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

public final class Main {
    private Main() {}

    private static final String SALTO = System.lineSeparator();

    public static void main(String[] args) {
        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

        if (args.length != 1) {
            out.println("Uso: desensamblador.sh <archivo.hack>");
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
            codigo = Desensamblador.desensamblar(lineas);
        } catch (ErrorLinea e) {
            out.println(e.formato());
            System.exit(1);
            return;
        }

        StringBuilder sb = new StringBuilder(codigo.size() * 12);
        for (String linea : codigo) {
            sb.append(linea).append(SALTO);
        }
        try {
            Files.write(nombreSalida(entrada), sb.toString().getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            out.println("No se pudo escribir el archivo de salida: " + e.getMessage());
            System.exit(2);
        }
    }

    static Path nombreSalida(Path entrada) {
        String nombre = entrada.getFileName().toString();
        int punto = nombre.lastIndexOf('.');
        String base = (punto > 0) ? nombre.substring(0, punto) : nombre;
        return entrada.resolveSibling(base + "Dis.asm");
    }

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
