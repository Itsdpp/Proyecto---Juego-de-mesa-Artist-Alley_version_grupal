public class Bots extends Artistas {//uso herencia considerando que necesito muchas
    // cosas de la clase jugadores?? En resumen, si.

    //investigue y descubri que se puede realizar una "sobreescitura de metodos"
    //usare eso para editar el metodo "UsarCarta" y aleatorizarlo.

    //obtiene una carta aleatoria, la revisa y si es posible, la juega
    @Override//lei que esta es una buena practica, asi que lo coloco
    public int UsarCarta(){
        boolean chequeo=false;
        do {
            String verificador = Integer.toString(Utils.ValorAleatorio(cartas.length, 0));//como quiero cartas.length, en vez de private uso protected en Artistas
            //no se resta 1 para que escoja cualquier valor del indice
            chequeo = CartaJugada(verificador);
        } while(chequeo==false);
        return
    }
//se que se genera una X cantidad de valores hasta encontrar uno valido, pero diria que cumple la funcion y eso me basta de momento
//quizas la proxima vez que chequee esto lo mejore

    }
