package desensamblador;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class Tablas {
    private Tablas() {}

    public static final Map<String, String> COMP_INV;
    public static final Map<String, String> DEST_INV;
    public static final Map<String, String> JUMP_INV;

    static {
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
            comp.put("0" + e[1], e[0]);
            if (e[0].indexOf('A') >= 0) {
                comp.put("1" + e[1], e[0].replace('A', 'M'));
            }
        }
        COMP_INV = Collections.unmodifiableMap(comp);

        Map<String, String> dest = new HashMap<>();
        dest.put("000", "");
        dest.put("001", "M");
        dest.put("010", "D");
        dest.put("011", "MD");
        dest.put("100", "A");
        dest.put("101", "AM");
        dest.put("110", "AD");
        dest.put("111", "AMD");
        DEST_INV = Collections.unmodifiableMap(dest);

        Map<String, String> jump = new HashMap<>();
        jump.put("000", "");
        jump.put("001", "JGT");
        jump.put("010", "JEQ");
        jump.put("011", "JGE");
        jump.put("100", "JLT");
        jump.put("101", "JNE");
        jump.put("110", "JLE");
        jump.put("111", "JMP");
        JUMP_INV = Collections.unmodifiableMap(jump);
    }
}
