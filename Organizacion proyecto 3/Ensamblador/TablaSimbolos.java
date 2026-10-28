package ensamblador;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabla de símbolos única: nombre -> dirección. Nace con los predefinidos, la pasada 1
 * le añade las etiquetas y la pasada 2 le añade las variables (desde la dirección 16,
 * en el orden de primera aparición).
 */
public final class TablaSimbolos {
    private final Map<String, Integer> tabla = new HashMap<>(Tablas.PREDEFINIDOS);
    private int siguienteVariable = 16;

    /** Pasada 1: declara (NOMBRE) con la dirección de la siguiente instrucción. */
    public void declararEtiqueta(String nombre, int direccion, int nLinea) {
        if (Tablas.PREDEFINIDOS.containsKey(nombre)) {
            throw new ErrorLinea(nLinea, "la etiqueta '" + nombre + "' usa el nombre de un símbolo predefinido");
        }
        if (tabla.containsKey(nombre)) {
            throw new ErrorLinea(nLinea, "etiqueta '" + nombre + "' declarada más de una vez");
        }
        tabla.put(nombre, direccion);
    }

    /** Pasada 2: predefinido o etiqueta si existe; si no, es una variable nueva. */
    public int resolver(String nombre) {
        Integer dir = tabla.get(nombre);
        if (dir == null) {
            dir = siguienteVariable++;
            tabla.put(nombre, dir);
        }
        return dir;
    }
}
