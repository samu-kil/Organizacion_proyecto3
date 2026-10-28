package ensamblador;

import java.util.regex.Pattern;

/**
 * Análisis léxico/sintáctico de UNA línea. Valida todo lo que se puede validar sin
 * conocer el resto del programa (sección 10.1). Las comprobaciones que dependen de
 * otras líneas (etiqueta repetida) las hace TablaSimbolos.
 */
public final class Analizador {
    private Analizador() {}

    /** Símbolo: letras, dígitos, _ . $ : y no empieza por dígito. ASCII explícito (isLetter aceptaría 'ñ'). */
    private static final Pattern SIMBOLO = Pattern.compile("[A-Za-z_.$:][A-Za-z0-9_.$:]*");

    public static boolean esSimboloValido(String s) {
        return SIMBOLO.matcher(s).matches();
    }

    /** Quita el comentario (// hasta el final) y todos los espacios y tabulaciones. */
    public static String limpiar(String linea) {
        int c = linea.indexOf("//");
        if (c >= 0) {
            linea = linea.substring(0, c);
        }
        StringBuilder sb = new StringBuilder(linea.length());
        for (int i = 0; i < linea.length(); i++) {
            char ch = linea.charAt(i);
            if (ch != ' ' && ch != '\t') {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /** ¿La línea (ya limpia, no vacía) es una declaración de etiqueta? */
    public static boolean esEtiqueta(String limpia) {
        return limpia.charAt(0) == '(';
    }

    /** Valida "(NOMBRE)" y devuelve NOMBRE. */
    public static String nombreEtiqueta(String limpia, int nLinea) {
        if (!limpia.endsWith(")")) {
            throw new ErrorLinea(nLinea, "etiqueta sin cierre ')'");
        }
        String nombre = limpia.substring(1, limpia.length() - 1);
        if (nombre.isEmpty()) {
            throw new ErrorLinea(nLinea, "etiqueta vacía '()'");
        }
        if (!esSimboloValido(nombre)) {
            throw new ErrorLinea(nLinea, "nombre de etiqueta inválido '" + nombre + "'");
        }
        return nombre;
    }

    /** Analiza una instrucción A o C (la línea limpia no es vacía ni etiqueta). */
    public static Instruccion analizar(String limpia, int nLinea) {
        if (limpia.charAt(0) == '@') {
            return analizarA(limpia.substring(1), nLinea);
        }
        if (limpia.endsWith(")")) {
            throw new ErrorLinea(nLinea, "etiqueta sin apertura '('");
        }
        return analizarC(limpia, nLinea);
    }

    private static Instruccion analizarA(String v, int nLinea) {
        if (v.isEmpty()) {
            throw new ErrorLinea(nLinea, "instrucción A sin valor");
        }
        char primero = v.charAt(0);
        if (primero >= '0' && primero <= '9') {
            // Constante: todo dígitos y entre 0 y 32767.
            for (int i = 0; i < v.length(); i++) {
                char ch = v.charAt(i);
                if (ch < '0' || ch > '9') {
                    throw new ErrorLinea(nLinea, "constante inválida '@" + v + "'");
                }
            }
            String sinCeros = v.replaceFirst("^0+(?=.)", "");
            if (sinCeros.length() > 5 || Integer.parseInt(sinCeros) > 32767) {
                throw new ErrorLinea(nLinea, "constante fuera de rango (0..32767): '@" + v + "'");
            }
            return Instruccion.constante(Integer.parseInt(sinCeros));
        }
        if (!esSimboloValido(v)) {
            throw new ErrorLinea(nLinea, "símbolo inválido '@" + v + "'");
        }
        return Instruccion.simbolo(v);
    }

    private static Instruccion analizarC(String s, int nLinea) {
        if (cuenta(s, '=') > 1) {
            throw new ErrorLinea(nLinea, "más de un '=' en la instrucción");
        }
        if (cuenta(s, ';') > 1) {
            throw new ErrorLinea(nLinea, "más de un ';' en la instrucción");
        }

        // dest=comp;jump  ->  se corta primero por ';' y luego por '='.
        String jump = "";
        String resto = s;
        int pc = s.indexOf(';');
        boolean hayJump = pc >= 0;
        if (hayJump) {
            jump = s.substring(pc + 1);
            resto = s.substring(0, pc);
        }

        String dest = "";
        String comp = resto;
        int eq = resto.indexOf('=');
        if (eq >= 0) {
            dest = resto.substring(0, eq);
            comp = resto.substring(eq + 1);
            if (dest.isEmpty()) {
                throw new ErrorLinea(nLinea, "destino vacío antes de '='");
            }
            if (!Tablas.DEST.containsKey(dest)) {
                throw new ErrorLinea(nLinea, "destino inválido '" + dest + "'");
            }
        }

        if (comp.isEmpty()) {
            throw new ErrorLinea(nLinea, "cómputo vacío");
        }
        if (!Tablas.COMP.containsKey(comp)) {
            throw new ErrorLinea(nLinea, "cómputo inválido '" + comp + "'");
        }

        if (hayJump) {
            if (jump.isEmpty()) {
                throw new ErrorLinea(nLinea, "salto vacío después de ';'");
            }
            if (!Tablas.JUMP.containsKey(jump)) {
                throw new ErrorLinea(nLinea, "salto inválido '" + jump + "'");
            }
        }
        return Instruccion.c(dest, comp, jump);
    }

    private static int cuenta(String s, char c) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                n++;
            }
        }
        return n;
    }
}
