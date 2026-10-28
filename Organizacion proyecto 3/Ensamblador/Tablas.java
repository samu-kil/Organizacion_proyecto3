package ensamblador;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Tablas estáticas de la plataforma Hack (secciones 7.2 y 7.3 de la especificación). */
public final class Tablas {
    private Tablas() {}

    /** comp -> 7 bits "a c1c2c3c4c5c6". */
    public static final Map<String, String> COMP;
    /** dest -> 3 bits (la clave "" es el destino vacío). */
    public static final Map<String, String> DEST;
    /** jump -> 3 bits (la clave "" es el salto vacío). */
    public static final Map<String, String> JUMP;
    /** Símbolos predefinidos -> dirección. */
    public static final Map<String, Integer> PREDEFINIDOS;

    static {
        // 18 cómputos con a = 0: mnemónico -> c1..c6
        String[][] a0 = {
            {"0", "101010"}, {"1", "111111"}, {"-1", "111010"},
            {"D", "001100"}, {"A", "110000"}, {"!D", "001101"},
            {"!A", "110001"}, {"-D", "001111"}, {"-A", "110011"},
            {"D+1", "011111"}, {"A+1", "110111"}, {"D-1", "001110"},
            {"A-1", "110010"}, {"D+A", "000010"}, {"D-A", "010011"},
            {"A-D", "000111"}, {"D&A", "000000"}, {"D|A", "010101"}
        };
        Map<String, String> comp = new HashMap<>();
        for (String[] e : a0) {
            comp.put(e[0], "0" + e[1]);
            // Los que usan A tienen gemelo con M (a = 1) y los mismos c1..c6.
            if (e[0].indexOf('A') >= 0) {
                comp.put(e[0].replace('A', 'M'), "1" + e[1]);
            }
        }
        COMP = Collections.unmodifiableMap(comp);

        Map<String, String> dest = new HashMap<>();
        dest.put("", "000");
        dest.put("M", "001");
        dest.put("D", "010");
        dest.put("MD", "011");
        dest.put("A", "100");
        dest.put("AM", "101");
        dest.put("AD", "110");
        dest.put("AMD", "111");
        DEST = Collections.unmodifiableMap(dest);

        Map<String, String> jump = new HashMap<>();
        jump.put("", "000");
        jump.put("JGT", "001");
        jump.put("JEQ", "010");
        jump.put("JGE", "011");
        jump.put("JLT", "100");
        jump.put("JNE", "101");
        jump.put("JLE", "110");
        jump.put("JMP", "111");
        JUMP = Collections.unmodifiableMap(jump);

        Map<String, Integer> pre = new HashMap<>();
        pre.put("SP", 0);
        pre.put("LCL", 1);
        pre.put("ARG", 2);
        pre.put("THIS", 3);
        pre.put("THAT", 4);
        for (int i = 0; i <= 15; i++) {
            pre.put("R" + i, i);
        }
        pre.put("SCREEN", 16384);
        pre.put("KBD", 24576);
        PREDEFINIDOS = Collections.unmodifiableMap(pre);
    }
}
