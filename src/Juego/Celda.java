package Juego;

import javafx.scene.control.Button;

public class Celda extends Button{
    private int fila;
    private int columna;
    private boolean tieneMina;

    public Celda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.tieneMina = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean tieneMina() {
        return tieneMina;
    }

    public void colocarMina() {
        this.tieneMina = true;
    }
}
