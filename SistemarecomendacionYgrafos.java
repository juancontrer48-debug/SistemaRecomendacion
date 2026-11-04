/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemarecomendacionygrafos;

/**
 *
 * @author EQUIPO
 */
public class SistemarecomendacionYgrafos {

    // NODO: cada "eslabon" de la lista enlazada (tiene constructor)
    static class Nodo {
        int dato;          // numero del nodo del grafo que guardamos
        Nodo siguiente;    // apunta al siguiente eslabon de la lista

        Nodo(int dato) {
            this.dato = dato;
        }
    }

    // LISTA ENLAZADA: cadena de nodos conectados uno tras otro
    static class ListaEnlazada {
        private Nodo primero;   // primer eslabon de la lista
        private Nodo ultimo;    // ultimo eslabon de la lista

        // Agregar un dato al final de la lista
        void agregar(int dato) {
            Nodo nuevo = new Nodo(dato);
            if (primero == null) {          // la lista estaba vacia
                primero = nuevo;
                ultimo = nuevo;
            } else {
                ultimo.siguiente = nuevo;   // el ultimo ahora apunta al nuevo
                ultimo = nuevo;
            }
        }

        // Quitar y devolver el primer dato de la lista
        int quitarPrimero() {
            int dato = primero.dato;
            primero = primero.siguiente;
            if (primero == null) {          // la lista quedo vacia
                ultimo = null;
            }
            return dato;
        }

        // Preguntar si la lista esta vacia
        boolean estaVacia() {
            return primero == null;
        }
    }

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

        // 3. RECOMENDAR A CADA USUARIO USANDO LA LISTA ENLAZADA
        for (int yo = 0; yo < nU; yo++) {

            boolean[] visitado = new boolean[total];
            int[] votos = new int[nP];

            // Lista enlazada con los nodos pendientes por revisar
            ListaEnlazada pendientes = new ListaEnlazada();
            pendientes.agregar(yo);     // empezamos en el usuario
            visitado[yo] = true;

            while (!pendientes.estaVacia()) {
                int nodo = pendientes.quitarPrimero();   // tomamos el primero de la lista

                if (nodo < nU && nodo != yo) {
                    // Es otra persona con gustos parecidos: vota por las
                    // peliculas que le gustaron y que yo no he visto
                    for (int p = 0; p < nP; p++) {
                        if (grafo[nodo][nU + p] && notas[yo][p] == 0) {
                            votos[p]++;
                        }
                    }
                } else {
                    // Soy yo o una pelicula: agrego a la lista sus vecinos
                    for (int vecino = 0; vecino < total; vecino++) {
                        if (grafo[nodo][vecino] && !visitado[vecino]) {
                            visitado[vecino] = true;
                            pendientes.agregar(vecino);
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