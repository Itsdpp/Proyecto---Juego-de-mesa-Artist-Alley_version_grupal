import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Artistas {
    protected String[] cartas = {"1","2","3","4","5","6","7","8","9","10","11","12"}; // mano del artista
    protected String[] cartasJugadas = new String[cartas.length]; // cartas ya usadas
    protected int puntos = 0; // ingresos acumulados
    private BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    // pide una carta al jugador y valida: rango, texto, carta repetida y EXIT
    public int UsarCarta() {
        while (true) {
            Textos.EscribirLinea("por favor, escoja un numero para jugar (EXIT para salir)");
            Textos.Escribir("[" + cartas[0] + ",");
            for (int i = 1; i < cartas.length - 1; i++) {
                Textos.Escribir(cartas[i] + ",");
            }
            Textos.EscribirLinea(cartas[cartas.length - 1] + "]");

            int carta;
            try {
                Textos.Escribir(Textos.AMARILLO + "> " + Textos.RESET);
                String entrada = lector.readLine().trim();
                if (entrada.equalsIgnoreCase("EXIT")) {
                    throw new SalirException();
                }
                carta = Integer.parseInt(entrada);
            } catch (IOException | NumberFormatException e) {
                Textos.EscribirLinea("Error de texto invalido, por favor escoja un numero.");
                continue;
            }

            if (carta < 1 || carta > cartas.length) {
                Textos.EscribirLinea("Por favor, escribir una carta valida");
                continue;
            }

            if (CartaJugada(String.valueOf(carta))) {
                Textos.EscribirLinea("Este numero ya lo jugo, porfavor escoja otro");
                continue;
            }

            cartasJugadas[carta - 1] = String.valueOf(carta);
            cartas[carta - 1] = Textos.tachar(cartas[carta - 1]);
            return carta;
        }
    }

    // revisa si la carta ya fue jugada
    protected boolean CartaJugada(String carta) {
        for (int i = 0; i < cartasJugadas.length; i++) {
            if (carta.equals(cartasJugadas[i])) {
                return true;
            }
        }
        return false;
    }
}