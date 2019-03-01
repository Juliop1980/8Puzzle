/**
 *  La clase <tt>Puzzle8</tt> representa la solucion al rompecabezas 8 Puzzle.
 *  El problema se resume en encontrar la minima cantidad de movimientos para
 *  llegar de un estado inicial a un estado final especificado.
 *  <p>
 *  Esta solucion es basada en el algoritmo de A* del libro de Oscar Meza 
 *  y Maruja H. Ortega, Grafos y Algoritmos (1993)
 *  <p>
 *
 *  @author 14-10946
 *  @author 14-10820
 */

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
import java.util.Comparator;
import java.util.PriorityQueue;

public class Puzzle8{
	public static void main(String[] args) {
		try {

			if(args.length != 2){

				System.out.println("Ha introducido parametros incompletos");

			}

			String heuristicaux1 = (args[0]);
			char heuristicaux = heuristicaux1.charAt(0);
			String dirArchivo = args[1];

			// se usaran matrices de enteros para representar los estados

	        FileReader fr = new FileReader(dirArchivo);
	        BufferedReader br = new BufferedReader(fr);
	        String sCurrentLine;
	        sCurrentLine = br.readLine();
	        String [] nodos = sCurrentLine.split(" ");
	        int dimension = nodos.length;
	        int [][] estadoInicial = new int[dimension][dimension];

	        for (int i = 0 ; i < dimension; i++) {
	        	for (int j = 0; j < dimension ; j++) {

	        		estadoInicial[i][j] = Integer.parseInt(nodos[j]);

	        	}

	        	sCurrentLine = br.readLine();
	        	if(i!= 2){
	        		nodos = sCurrentLine.split(" ");
	       		}
	        }

	        // se carga la meta, estado al que se quiere llegar

	        int [][] estadoMeta = new int [dimension][dimension];
	        for (int i = 0 ; i < dimension; i++) {
	        	for (int j = 0; j < dimension; j++) {

	        		estadoMeta[i][j] = dimension * i + j + 1;

	        	}
	        }


	        estadoMeta[2][2] = 0;

	       	  // primero hay que detectar si se puede resolver
		    if(seResuelve(estadoInicial) == false){

		    	System.out.println("No hay solución");
		    }

		    // LLamada a funcion A* con costos de camino 1
		    else{

		    	//la ultima matriz corresponde a los estados abiertos
		    	long t1 = System.currentTimeMillis();
		     	LinkedList<int[][]> camino = A(estadoInicial, heuristicaux, estadoMeta);
		     	long t2 = System.currentTimeMillis();
		     	int [][] estados = camino.getLast();
		     	int numEstados = estados[0][0];
		     	int pasos = 0;

		     	for (int i = 0; i < camino.size() - 1 ; i ++) {

		     		int [][] estadoAux = camino.get(i);
		     		pasos++;

		     		for (int k = 0; k < estadoAux.length; k++) {

   						 for (int j = 0; j < estadoAux[k].length; j++) {

       						System.out.print(estadoAux[k][j] + " ");

					   	 }

					   	 System.out.println();

					}

					System.out.println();

		     	}

		     	System.out.println("Numero de estados abiertos: " + numEstados);
		     	System.out.print("Tiempo: " + (t2-t1) +" ms" );

			 }


			}

   		 catch(FileNotFoundException excepcion){

       		 System.out.println("No se encontro el archivo introducido ");

   		 }

    	catch(IOException excepcion){

       		 System.out.print("Hay un error desconocido ");

   		 }		

	}

	/**
	 * Retorna una lista de matrices de enteros que reprensentan los estados que forman
	 * parte del camino minimo entre el estado origen y el estado meta
	 * Complejidad ((|V|+|E|).log2|V|)
	 *
	 * @param una matriz de enteros que representa el origen
	 * @param un caracter que representa la heuristica a utilizar
	 * @param una matriz de enteros que representa el estado al cual se quiere llegar
	 * @return Retorna una lista de matrices de enteros que reprensentan los estados que forman
	 * parte del camino minimo entre el estado origen y el estado meta
	 */

