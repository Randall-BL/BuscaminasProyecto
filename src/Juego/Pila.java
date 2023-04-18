package Juego;

import Juego.ListaEnlazada;


class Pila {
    private ListaEnlazada listaEnlazada;

    public Pila() {
        listaEnlazada = new ListaEnlazada();
    }

    public void push(int[] dato) {
        listaEnlazada.agregarAlInicio(dato);
    }

    public int[] pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía.");
        }
        int[] dato = listaEnlazada.getInicio().dato;
        listaEnlazada.eliminarInicio();
        return dato;
    }

    public int[] peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía.");
        }
        return listaEnlazada.getInicio().dato;
    }

    public boolean isEmpty() {
        return listaEnlazada.getInicio() == null;
    }
}

