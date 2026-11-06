public class Clientes {
    int[] cartas ={9,12,15,16,18,20,22,23,24,25,26,30};//arreglo de 12, de los valores disponibles para los clientes
    int[] compradores= new int[9];//numero maximo de rondas

    void CargarCompradores(){
        Utils.InicializarArreglo(compradores);//limpia el arreglo y lleva cada indice a 0
        int verNum=0;
        boolean verDupe=false;
        for (int i=0;i<compradores.length;i++){//repetir hasta generar los valores de los compradores
            do{
                verNum = cartas[Utils.ValorAleatorio(cartas.length - 1, 0)];//genera un valor aleatorio entre 0 y 11
                verDupe = Utils.VerificadorDobles(verNum, compradores);//se verifica el numero

            }while(verDupe==true);//mientras el verificador sea true, se repite
            compradores[i]=verNum;//cuando se encuentra un valor, sale del ciclo y se asigna ese valor al indice
        }
          
    }
}
                /* resumiendo vagamente lo de arriba:
                           verNum almacena el valor de una carta de clientes escogida al azar
                           verDupe verifica si el numero ya fue escogido y almacena un resultado de true or false, si es false:
                           se asigna verNum a la variables de compradores con el indice actual. Si es true se repite lo anterior.
                           Se repite lo anterior hasta que termine el ciclo for
             */