	public static LinkedList<int[][]> A(int [][] origen, char heuristicaux, int [][] estadoMeta){

		ComparadorDinamicoHeuristica comparator = new ComparadorDinamicoHeuristica(heuristicaux);
		PriorityQueue<LinkedList<int[][]>> Abiertos = new PriorityQueue<LinkedList<int[][]>>(10000, comparator);

		// costo entre estados adyacentes 1
		int contador = 1;
		LinkedList<int [][]> resultado = new LinkedList<int [][]>();

		// se guardan todos los estados explorados para no volver a abrir uno explorado

		LinkedList<int[][]> estadosExplorados = new LinkedList<int [][]>();
		LinkedList<LinkedList<int [][]>> Cerrados = new LinkedList<LinkedList<int [][]>>();
		LinkedList<int[][]> inicio = new LinkedList<int[][]>();
		inicio.add(origen);
		estadosExplorados.add(origen);
		Abiertos.add(inicio);

		while(Abiertos.size() > 0){

			LinkedList<int [][]> AbiertoAux = Abiertos.remove();

	        if(Arrays.deepEquals(AbiertoAux.getLast(), estadoMeta)){
	        	resultado = AbiertoAux;
	        	// aqui guardo el numero de estados abiertos
	        	int[][] ultimo = new int[1][1];
	        	ultimo[0][0] = contador;
	        	AbiertoAux.add(ultimo);
	        	return AbiertoAux;
	        }

	        else{

	        		LinkedList<int[][]> adyacentesaux = sucesores(AbiertoAux.getLast());
					LinkedList<LinkedList<int[][]>> CadenasExp = new LinkedList<LinkedList<int[][]>>();
					Iterator<int[][]> iterador = adyacentesaux.iterator();

					while (iterador.hasNext()) {

						LinkedList<int[][]> cadenaux = new LinkedList<int[][]>();
		                cadenaux.addAll(AbiertoAux);
		                int[][] a = iterador.next();
		                cadenaux.add(a);
		                CadenasExp.add(cadenaux);

	            	}

	            	for (int j = 0 ; j < CadenasExp.size() ; j++) {

			        	LinkedList<int[][]> expAux = CadenasExp.get(j);
			        	LinkedList<int[][]> exploradosAux = new LinkedList<int[][]>();
			        	exploradosAux.add(expAux.getLast());
			        	LinkedList<int[][]> prueba = new LinkedList<int[][]>();
			        	LinkedList<int[][]> esta = estaA(Cerrados, Abiertos, expAux);

			        	if (esta.size() != 0){

			        		if (expAux.size() - 1 < esta.size() - 1){

			        			int indice = Cerrados.indexOf(esta);

			        			if(indice != -1){

			        				Cerrados.remove(indice);

			        			}

			        				Abiertos.remove(esta);
			        				Abiertos.add(expAux);



			        		}

			        	}

			        	if (esta.size() == 0){

							if(contains(estadosExplorados,expAux.getLast()) == false){

			        			Abiertos.add(expAux);
			        			estadosExplorados.add(expAux.getLast());
			        			contador++;

			        		}
			        	}

					}
			}	


		}

		return resultado;

	}

	/**
	 * Retorna una lista de matrices de enteros que representa
	 * un camino cuyo vertice final es igual al vertice final del camino
	 * recibido en los parametros, sino existe devuelve un camino vacio.
	 * Complejidad (|Abiertos|)
	 *
	 * @param una lista de listas de matrices de enteros que representan los caminos cerrados
	 * @param una cola de prioridad de listas de matrices de enteros que representan los caminos abiertos
	 * @param una lista de matrices de enteros que representa un camino
	 * @return una lista de matrices de enteros que representa
	 * un camino cuyo vertice final es igual al vertice final del camino
	 * recibido en los parametros, sino existe devuelve un camino vacio.
	 */

