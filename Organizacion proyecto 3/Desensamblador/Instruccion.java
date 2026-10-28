package desensamblador;

public final class Instruccion {
    public final boolean esA;
    public final int valor;
    public final String dest;
    public final String comp;
    public final String jump;

    private Instruccion(boolean esA, int valor, String dest, String comp, String jump) {
        this.esA = esA;
        this.valor = valor;
        this.dest = dest;
        this.comp = comp;
        this.jump = jump;
    }

    public static Instruccion a(int valor) {
        return new Instruccion(true, valor, null, null, null);
    }

    public static Instruccion c(String dest, String comp, String jump) {
        return new Instruccion(false, 0, dest, comp, jump);
    }

    public boolean esCConSalto() {
        return !esA && !jump.isEmpty();
    }
}
