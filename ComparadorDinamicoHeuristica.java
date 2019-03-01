/**
 *  La clase <tt>ComparadoDinamicoHeuristica</tt> representa el comparador usado en la
 *  cola de prioridad.
 *  <p>
 *  La cola de prioriodad usa la heuristica pasada como argumento para almacenar 
 *  listas de matrices de enteros que representan caminos
 *  <p>
 *
 *  @author 14-10946
 *  @author 14-10820
 */


import java.util.Comparator;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Stack;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.lang.*;
import java.util.PriorityQueue;

public class ComparadorDinamicoHeuristica implements Comparator<LinkedList<int[][]>>
{

    private static char compareBy = 'z';

    public ComparadorDinamicoHeuristica(char compareBy) {

        this.compareBy = compareBy;

    }

    public int compare(LinkedList<int [][]> x, LinkedList<int [][]> y)

    {

        int dimension = 3;
        int [][] estadoMeta = new int [dimension][dimension];

        for (int i = 0 ; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {

                estadoMeta[i][j] = dimension * i + j + 1;

            }
        }


        estadoMeta[2][2] = 0;
        char heuristicaux = compareBy;

        if (x.size() + h(heuristicaux, x.getLast(), estadoMeta) < y.size() + h(heuristicaux, y.getLast(), estadoMeta))
        {

            return -1;

        }

        if (x.size() + h(heuristicaux, x.getLast(), estadoMeta) > y.size() + h(heuristicaux, y.getLast(), estadoMeta))
        {

            return 1;

        }

        return 0;
    }

    /**
     * Retorna un entero que representa la heuristica de un estado
     *
     * @param un caracter que representa la heuristica a utilizar
     * @param una matriz de enteros que representa el estado al que se le quiere
     * obtener la heuristica
     * @param una matriz que representa el estado final utilizado para calcular
     * la heuristica
     * @return un entero que representa la heuristica del estado pasado como parametro
     */

    public static int h(char heuristicaux, int[][] estadoInicial, int[][] estadoMeta){

                    LinkedList<Integer> heuristica = new LinkedList<Integer>();
                    int dimension = estadoMeta.length;

                    if(heuristicaux == 'm'){

                        int x1 = 0;
                        int y1 = 0;
                        int x2 = 0;
                        int y2 = 0;

                        for (int i = 0; i < dimension*dimension; i++) {

                            if(i != 0){

                                for (int j = 0 ; j < dimension; j++) {

                                    for (int k = 0; k < dimension; k++) {

                                        if (estadoInicial[j][k] == i){

                                            x1 = j;
                                            y1 = k;

                                        }

                                        if (estadoMeta[j][k] == i){

                                            x2 = j;
                                            y2 = k;

                                        }
                                    }
                                    
                                }
                                    int suma = Math.abs(x1-x2) + Math.abs(y1-y2);
                                    heuristica.add(suma);
                            }

                        }

                    int total = 0;

                    for (int i = 0;i < heuristica.size();i ++ ) {

                        total = total + heuristica.get(i);

                    }

                    return total;

                 }


                if(heuristicaux == 'd'){

                        int x1 = 0;
                        int y1 = 0;
                        int x2 = 0;
                        int y2 = 0;
                        int total = 0;

                        for (int i = 0; i < dimension*dimension; i++) {

                            int suma = 0;

                            if(i != 0){

                                for (int j = 0 ; j < dimension; j++) {

                                    for (int k = 0; k < dimension; k++) {

                                        if (estadoInicial[j][k] == i){

                                            x1 = j;
                                            y1 = k;

                                        }

                                        if (estadoMeta[j][k] == i){

                                            x2 = j;
                                            y2 = k;

                                        }

                                    }
                                    
                                }

                                if( (x1 != x2) || (y1 != y2)){

                                    suma = suma +1;

                                }

                            }

                            total = total + suma;
                     }

                     return total;

                }

                if (heuristicaux == 'b') {

                    return 2;

                }
                        
                    return 0;
    }

}