	public static LinkedList<int[][]> estaA(LinkedList<LinkedList<int[][]>> Cerrados, PriorityQueue<LinkedList<int[][]>> Abiertos, LinkedList<int[][]> cadena ){

		LinkedList<int[][]> falsa = new LinkedList<int[][]>();
		int [][] vertice = cadena.getLast();
	    Iterator<LinkedList<int[][]>> it = Abiertos.iterator();

	    while (it.hasNext()) {

	      	LinkedList<int[][]> priorAux = it.next();

			if (Arrays.deepEquals(priorAux.getLast(),vertice)){

				return priorAux;

			}

		}

		for (int i = 0; i < Cerrados.size() ; i++ ) {

			if (Arrays.deepEquals(Cerrados.get(i).getLast(),vertice)){

				return Cerrados.get(i);

			}

		}

		return falsa;

	}


	/**
	 * Retorna un booleano que representa si dado un estado inicial
	 * es posible llegar al estado final
	 *
	 * @param una matriz de enteros que representa un estado inicial 
	 * @return un booleano que representa si dado un estado inicial
	 * es posible llegar al estado final
	 */

	public static boolean seResuelve(int[][] estadoInicial){

		int dimension = estadoInicial.length;
	 	int inversiones = 0;

		for (int i = 0; i< dimension; i++) {
			for (int j = 0; j < dimension ; j ++) {

				int num = estadoInicial[i][j];
				int sum = 0;
				int l = (j + 1)%(dimension);
				int k = i;

				if (l == 0) {

					k = k + 1;

				}

				while(l <dimension && k != dimension){

					if ((estadoInicial[k][l] != 0)&& (estadoInicial[k][l] < num)){

						sum = sum + 1;

					}

					l++;

					}

					k = k +1;

				while(k < dimension){
					for (int u = 0; u < dimension ; u++) {

						if ((estadoInicial[k][u] != 0)&& (estadoInicial[k][u] < num)){

							sum = sum + 1;

							}

					}

					k++;

				}

				inversiones = inversiones + sum;
			}
		}	

		if(inversiones%2 == 0){

			return true;

		}

		return false;
	}


	/**
	 * Retorna una lista de matrices de enteros que representan los 
	 * estados sucesores del estado pasado como parametro
	 *
	 * @param una matriz de enteros que representa un estado especifico
	 * @return una lista de matrices de enteros que representan los 
	 * estados sucesores del estado pasado como parametr
	 */


