import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ReglasDelJuego {
    private BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
    private Artistas jugador= new Artistas();//cantidad de jugadores
    private Bots[] bots=new Bots[2];//cantidad de bots, como se especifico, juegan solo 2
    private int rondas= 0;//con esto se llevaran las rondas del juego
    private int[] clientes = new int[PreGame()];//genero un arreglo que guarde la cantidad de clientes, con el tamaño de metodos(permitiendo que el arreglo tenga cualquier tamaño, siempre y cuando se adapte el metodo)
//fase 0 del juego, se pregunta la cantidad de rondas(esta hecho para generar un arreglo de cualquier valor, pero como se instruye en la tarea, solo se genera uno de tamaño 3-9)
    private int PreGame(){
        do {
            Textos.Escribir("Porfavor, escribir la cantidad de rondas(minimo 3, maximo 9): ");
        try{
            rondas= Integer.parseInt(lector.readLine());
        } catch (IOException e) {
            Textos.EscribirLinea("Porfavor, escribir un numero");
            throw new RuntimeException(e);
        }
        if (rondas<3||rondas>9) Textos.EscribirLinea("Por favor, escribir un numero dentro de los limites");
        } while(rondas<3||rondas>9);
        return rondas;
    }
    public void Juego(){
        int[] cartasJugadas = new int[3];// 3 al solo tener 3 valores a guardar, el de los bots y el del jugador(si quisiera mas personas hacer cambios y no tengo ganas de hacer la logica para que sea dinamico)
        PreGame();//se realiza el metodo superior
        Clientes.CargarCompradores(clientes);//se generan los valores de los compradores
        for(int i=1; i<rondas; i++) {
            Textos.EscribirLinea("ronda " + i);
            Textos.EscribirLinea("Presupuesto del Comprador "+ i + ":" + clientes[i-1]);//tenia 2 opciones: i=0  y sumaba en los otros valores o i=1 y solo restaba en este, opino que la decision es obvia
            //como el jugador es solo 1 y no tengo que matarme con la logica de distincion entre jugador 2 o mas o con reconocer cada bot, tomo el camino facil
            //Nota personal: No tienen NI IDEA de la satisfaccion que da solo escribir esas 3 lineas

            cartasJugadas[0]= jugador.UsarCarta();//guarda el valor de la carta del jugador
            cartasJugadas[1]= bots[0].UsarCarta();//guarda el valor de la carta del bot 1
            cartasJugadas[2]= bots[1].UsarCarta();//guarda el valor de la carta del bot 2

            //reviso que los valores cumplan sus respectivas funciones(2 iguales= mitad del valor, 3 iguales=se eliminan)
            FaseRevision(cartasJugadas,clientes[i-1]);
            FaseCompras(cartasJugadas,clientes[i-1],jugador,bots );
        }
    }

    //metodo que revisa que los valores cumplan sus respectivas funciones
    //traigo los valores que usaron los jugadores y el presupuesto del cliente
    private void FaseRevision(int[] cartas, int presupuesto){
        //ordeno los valores con esta instruccion, que hace que los valores se ordenen de menor a mayor
        //tuve que buscar este metodo, porque hacerla manualmente me tomaria demasiado hacer la logica
        Arrays.sort(cartas);

        //verifico que hay un doble(honestamente no planee este metodo para esta funcion, pero me ahorra el tiempo de busqueda)
        for (int i=0; i<cartas.length; i++){
            boolean verificador = false;
            //cartas[i] es el valor a verificar, cartas es el arreglo que posee el resto de numeros
            verificador = Utils.VerificadorDobles(cartas[i], cartas);
            if (verificador= true)
                //divide el valor entre 2 y usando floor, lo redondea hacia abajo
                cartas[i]= Math.floorDiv(cartas[i],2);

        }
        //me tome un rato pensando como puedo verificar que (en este caso) los 3 valores no sean iguales, luego me di cuenta que me estaba ahogando en un vaso de agua
        //en caso de que los 3 valores sean iguales, el arreglo regresa a 0
        if ((cartas[0]== cartas[1]) && (cartas[1]== cartas[2]))
            //reutilizo este codigo, porque hace lo que quiero justamente
            Utils.InicializarArreglo(cartas);
    }
    //Fase en la cual el cliente comienza a comprar las pinturas
    //Prefiero intentar simplificar el proceso, usando el hecho de que solo son 2 bots y 1 persona
    private void FaseCompras(int[] cartas, int presupuesto, Artistas jugador, Bots bot){
        for (int i = 0; i<cartas.length; i++){
            //como los valores ya deberian estar ordenados por el metodo anterior, voy directo a la condicional

            //Si el presupuesto es menor o igual/alcanza para comprar carta
            if (presupuesto<=cartas[i]){
                
            }
        }

    }
}
