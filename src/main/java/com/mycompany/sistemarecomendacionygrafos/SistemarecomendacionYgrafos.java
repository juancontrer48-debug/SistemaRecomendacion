/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemarecomendacionygrafos;
  import java.util.Stack;
/**
 *
 * @author EQUIPO
 */
public class SistemarecomendacionYgrafos {

    public static void main(String[] args) {

        // 1. DATOS: notas de 1 a 5 (0 = no la ha visto)
        String[] usuarios = {"Ana", "Luis", "Marta", "Pedro"};
        String[] pelis = {"Matrix", "Titanic", "Avatar", "Coco", "Up"};
        int[][] notas = {
            {5, 1, 5, 4, 0},   // Ana
            {4, 2, 5, 0, 4},   // Luis
            {4, 5, 0, 5, 4},   // Marta
            {0, 5, 4, 4, 5}    // Pedro
        };

        int nU = usuarios.length;   // cantidad de usuarios
        int nP = pelis.length;      // cantidad de peliculas
        int total = nU + nP;        // total de nodos del grafo

        // 2. CREAR EL GRAFO (matriz de adyacencia)
        // Nodos 0..3 = usuarios, nodos 4..8 = peliculas
        // Hay una arista entre un usuario y una pelicula si le gusto (nota >= 4)
        boolean[][] grafo = new boolean[total][total];
        for (int u = 0; u < nU; u++) {
            for (int p = 0; p < nP; p++) {
                if (notas[u][p] >= 4) {
                    grafo[u][nU + p] = true;
                    grafo[nU + p][u] = true;
                }
            }
        }

        // Mostrar las conexiones del grafo
        System.out.println("CONEXIONES DEL GRAFO (le gusto):");
        for (int u = 0; u < nU; u++) {
            for (int p = 0; p < nP; p++) {
                if (grafo[u][nU + p]) {
                    System.out.println("  " + usuarios[u] + " --- " + pelis[p]);
                }
            }
        }
        System.out.println();

        // 3. RECOMENDAR A CADA USUARIO USANDO UNA PILA
        for (int yo = 0; yo < nU; yo++) {

            boolean[] visitado = new boolean[total];
            int[] votos = new int[nP];

            Stack<Integer> pila = new Stack<>();
            pila.push(yo);          // empezamos en el usuario
            visitado[yo] = true;

            while (!pila.isEmpty()) {
                int nodo = pila.pop();   // sacamos el ultimo nodo de la pila

                if (nodo < nU && nodo != yo) {
                    // Es otra persona con gustos parecidos: vota por las
                    // peliculas que le gustaron y que yo no he visto
                    for (int p = 0; p < nP; p++) {
                        if (grafo[nodo][nU + p] && notas[yo][p] == 0) {
                            votos[p]++;
                        }
                    }
                } else {
                    // Soy yo o una pelicula: meto a la pila sus vecinos
                    for (int vecino = 0; vecino < total; vecino++) {
                        if (grafo[nodo][vecino] && !visitado[vecino]) {
                            visitado[vecino] = true;
                            pila.push(vecino);
                        }
                    }
                }
            }

            // 4. MOSTRAR EL RESULTADO
            System.out.println("Usuario: " + usuarios[yo]);
            for (int p = 0; p < nP; p++) {
                if (votos[p] > 0) {
                    System.out.println("  Le recomendamos: " + pelis[p]
                        + " (" + votos[p] + " personas parecidas la recomiendan)");
                }
            }
            System.out.println();
        }
    }
}