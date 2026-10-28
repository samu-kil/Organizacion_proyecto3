package ensamblador;

/**
 * Instrucción ya analizada y validada. Es de tipo A (@valor o @símbolo)
 * o de tipo C (dest=comp;jump). Inmutable.
 */
public final class Instruccion {
    public final boolean esA;
    /** Solo tipo A: nombre del símbolo, o null si la instrucción es una constante. */
    public final String simbolo;
    /** Solo tipo A con constante: el valor (0..32767). */
    public final int valor;
    /** Solo tipo C: partes ya validadas ("" cuando están vacías). */
    public final String dest;
    public final String comp;
    public final String jump;

    private Instruccion(boolean esA, String simbolo, int valor, String dest, String comp, String jump) {
        this.esA = esA;
        this.simbolo = simbolo;
        this.valor = valor;
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    public static Instruccion constante(int valor) {
        return new Instruccion(true, null, valor, null, null, null);
    }

    public static Instruccion simbolo(String nombre) {
        return new Instruccion(true, nombre, 0, null, null, null);
    }

    public static Instruccion c(String dest, String comp, String jump) {
        return new Instruccion(false, null, 0, dest, comp, jump);
    }
}
