public class Bots extends Artistas {//uso herencia considerando que necesito muchas
    // cosas de la clase jugadores?? En resumen, si.

    //investigue y descubri que se puede realizar una "sobreescitura de metodos"
    //usare eso para editar el metodo "UsarCarta" y aleatorizarlo.

    //obtiene una carta aleatoria, la revisa y si es posible, la juega
    @Override//lei que esta es una buena practica, asi que lo coloco
    public int UsarCarta(){
        boolean chequeo=false;
        String verificador;
        do {
            verificador= Integer.toString(Utils.ValorAleatorio(cartas.length, 0));//como quiero cartas.length, en vez de private uso protected en Artistas
            //no se resta 1 para que escoja cualquier valor del indice
            chequeo = CartaJugada(verificador);//debo convertirlo, debido a que el metodo CartaJugada usa un String
        } while(chequeo==false);
        return Integer.parseInt(verificador);//envio el valor convertido como indice
    }
//se que se genera una X cantidad de valores hasta encontrar uno valido, pero diria que cumple la funcion y eso me basta de momento
//quizas la proxima vez que chequee esto lo mejore

    }
