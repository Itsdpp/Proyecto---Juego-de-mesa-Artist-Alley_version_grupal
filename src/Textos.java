import java.io.IOException;

public class Textos {
    public static final String RESET = "\u001B[0m";
    public static final String NEGRITA = "\u001B[1m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String GRIS = "\u001B[90m";

    public static String tachar(String x) {
        return GRIS + "\u001B[9m" + x + RESET;
    }

    public static void Escribir(Object x) {
        System.out.print(x);
    }

    public static void EscribirLinea(Object x) {
        System.out.println(x);
    }

    // texto en un color y salto de linea
    public static void Color(String color, Object x) {
        System.out.println(color + x + RESET);
    }

    // titulo centrado dentro de un marco
    public static void Titulo(String texto) {
        int ancho = 44;
        int izq = (ancho - texto.length()) / 2;
        int der = ancho - texto.length() - izq;
        System.out.println(CYAN + "+" + "=".repeat(ancho) + "+");
        System.out.println("|" + " ".repeat(izq) + AMARILLO + NEGRITA + texto + RESET + CYAN + " ".repeat(der) + "|");
        System.out.println("+" + "=".repeat(ancho) + "+" + RESET);
    }

    // linea separadora con titulo
    public static void Separador(String texto) {
        System.out.println(GRIS + "--- " + RESET + NEGRITA + texto + RESET + GRIS + " ---" + RESET);
    }

    // barra de progreso: [#####-----] 5/10
    public static String Barra(int actual, int max) {
        int largo = 20;
        int llenos = 0;
        if (max > 0) {
            llenos = Math.min(largo, actual * largo / max);
        }
        return "[" + "#".repeat(llenos) + "-".repeat(largo - llenos) + "] " + actual + "/" + max;
    }

    // espera a que el jugador presione Enter
    public static void Pausa(String mensaje) {
        System.out.println();
        System.out.print(GRIS + mensaje + RESET);
        try {
            int c;
            do {
                c = System.in.read();
            } while (c != '\n' && c != -1);
        } catch (IOException e) {
            // no pasa nada, sigue el juego
        }
    }
}