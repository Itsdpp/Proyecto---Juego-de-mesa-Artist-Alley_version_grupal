import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReglasDelJuego {
    private BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    private Artistas[] jugadores = new Artistas[3]; // 1 persona y 2 bots
    private String[] nombres = {"Tu", "Bot 1", "Bot 2"};
    private String[] colores = {Textos.VERDE, Textos.CYAN, Textos.MAGENTA};
    private int rondas = 0; // cantidad de rondas que elige el jugador
    private int[] clientes; // presupuesto de cada cliente
    private int Comprador = 0; // presupuesto del cliente en la ronda actual

    // fase 0: se pregunta la cantidad de rondas (minimo 3, maximo 9)
    private int PreGame() {
        int cantidad = 0;
        boolean valido;
        do {
            valido = true;
            Textos.Escribir("Porfavor, escribir la cantidad de rondas (minimo 3, maximo 9): ");
            try {
                cantidad = Integer.parseInt(lector.readLine().trim());
            } catch (IOException | NumberFormatException e) {
                Textos.EscribirLinea("Porfavor, escribir un numero");
                valido = false;
                continue;
            }
            if (cantidad < 3 || cantidad > 9) {
                Textos.EscribirLinea("Por favor, escribir un numero dentro de los limites");
                valido = false;
            }
        } while (!valido);
        return cantidad;
    }

    public void InicioJuego() {
        jugadores[0] = new Artistas();
        jugadores[1] = new Bots();
        jugadores[2] = new Bots();

        Cl.clean();
        Textos.Titulo("ARTIST ALLEY");
        Textos.Color(Textos.GRIS, "Juego de mesa - 1 jugador vs 2 bots");
        Textos.EscribirLinea("");
        Textos.EscribirLinea("Cada ronda llega un cliente con un limite de dinero.");
        Textos.EscribirLinea("Juegan una carta y el cliente compra de menor a mayor");
        Textos.EscribirLinea("hasta que la siguiente ya no le alcance.");
        Textos.EscribirLinea("Si dos cartas son iguales valen la mitad;");
        Textos.EscribirLinea("si las tres son iguales, se anulan.");
        Textos.EscribirLinea("");

        rondas = PreGame();
        clientes = new int[rondas];
        Clientes.CargarCompradores(clientes); // se generan los presupuestos

        for (int r = 1; r <= rondas; r++) {
            JugarRonda(r);
        }
        FinDelJuego();
    }

    private void Marcador() {
        Textos.Separador("MARCADOR");
        for (int i = 0; i < jugadores.length; i++) {
            Textos.EscribirLinea(colores[i] + nombres[i] + ": " + jugadores[i].getPuntos() + " pts" + Textos.RESET);
        }
        Textos.EscribirLinea("");
    }

    private void JugarRonda(int numero) {
        Comprador = clientes[numero - 1];
        Cl.clean();
        Textos.Titulo("RONDA " + numero + " DE " + rondas);
        Textos.EscribirLinea(Textos.AMARILLO + "Cliente con limite de: " + Textos.NEGRITA + Comprador + Textos.RESET);
        Textos.EscribirLinea("");
        Marcador();

        int[] jugadas = new int[jugadores.length];
        for (int i = 0; i < jugadores.length; i++) {
            jugadas[i] = jugadores[i].UsarCarta();
        }

        // fase de revision: 2 iguales = mitad, 3 iguales = se anulan
        int[] valores = new int[jugadores.length];
        for (int i = 0; i < jugadas.length; i++) {
            int repetidas = 0;
            for (int j = 0; j < jugadas.length; j++) {
                if (jugadas[j] == jugadas[i]) {
                    repetidas++; // se cuenta a si misma
                }
            }
            if (repetidas == 2) {
                valores[i] = jugadas[i] / 2;
            } else if (repetidas == 3) {
                valores[i] = 0;
            } else {
                valores[i] = jugadas[i];
            }
        }

        Textos.EscribirLinea("");
        Textos.Separador("CARTAS JUGADAS");
        for (int i = 0; i < jugadores.length; i++) {
            String extra = "";
            if (valores[i] == 0) {
                extra = Textos.ROJO + "  (anulada)" + Textos.RESET;
            } else if (valores[i] != jugadas[i]) {
                extra = Textos.AMARILLO + "  (repetida, vale " + valores[i] + ")" + Textos.RESET;
            }
            Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + " jugo: " + Textos.NEGRITA + jugadas[i] + Textos.RESET + extra);
        }

        // orden guarda los indices de los jugadores, de menor a mayor valor (burbuja)
        int[] orden = {0, 1, 2};
        for (int a = 0; a < orden.length - 1; a++) {
            for (int b = 0; b < orden.length - 1 - a; b++) {
                if (valores[orden[b]] > valores[orden[b + 1]]) {
                    int temp = orden[b];
                    orden[b] = orden[b + 1];
                    orden[b + 1] = temp;
                }
            }
        }

        Textos.EscribirLinea("");
        Textos.Separador("COMPRAS DEL CLIENTE");
        int suma = 0;
        for (int i = 0; i < orden.length; i++) {
            int idx = orden[i];
            if (valores[idx] == 0) {
                continue; // carta anulada, no se compra
            }
            if (suma + valores[idx] <= Comprador) {
                suma += valores[idx];
                jugadores[idx].setPuntos(jugadores[idx].getPuntos() + valores[idx]);
                Textos.EscribirLinea(Textos.VERDE + "+ Compra la de " + nombres[idx] + " (" + valores[idx] + ")" + Textos.RESET);
                Textos.EscribirLinea("  " + Textos.Barra(suma, Comprador));
            } else {
                Textos.EscribirLinea(Textos.ROJO + "x No le alcanza para la de " + nombres[idx] + " (" + valores[idx] + "). Se va." + Textos.RESET);
                break;
            }
        }

        Textos.Pausa("Presiona Enter para continuar...");
    }

    private void FinDelJuego() {
        Cl.clean();
        Textos.Titulo("FIN DEL JUEGO");
        Textos.EscribirLinea("");

        // orden de mayor a menor puntos (burbuja)
        int[] orden = {0, 1, 2};
        for (int a = 0; a < orden.length - 1; a++) {
            for (int b = 0; b < orden.length - 1 - a; b++) {
                if (jugadores[orden[b]].getPuntos() < jugadores[orden[b + 1]].getPuntos()) {
                    int temp = orden[b];
                    orden[b] = orden[b + 1];
                    orden[b + 1] = temp;
                }
            }
        }

        String[] lugares = {"1er lugar", "2do lugar", "3er lugar"};
        int maximo = jugadores[orden[0]].getPuntos();
        for (int i = 0; i < orden.length; i++) {
            int idx = orden[i];
            Textos.EscribirLinea(Textos.NEGRITA + lugares[i] + Textos.RESET + "  " + colores[idx] + nombres[idx] + Textos.RESET);
            Textos.EscribirLinea("   " + Textos.Barra(jugadores[idx].getPuntos(), maximo) + " pts");
        }

        Textos.EscribirLinea("");
        Textos.Color(Textos.AMARILLO + Textos.NEGRITA, "Ganador: " + nombres[orden[0]] + "!");
    }
}