public class Utils {
    public static int ValorAleatorio(int max, int min){//editar para que sea general
                //para explicacion de este segmento en un txt aparte(que envidia no haber pensado en algo como eso, pero no conocia
                // la funcion math.floor y mi int matematica no es tan buena)
                return (int) (Math.floor(Math.random() * ((max) - (min) + 1)) + (min));//obligatorio usar int, aunque floor
                // int evita decimales
            }
    public static boolean VerificadorDobles(int verNum, int[] ar){//verNum=verificadorNumeros  ar=arreglo
        for (int i = 0; i<ar.length; i++){
            if (ar[i]==verNum)//condicional= si el valor que se almacena en ese indice es igual al valor de verNum, se envia true==hay un duplicado
                return true;
        }
        return false;
    }
    public static void InicializarArreglo(int[]arreglo){//se asegura que el arreglo no tenga valores aleatorios y todo comienzen en 0
        for (int i=0;i<arreglo.length;i++){
            arreglo[i]=0;
        }
    }
    }
