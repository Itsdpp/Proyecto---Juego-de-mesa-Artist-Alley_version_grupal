import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Artistas {
protected String[] cartas = {"1","2","3","4","5","6","7","8","9","10","11","12"};
protected String[] cartasJugadas= new String[cartas.length];
protected int puntos = 0;
private BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
//protected la mayoria, para que la clase Bots los pueda usar

//primera vez que uso buffered reader, que lee todo directamente en string siempre

    public int getPuntos() {
        return puntos;
    }//para saber cuantos puntos tiene cada jugador

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }//para acumular los puntos

    //Se encarga de toda la verificacion de la carta(si no fue jugada o valores apropiados)
    public int UsarCarta(){
        int carta;//integer debido a que guardara el indice de la carta usada
        boolean ver = true;
        do {
            ver = true;
            Textos.EscribirLinea("por favor, escoja un numero para jugar");
            Textos.Escribir("[" + cartas[0] + ",");
            for (int i = 1; i < cartas.length - 1; i++) {//no revisa el primer elemento ni el ultimo
                Textos.Escribir(cartas[i] + ",");//genera todo el string excepto el primero y ultimo
            }
            Textos.EscribirLinea(cartas[cartas.length - 1] + "]");//tuve que revisar para hacer eso, pero resumiendo toma el elemento final
            try {
                Textos.Escribir(Textos.AMARILLO + "> " + Textos.RESET);
                carta = Integer.parseInt(lector.readLine());//convierte el string directamente en un numero int
            } catch (IOException | NumberFormatException e) {//es posible hacer un multi-catch usando |, en estos casos, la e va al final.
                ver = false;
                Textos.EscribirLinea("Error de texto invalido, por favor escoja un numero.");
                continue;//lo reenvia a colocar un numero valido
            }
            if (carta > cartas.length || carta <= 0){//me aseguro que se coloque el indice de cartas correcto, si es mayor al indice o menor a 0
                ver = false;
            Textos.EscribirLinea("Por favor, escribir una carta valida");
            continue;//lo reenvia a colocar un numero valido

        }//fin del if

            else{//si pasa el verificador y los catchs, se comprueba su valor con el metodo "CartaJugada"
                boolean repetido = CartaJugada(String.valueOf(carta));//entra el valor cartas[valor de indice]
                //se resta uno, ya que de este forma entra en el indice correcto (1=index 0)
                //NOTA PERSONAL:
                // pensaba que debia ser un string[], pero como solo entra un valor de indice x
                //es string solamente.

                if (repetido==true){
                    ver = false;
                    Textos.EscribirLinea("Este numero ya lo jugo, porfavor escoja otro");
                    continue;//regresa al inicio
                }
                else//FINALMENTE LLEGUE AQUI, Y CON 1% DE IA NAMAS PARA INSTRUCCIONES Y FULL LOGICA, WOOOOO
                {
                    cartasJugadas[carta - 1] = String.valueOf(carta);//coloco el valor en una variable aparte
                    cartas[carta - 1] = Textos.tachar(cartas[carta - 1]);//se tacha el texto dentro del indice, indicando que fue usada.
                 return carta;
                }
            }

        }while (!ver);//un while simple, si entra a una excepcion se vuelve false, se repite
        // y se vuelve true.

        return 35505;//en caso de que de alguna manera llegue aca, el valor regresado sera 35505, "ERROR" en numeros
    }//fin clase jugar_carta

    //se encarga de revisar si la carta fue jugada o no
    protected boolean CartaJugada (String carta){//protected al solo ser usado en la clase bots y esta
        for (int i = 0; i < cartasJugadas.length; i++) {//revisa la ultima carta jugada, si es igual a la carta que se quiere jugar, envia true == numero repetido
            if (carta.equals(cartasJugadas[i])) {
                return true;
            }
        }

        return false;//si la condicion no se cumple, envia false == no numero repetido
    }
}
