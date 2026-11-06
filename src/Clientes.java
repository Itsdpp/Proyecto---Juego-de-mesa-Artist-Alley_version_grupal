public class Clientes {
    //en una clase estatica, incluso las variables deben ser static
    static int[] cartas ={9,12,15,16,18,20,22,23,24,25,26,30};//arreglo de 12, de los valores disponibles para los clientes

    //realmente no se llaman compradores, pero como lo nombre incialmente asi y funciona (tambien que me da pereza arreglarlo), lo dejo asi
    static void CargarCompradores(int[] compradores){
        Utils.InicializarArreglo(compradores);//limpia el arreglo y lleva cada indice a 0
        int verNum=0;//verificador del numero
        boolean verDupe=false;//confirma si el numero es un duplicado o no
        for (int i=0;i<compradores.length;i++){//repetir hasta rellenar compradores
            do{
                verNum = Utils.ValorAleatorio(cartas.length,0);//genera un valor aleatorio entre 0 y el tamaño del indice de cartas(por si se quisieran poner mas)
                verDupe= Utils.VerificadorDobles(verNum,compradores);//se verifica que el numero no este duplicado
            }while(verDupe==false);//mientras el verificador sea false, se repite
            compradores[i]=verNum;//cuando se encuentra un valor, sale del ciclo y se asigna ese valor al indice
        }
            /* resumiendo vagamente lo de arriba:
                           verNum almacena un valor aleatorio
                           verDupe verifica si el numero ya fue escogido y almacena un resultado de true or false, si es true:
                           se asigna verNum a la variables de compradores con el indice actual. Si es false se repite lo anterior.
                           Se repite lo anterior hasta que termine el ciclo for
             */
    }//el metodo termina con este
}
