public class Bots extends Artistas {//uso herencia considerando que necesito muchas
    // cosas de la clase jugadores?? En resumen, si.

    //investigue y descubri que se puede realizar una "sobreescitura de metodos"
    //usare eso para editar el metodo "UsarCarta" y aleatorizarlo.

    //obtiene una carta aleatoria, la revisa y si es posible, la juega
    @Override//lei que esta es una buena practica, asi que lo coloco
    public int UsarCarta() {
        boolean chequeo = false;
        int carta = 0;
        do {
            carta = Utils.ValorAleatorio(cartas.length, 1);//genera un valor de 1 a 12
            chequeo = CartaJugada(Integer.toString(carta));
        } while (chequeo == true); //si la carta ya fue jugada, se repite el ciclo hasta que se encuentre una carta valida

        cartasJugadas[carta - 1] = Integer.toString(carta); //marca la carta como jugada, se resta 1 para que entre en el indice correcto
        cartas[carta - 1] = Textos.tachar(cartas[carta - 1]);
        return carta;
    }
}