	public static LinkedList<int[][]> sucesores(int[][] vertice){

		int dimension = vertice.length;
		LinkedList<int[][]> sucesores = new LinkedList<int[][]>();
		int [][] verticeAux = new int[dimension][];

		for(int i = 0; i < dimension; i++)
	   	 verticeAux[i] = vertice[i].clone();

		int [][] verticeAux1 = new int[dimension][];

		for(int i = 0; i < dimension; i++)
	    verticeAux1[i] = vertice[i].clone();
		
		int [][] verticeAux2 = new int[dimension][];

		for(int i = 0; i < dimension; i++)
	    verticeAux2[i] = vertice[i].clone();

		int [][] verticeAux3 = new int[dimension][];

		for(int i = 0; i < dimension; i++)
	    verticeAux3[i] = vertice[i].clone();

		int x = 0;
		int y = 0;

		for (int i = 0; i < dimension; i ++) {

			for (int j = 0; j < dimension ; j++) {

				if(vertice[i][j] == 0){

					x = i;
					y = j;

				}

			}
		}
		//casos
		// parte de arriba
		if(x == 0){
		//esquina superior izquierda

					if(y == 0){

							verticeAux[0][0] = verticeAux[1][0];
							verticeAux[1][0] = 0;
							sucesores.add(verticeAux);
					    	verticeAux1[0][0] = verticeAux1[0][1];
							verticeAux1[0][1] = 0;
							sucesores.add(verticeAux1);

					}

					else if(y == dimension-1){

							verticeAux[0][dimension-1] = verticeAux[0][dimension-2];
							verticeAux[0][dimension-2] = 0;
							sucesores.add(verticeAux);
					    	verticeAux1[0][dimension-1] = verticeAux1[1][dimension-1];
							verticeAux1[1][dimension-1] = 0;
							sucesores.add(verticeAux1);
					}

					else{

							verticeAux[x][y] = verticeAux[x][y - 1];
							verticeAux[x][y-1] = 0;
							sucesores.add(verticeAux);
					    	verticeAux1[x][y] = verticeAux1[x][y + 1];
							verticeAux1[x][y + 1] = 0;
							sucesores.add(verticeAux1);
					    	verticeAux2[x][y] = verticeAux2[x + 1][y];
							verticeAux2[x + 1][y] = 0;
							sucesores.add(verticeAux2);

					}

		}

		// parte de abajo

		else if(x == dimension-1){

		// esquina inferior izquierda

			if(y == 0){

				verticeAux[dimension-1][0] = verticeAux[dimension-2][0];
				verticeAux[dimension-2][0] = 0;
				sucesores.add(verticeAux);
		    	verticeAux1[dimension-1][0] = verticeAux1[dimension-1][1];
				verticeAux1[dimension-1][1] = 0;
				sucesores.add(verticeAux1);

			}

			// esquina inferior derecha

			else if(y == dimension-1){

				verticeAux[dimension-1][dimension-1] = verticeAux[dimension-2][dimension-1];
				verticeAux[dimension-2][dimension-1] = 0;
				sucesores.add(verticeAux);	
		    	verticeAux1[dimension-1][dimension-1] = verticeAux1[dimension-1][dimension-2];
				verticeAux1[dimension-1][dimension-2] = 0;
				sucesores.add(verticeAux1);
		}

			else{

				verticeAux[x][y] = verticeAux[x][y - 1];
				verticeAux[x][y-1] = 0;
				sucesores.add(verticeAux);
		    	verticeAux1[x][y] = verticeAux1[x][y + 1];
				verticeAux1[x][y + 1] = 0;
				sucesores.add(verticeAux1);
		    	verticeAux2[x][y] = verticeAux2[x - 1][y];
				verticeAux2[x - 1][y] = 0;
				sucesores.add(verticeAux2);
			}

		}

		else if(y == 0){

				verticeAux[x][y] = verticeAux[x - 1][y];
				verticeAux[x - 1][y] = 0;
				sucesores.add(verticeAux);
		    	verticeAux1[x][y] = verticeAux1[x + 1][y];
				verticeAux1[x + 1][y] = 0;
				sucesores.add(verticeAux1);
		    	verticeAux2[x][y] = verticeAux2[x][y + 1];
				verticeAux2[x][y + 1] = 0;
				sucesores.add(verticeAux2);
		}


		else if(y == dimension-1){

				verticeAux[x][y] = verticeAux[x - 1][y];
				verticeAux[x - 1][y] = 0;
				sucesores.add(verticeAux);
		    	verticeAux1[x][y] = verticeAux1[x + 1][y];
				verticeAux1[x + 1][y] = 0;
				sucesores.add(verticeAux1);
		    	verticeAux2[x][y] = verticeAux2[x][y - 1];
				verticeAux2[x][y - 1] = 0;
				sucesores.add(verticeAux2);

		}

		else {

				verticeAux[x][y] = verticeAux[x - 1][y];
				verticeAux[x - 1][y] = 0;
				sucesores.add(verticeAux);
		    	verticeAux1[x][y] = verticeAux1[x + 1][y];
				verticeAux1[x + 1][y] = 0;
				sucesores.add(verticeAux1);
		    	verticeAux2[x][y] = verticeAux2[x][y - 1];
				verticeAux2[x][y - 1] = 0;
				sucesores.add(verticeAux2);
		    	verticeAux3[x][y] = verticeAux3[x][y + 1];
				verticeAux3[x][y + 1] = 0;
				sucesores.add(verticeAux3);
		}

		return sucesores;

	}

	/**
	 * Retorna un booleano que indica si un estado se encuentra en una lista de estados
	 *
	 * @param una lista de matrices de enteros que representa todos los estados esplorados
	 * @param un estado en especifico
	 * @return un booleano que indica si un estado se encuentra en una lista de estados
	 */

	public static boolean contains(LinkedList<int[][]> l,  int[][] arr){

	    for(int[][] array : l){

	        if(Arrays.deepEquals(array, arr)){

	            return true;

	        }

	    }
	    
	    return false;
	}


}