import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReglasDelJuego {
    private BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    private Artistas[] jugadores = new Artistas[3]; // A1 (persona), A2 y A3 (automaticos)
    private String[] nombres = {"A1", "A2", "A3"};
    private String[] colores = {Textos.VERDE, Textos.CYAN, Textos.MAGENTA};
    private int rondas = 0; // cantidad de rondas elegida
    private int[] clientes; // presupuesto de cada cliente
    private int Comprador = 0; // presupuesto del cliente en la ronda actual
    private int rondasJugadas = 0; // rondas completadas
    private int rondasVentaA1 = 0; // rondas donde A1 logro vender
    private int[][] cartasRegistro = new int[3][9]; // carta jugada por artista en cada ronda
    private int[][] ingresosRegistro = new int[3][9]; // ingreso de cada artista en cada ronda

    // fase 0: se pregunta la cantidad de rondas (minimo 3, maximo 9)
    private int PreGame() {
        int cantidad = 0;
        boolean valido;
        do {
            valido = true;
            Textos.Escribir("Porfavor, escribir la cantidad de rondas (minimo 3, maximo 9, EXIT para salir): ");
            try {
                String entrada = lector.readLine().trim();
                if (entrada.equalsIgnoreCase("EXIT")) {
                    throw new SalirException();
                }
                cantidad = Integer.parseInt(entrada);
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
        boolean terminada = false;

        try {
            Cl.clean();
            Textos.Titulo("ARTIST ALLEY");
            Textos.Color(Textos.GRIS, "Juego de mesa - 1 jugador vs 2 bots");
            Textos.EscribirLinea("");
            Textos.EscribirLinea("Cada ronda llega un cliente con un limite de dinero.");
            Textos.EscribirLinea("Juegan una carta y el cliente compra de menor a mayor");
            Textos.EscribirLinea("hasta que la siguiente ya no le alcance.");
            Textos.EscribirLinea("Si dos cartas son iguales valen la mitad;");
            Textos.EscribirLinea("si las tres son iguales, se anulan.");
            Textos.EscribirLinea("Escribe EXIT en cualquier momento para salir.");
            Textos.EscribirLinea("");

            rondas = PreGame();
            clientes = new int[rondas];
            Clientes.CargarCompradores(clientes); // se generan los presupuestos

            for (int r = 1; r <= rondas; r++) {
                JugarRonda(r);
            }
            terminada = true;
        } catch (SalirException e) {
            Textos.EscribirLinea("");
            Textos.Color(Textos.ROJO, "Saliste de la partida.");
        }

        if (terminada) {
            ResumenFinal();
        }
        PantallaSalida(terminada);
    }

    // muestra los ingresos acumulados de cada artista
    private void Marcador() {
        Textos.Separador("INGRESOS ACUMULADOS");
        for (int i = 0; i < jugadores.length; i++) {
            Textos.EscribirLinea(colores[i] + nombres[i] + ": $" + jugadores[i].getPuntos() + Textos.RESET);
        }
        Textos.EscribirLinea("");
    }

    private void JugarRonda(int numero) {
        Comprador = clientes[numero - 1];
        Cl.clean();
        Textos.Titulo("RONDA " + numero + " DE " + rondas);
        Textos.EscribirLinea(Textos.AMARILLO + "Presupuesto del cliente: " + Textos.NEGRITA + "$" + Comprador + Textos.RESET);
        Textos.EscribirLinea("");
        Marcador();

        int[] jugadas = new int[jugadores.length];
        for (int i = 0; i < jugadores.length; i++) {
            jugadas[i] = jugadores[i].UsarCarta();
        }

        // revision: 2 iguales = mitad (redondeada hacia abajo), 3 iguales = se anulan
        int[] valores = new int[jugadores.length];
        boolean[] anulada = new boolean[jugadores.length];
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
                anulada[i] = true;
            } else {
                valores[i] = jugadas[i];
            }
        }

        Textos.EscribirLinea("");
        Textos.Separador("CARTAS JUGADAS");
        for (int i = 0; i < jugadores.length; i++) {
            String extra = "";
            if (anulada[i]) {
                extra = Textos.ROJO + "  (3 iguales, se anula)" + Textos.RESET;
            } else if (valores[i] != jugadas[i]) {
                extra = Textos.AMARILLO + "  (repetida, regateo: vale $" + valores[i] + ")" + Textos.RESET;
            }
            Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + " = $" + jugadas[i] + extra);
        }

        // orden guarda los indices de los artistas, de menor a mayor valor (burbuja)
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

        // el cliente compra de menor a mayor mientras le alcance el presupuesto
        int[] pagos = new int[jugadores.length];
        boolean[] vendio = new boolean[jugadores.length];
        int suma = 0;
        boolean seguir = true;
        for (int i = 0; i < orden.length; i++) {
            int idx = orden[i];
            if (!seguir || anulada[idx]) {
                continue;
            }
            if (suma + valores[idx] <= Comprador) {
                suma += valores[idx];
                pagos[idx] = valores[idx];
                vendio[idx] = true;
            } else {
                seguir = false; // los demas son mas caros, ya no alcanza
            }
        }

        // se guardan los ingresos y el registro de la ronda
        for (int i = 0; i < jugadores.length; i++) {
            jugadores[i].setPuntos(jugadores[i].getPuntos() + pagos[i]);
            cartasRegistro[i][numero - 1] = jugadas[i];
            ingresosRegistro[i][numero - 1] = pagos[i];
        }
        if (vendio[0]) {
            rondasVentaA1++;
        }
        rondasJugadas++;

        Textos.EscribirLinea("");
        Textos.Separador("RESULTADO");
        for (int i = 0; i < jugadores.length; i++) {
            if (vendio[i]) {
                Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + " " + Textos.VERDE + "VENDE por " + pagos[i] + Textos.RESET);
            } else {
                Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + " " + Textos.ROJO + "SIN VENTA" + Textos.RESET);
            }
        }
        Textos.EscribirLinea("Presupuesto usado: " + Textos.Barra(suma, Comprador));
        Textos.EscribirLinea("");
        Marcador();

        Textos.Pausa("Presiona Enter para continuar...");
    }

    // devuelve el ganador, o EMPATE si hay igualdad en el ingreso mas alto
    private String Ganador() {
        int max = 0;
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getPuntos() > max) {
                max = jugadores[i].getPuntos();
            }
        }
        String lista = "";
        int cuantos = 0;
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getPuntos() == max) {
                if (cuantos > 0) {
                    lista += " y ";
                }
                lista += nombres[i];
                cuantos++;
            }
        }
        if (cuantos > 1) {
            return "EMPATE entre " + lista;
        }
        return lista;
    }

    // resumen cuando la partida termino
    private void ResumenFinal() {
        Cl.clean();
        Textos.Titulo("RESUMEN DE LA PARTIDA");
        Textos.EscribirLinea("");
        Textos.EscribirLinea("Rondas jugadas: " + rondasJugadas);
        Textos.EscribirLinea("Rondas donde A1 logro vender: " + rondasVentaA1);
        Textos.EscribirLinea("");
        Textos.Separador("INGRESOS ACUMULADOS");
        int maximo = 0;
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i].getPuntos() > maximo) {
                maximo = jugadores[i].getPuntos();
            }
        }
        for (int i = 0; i < jugadores.length; i++) {
            Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET + ": $" + jugadores[i].getPuntos());
            Textos.EscribirLinea("   " + Textos.Barra(jugadores[i].getPuntos(), maximo));
        }
        Textos.EscribirLinea("");
        Textos.Color(Textos.AMARILLO + Textos.NEGRITA, "Ganador: " + Ganador());
    }

    // pantalla que se muestra al terminar o al salir con EXIT
    private void PantallaSalida(boolean terminada) {
        Textos.EscribirLinea("");
        Textos.Titulo("FIN DEL PROGRAMA");

        if (clientes != null) {
            Textos.Separador("LISTA DE CLIENTES");
            String lista = "";
            for (int i = 0; i < clientes.length; i++) {
                lista += "$" + clientes[i];
                if (i < clientes.length - 1) {
                    lista += ", ";
                }
            }
            Textos.Color(Textos.AMARILLO, lista);
            Textos.EscribirLinea("");
        }

        Textos.Separador("REGISTRO DE RONDAS");
        for (int i = 0; i < jugadores.length; i++) {
            Textos.EscribirLinea(colores[i] + nombres[i] + Textos.RESET);
            for (int r = 0; r < rondasJugadas; r++) {
                Textos.EscribirLinea("  Ronda " + (r + 1) + ": producto $" + cartasRegistro[i][r] + ", ingreso $" + ingresosRegistro[i][r]);
            }
            Textos.EscribirLinea("  Total: $" + jugadores[i].getPuntos());
        }
        Textos.EscribirLinea("");

        if (terminada) {
            Textos.Color(Textos.AMARILLO + Textos.NEGRITA, "Ganador: " + Ganador());
        } else {
            Textos.Color(Textos.ROJO + Textos.NEGRITA, "PARTIDA INTERRUMPIDA");
        }
    }
}