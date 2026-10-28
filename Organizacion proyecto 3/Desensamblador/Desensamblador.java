package desensamblador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class Desensamblador {
    private Desensamblador() {}


    public static List<String> desensamblar(List<String> lineas) {
        int n = lineas.size();

        Instruccion[] prog = new Instruccion[n];
        for (int i = 0; i < n; i++) {
            prog[i] = Decodificador.decodificar(lineas.get(i), i + 1);
        }

        TreeSet<Integer> destinos = new TreeSet<>();
        for (int i = 1; i < n; i++) {
            if (etiquetable(prog, i - 1, n)) {
                destinos.add(prog[i - 1].valor);
            }
        }

        Map<Integer, String> etiqueta = new HashMap<>();
        int k = 0;
        for (int direccion : destinos) {
            etiqueta.put(direccion, "L" + k++);
        }

        List<String> salida = new ArrayList<>(n + etiqueta.size());
        for (int i = 0; i < n; i++) {
            if (etiqueta.containsKey(i)) {
                salida.add("(" + etiqueta.get(i) + ")");
            }
            Instruccion ins = prog[i];
            if (ins.esA) {
                if (etiquetable(prog, i, n)) {
                    salida.add("@" + etiqueta.get(ins.valor));
                } else {
                    salida.add("@" + ins.valor);   // las demás A se dejan en decimal
                }
            } else {
                String dest = ins.dest.isEmpty() ? "" : ins.dest + "=";
                String jump = ins.jump.isEmpty() ? "" : ";" + ins.jump;
                salida.add(dest + ins.comp + jump);
            }
        }
        if (etiqueta.containsKey(n)) {
            salida.add("(" + etiqueta.get(n) + ")");
        }
        return salida;
    }

    private static boolean etiquetable(Instruccion[] prog, int j, int n) {
        return prog[j].esA
                && j + 1 < n
                && prog[j + 1].esCConSalto()
                && prog[j].valor <= n;
    }
}
