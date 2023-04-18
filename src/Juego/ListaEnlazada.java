package Juego;

import Juego.Nodo;

class ListaEnlazada {
    private Nodo cabeza;
    private int size;

    public ListaEnlazada() {
        cabeza = null;
        size = 0;
    }
    
    public Nodo getInicio(){
        return cabeza;
    }
    
    public void agregarAlInicio(int[] dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    // Método para agregar un elemento al final de la lista
    public void agregarAlFinal(int[] dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        size++;
    }

    // Método para obtener el nodo en un índice específico
    public Nodo getNodo(int indice) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        Nodo actual = cabeza;
        int contador = 0;
        while (contador < indice) {
            actual = actual.siguiente;
            contador++;
        }
        return actual;
    }

    // Método para obtener el tamaño de la lista
    public int size() {
        return size;
    }

    // Método para eliminar un elemento en un índice específico
    public void eliminar(int indice) {
        if (indice < 0 || indice >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        if (indice == 0) {
            cabeza = cabeza.siguiente;
        } else {
            Nodo actual = cabeza;
            int contador = 0;
            while (contador < indice - 1) {
                actual = actual.siguiente;
                contador++;
            }
            actual.siguiente = actual.siguiente.siguiente;
        }
        size--;
    }
    
    public void eliminarInicio() {
        if (cabeza == null) {
            throw new IllegalStateException("La lista está vacía.");
        }
        cabeza = cabeza.siguiente;
    }

    // Método para obtener el dato en un índice específico
    public int[] get(int indice) {
        return getNodo(indice).dato;
    }

    // Método para modificar el dato en un índice específico
    public void set(int indice, int[]dato) {
        getNodo(indice).dato = dato;
    }

    // Método para insertar un elemento en un índice específico
    public void insertar(int indice, int[] dato) {
        if (indice < 0 || indice > size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        if (indice == 0) {
            Nodo nuevo = new Nodo(dato);
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            int contador = 0;
            while (contador < indice - 1) {
                actual = actual.siguiente;
                contador++;
            }
            Nodo nuevo = new Nodo(dato);
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }
        size++;
    }

    // Método para verificar si la lista está vacía
    public boolean estaVacia() {
        return size == 0;
    }

    // Método para vaciar la lista
    public void vaciar() {
        cabeza = null;
        size = 0;
    }
}
