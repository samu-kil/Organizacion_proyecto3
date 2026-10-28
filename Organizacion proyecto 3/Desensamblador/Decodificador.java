package desensamblador;


public final class Decodificador {
    private Decodificador() {}

    public static Instruccion decodificar(String linea, int nLinea) {
        if (linea.length() != 16) {
            throw new ErrorLinea(nLinea, "longitud inválida: " + linea.length() + " caracteres (se esperaban 16)");
        }
        for (int i = 0; i < 16; i++) {
            char ch = linea.charAt(i);
            if (ch != '0' && ch != '1') {
                throw new ErrorLinea(nLinea, "carácter inválido '" + ch + "' en la posición " + (i + 1) + " (solo 0 y 1)");
            }
        }

        if (linea.charAt(0) == '0') {
            return Instruccion.a(Integer.parseInt(linea.substring(1), 2));
        }

        if (!linea.startsWith("111")) {
            throw new ErrorLinea(nLinea, "instrucción C con bits 14-13 distintos de 11");
        }
        String bitsComp = linea.substring(3, 10);
        String comp = Tablas.COMP_INV.get(bitsComp);
        if (comp == null) {
            throw new ErrorLinea(nLinea, "cómputo inválido (a c1..c6 = " + bitsComp + ")");
        }
        String dest = Tablas.DEST_INV.get(linea.substring(10, 13));
        String jump = Tablas.JUMP_INV.get(linea.substring(13, 16));
        return Instruccion.c(dest, comp, jump);
    }
}
