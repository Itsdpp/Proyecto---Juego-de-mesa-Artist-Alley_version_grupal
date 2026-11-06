public class ReglasDelJuego {
    private Artistas[] jugadores = new Artistas[3]; // 1 persona y 2 bots
    private String[] nombres = {"Tu", "Bot 1", "Bot 2"};
    private String[] colores = {Textos.VERDE, Textos.CYAN, Textos.MAGENTA};
    private Clientes clientes = new Clientes();
    private int rondas = 0;
    private int Comprador = 0; // limite de compra del cliente

    public void InicioJuego() {
        jugadores[0] = new Artistas();
        jugadores[1] = new Bots();
        jugadores[2] = new Bots();
        clientes.CargarCompradores();

        Cl.clean();
        Textos.Titulo("ARTIST ALLEY");
        Textos.Color(Textos.GRIS, "Juego de mesa - 1 jugador vs 2 bots");
        Textos.EscribirLinea("");
        Textos.EscribirLinea("Cada ronda llega un cliente con un limite de dinero.");
        Textos.EscribirLinea("Juegan una carta y el cliente compra de menor a mayor");
        Textos.EscribirLinea("hasta que la siguiente ya no le alcance.");
        Textos.Pausa("Presiona Enter para comenzar...");

        for (rondas = 1; rondas <= clientes.compradores.length; rondas++) {
            JugarRonda();
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

    private void JugarRonda() {
        Comprador = clientes.compradores[rondas - 1];
        Cl.clean();
        Textos.Titulo("RONDA " + rondas + " DE " + clientes.compradores.length);
        Textos.EscribirLinea(Textos.AMARILLO + "Cliente con limite de: " + Textos.NEGRITA + Comprador + Textos.RESET);
        Textos.EscribirLinea("");
        Marcador();

        int[] jugadas = new int[jugadores.length];
        for (int i = 0; i < jugadores.length; i++) {
            jugadas[i] = jugadores[i].UsarCarta();
        }

        Textos.EscribirLinea("");
        Textos.Separador("CARTAS JUGADAS");
        for (int i = 0; i < jugadores.length; i++) {
            Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + " jugo: " + Textos.NEGRITA + jugadas[i] + Textos.RESET);
        }

        // orden guarda los indices de los jugadores, de menor a mayor carta (burbuja)
        int[] orden = {0, 1, 2};
        for (int a = 0; a < orden.length - 1; a++) {
            for (int b = 0; b < orden.length - 1 - a; b++) {
                if (jugadas[orden[b]] > jugadas[orden[b + 1]]) {
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
            if (suma + jugadas[idx] <= Comprador) {
                suma += jugadas[idx];
                jugadores[idx].setPuntos(jugadores[idx].getPuntos() + jugadas[idx]);
                Textos.EscribirLinea(Textos.VERDE + "+ Compra la de " + nombres[idx] + " (" + jugadas[idx] + ")" + Textos.RESET);
                Textos.EscribirLinea("  " + Textos.Barra(suma, Comprador));
            } else {
                Textos.EscribirLinea(Textos.ROJO + "x No le alcanza para la de " + nombres[idx] + " (" + jugadas[idx] + "). Se va." + Textos.RESET);
                break;
            }
        }

        Textos.Pausa("Presiona Enter para la siguiente ronda...");
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