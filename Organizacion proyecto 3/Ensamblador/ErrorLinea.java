package ensamblador;

public class ErrorLinea extends RuntimeException {
    private final int linea;

    public ErrorLinea(int linea, String mensaje) {
        super(mensaje);
        this.linea = linea;
    }

    public int getLinea() {
        return linea;
    }

    public String formato() {
        return "ERROR *** L\u00ednea " + linea + ": " + getMessage();
    }
}
