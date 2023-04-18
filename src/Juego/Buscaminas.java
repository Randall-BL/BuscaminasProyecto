package Juego;

import Juego.JoystickController;
import Juego.ListaEnlazada;
import java.util.Random;
import Juego.Pila;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Buscaminas extends Application {
    private int[][] tablero;
    private Button[][] botones;
    private int minasRestantes;
    private Pila pilaSugerencias;
    private int contadorJugadas;
    private boolean turnoJugador = true;
    private boolean juegoEnCurso = true;
    private boolean dificultadAvanzada = false;
    private Pila listaSegura = new Pila();
    private JoystickController joystickcontroller;
    private int contadorMinas;
    private Label contadorMinasLabel;
    private int tiempoTranscurrido;
    private Label tiempoTranscurridoLabel;
    private Timeline timeline;



    @Override
    public void start(Stage primaryStage) {
    generarTablero();
    GridPane root = new GridPane();
    botones = new Button[8][8];
    minasRestantes = 10;
    pilaSugerencias = new Pila();
    contadorJugadas = 0;
    joystickcontroller = new JoystickController();
    contadorMinas = 0;
    contadorMinasLabel = new Label("Minas encontradas: " + contadorMinas);
    // Inicializa el tiempo transcurrido y el label
    tiempoTranscurrido = 0;
    tiempoTranscurridoLabel = new Label("Tiempo transcurrido: " + tiempoTranscurrido + " segundos");

    // Crea un timeline que se actualiza cada segundo
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        tiempoTranscurrido++;
        tiempoTranscurridoLabel.setText("Tiempo transcurrido: " + tiempoTranscurrido + " segundos");
    }));
    timeline.setCycleCount(Animation.INDEFINITE);

    

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            Button boton = new Button("");
            boton.setMinSize(50, 50);
            boton.setMaxSize(50, 50);
            int fila = i;
            int columna = j;
            boton.setOnMouseClicked(event -> {
                if (juegoEnCurso) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (tablero[fila][columna] == -1) {
                            boton.setText("X");
                            
                            Platform.runLater(() -> perder("El jugador perdió"));
                            joystickcontroller.SonidoBomba();
                        } else {
                            int minasCercanas = contarMinasCercanas(fila, columna);
                            boton.setText(String.valueOf(minasCercanas));
                            
                            boton.setDisable(true);
                            joystickcontroller.SonidoMina();
                            if (minasCercanas == 0) {
                                descubrirCasillasCercanas(fila, columna);
                            }
                            if (minasRestantes == 0) {
                                ganar();
                            }
                        }
                        if (turnoJugador) {
                            if (dificultadAvanzada) {
                                hacerJugadaMaquinaAvanzado();
                            } else {
                                hacerJugadaMaquina();
                            }
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        if (boton.getText().equals("")) {
                            boton.setText("F");
                            minasRestantes--;
                            joystickcontroller.toggleLED();
                            if (tablero[fila][columna] == -1) {
                            contadorMinas++;
                            }
                            actualizarContadorMinas();
                        } else if (boton.getText().equals("F")) {
                            boton.setText("");
                            minasRestantes++;
                            if (tablero[fila][columna] == -1) {
                            contadorMinas--;
                            }
                            actualizarContadorMinas();
                        }
                    }
                    contadorJugadas++;
                    if (contadorJugadas % 5 == 0) {
                        agregarSugerencia();
                    }
                }
            });
            botones[i][j] = boton;
            root.add(boton, j, i);
        }
    }

    Button botonDificultad = new Button("Escoger dificultad");
    Button botonSugerencia = new Button("Usar sugerencia");
    botonDificultad.setOnAction(event -> {
        if (dificultadAvanzada) {
            dificultadAvanzada = false;
            botonDificultad.setText("Dificultad normal");
            botonSugerencia.setDisable(false);
        } else {
            dificultadAvanzada = true;
            botonDificultad.setText("Dificultad avanzada");
            botonSugerencia.setDisable(true);
        }
    });
    
    botonSugerencia.setOnAction(event -> usarSugerencia());
    if (!dificultadAvanzada) {
        agregarSugerencia();
    } else {
        hacerJugadaMaquinaAvanzado();
        
    }
    VBox vbox = new VBox(10, contadorMinasLabel, tiempoTranscurridoLabel, root, botonDificultad, botonSugerencia);
    Scene scene = new Scene(vbox, 400, 550);
    primaryStage.setScene(scene);
    primaryStage.show();
    // Inicia el timeline
    timeline.play();
}
    private void actualizarContadorMinas() {
    contadorMinasLabel.setText("Minas encontradas: " + contadorMinas);
}


    private void generarTablero() {
        tablero = new int[8][8];
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int fila = random.nextInt(8);
            int columna = random.nextInt(8);
            while (tablero[fila][columna] == -1) {
                fila = random.nextInt(8);
                columna = random.nextInt(8);
            }
            tablero[fila][columna] = -1;
        }
    }

    private int contarMinasCercanas(int fila, int columna) {
        int minasCercanas = 0;
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && tablero[i][j] == -1) {
                    minasCercanas++;
                }
            }
        }
        return minasCercanas;
    }
    private void hacerJugadaMaquinaAvanzado() {
    ListaEnlazada listaGeneral = new ListaEnlazada();
    ListaEnlazada listaSegura = new ListaEnlazada();
    ListaEnlazada listaIncertidumbre = new ListaEnlazada();

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (!botones[i][j].isDisabled()) {
                listaGeneral.agregarAlFinal(new int[]{i, j});
            }
        }
    }

    Random random = new Random();

    while (!listaGeneral.estaVacia()) {
        int index = random.nextInt(listaGeneral.size());
        int[] celda = listaGeneral.get(index);
        int fila = celda[0];
        int columna = celda[1];
        int minasCercanas = contarMinasCercanas(fila, columna);

        if (minasCercanas > 0) {
            listaIncertidumbre.agregarAlFinal(celda);
        } else {
            listaSegura.agregarAlFinal(celda);
        }

        listaGeneral.eliminar(index);
        imprimirListas(listaGeneral, listaSegura, listaIncertidumbre);
    }

    if (!listaSegura.estaVacia()) {
        int index = random.nextInt(listaSegura.size());
        int[] celdaSegura = listaSegura.get(index);
        int fila = celdaSegura[0];
        int columna = celdaSegura[1];
        int minasCercanas = contarMinasCercanas(fila, columna);
        botones[fila][columna].setText(String.valueOf(minasCercanas));
        botones[fila][columna].setDisable(true);
        if (minasCercanas == 0) {
            descubrirCasillasCercanas(fila, columna);
        }
    } else {
        int index = random.nextInt(listaIncertidumbre.size());
        int[] celdaIncertidumbre = listaIncertidumbre.get(index);
        int fila = celdaIncertidumbre[0];
        int columna = celdaIncertidumbre[1];
        if (tablero[fila][columna] == -1) {
            botones[fila][columna].setText("X");
            botones[fila][columna].setDisable(true);
            maquinaPerdio();
        } else {
            int minasCercanas = contarMinasCercanas(fila, columna);
            botones[fila][columna].setText(String.valueOf(minasCercanas));
            botones[fila][columna].setDisable(true);
            if (minasCercanas == 0) {
                descubrirCasillasCercanas(fila, columna);
            }
        }
    }
}

    private void imprimirListas(ListaEnlazada listaGeneral, ListaEnlazada listaSegura, ListaEnlazada listaIncertidumbre) {
        System.out.println("Lista General: " + listaToString(listaGeneral));
        System.out.println("Lista Segura: " + listaToString(listaSegura));
        System.out.println("Lista Incertidumbre: " + listaToString(listaIncertidumbre));
        System.out.println();
    }

    private String listaToString(ListaEnlazada lista) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Nodo actual = lista.getInicio();
        while (actual != null){
            sb.append("(").append(actual.getDato()[0]).append(", ").append(actual.getDato()[1]).append("), ");
            actual = actual.siguiente;
        }
        
        if (lista.getInicio() != null) {
                sb.setLength(sb.length() - 2);
        }
        sb.append("]");
        return sb.toString();
    }


    private void descubrirCasillasCercanas(int fila, int columna) {
        for (int i = fila - 1; i <= fila + 1; i++) {
            for (int j = columna - 1; j <= columna + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && !botones[i][j].isDisabled()) {
                    int minasCercanas = contarMinasCercanas(i, j);
                    botones[i][j].setText(String.valueOf(minasCercanas));
                    botones[i][j].setDisable(true);
                    if (minasCercanas == 0) {
                        descubrirCasillasCercanas(i, j);
                    }
                }
            }
        }
    }

    private void hacerJugadaMaquina() {
    Random random = new Random();
    int fila = random.nextInt(8);
    int columna = random.nextInt(8);
    while (botones[fila][columna].isDisabled()) {
        fila = random.nextInt(8);
        columna = random.nextInt(8);
    }
    if (tablero[fila][columna] == -1) {
        botones[fila][columna].setText("X");
        botones[fila][columna].setDisable(true);
        maquinaPerdio();
    } else {
        int minasCercanas = contarMinasCercanas(fila, columna);
        botones[fila][columna].setText(String.valueOf(minasCercanas));
        botones[fila][columna].setDisable(true);
        if (minasCercanas == 0) {
            descubrirCasillasCercanas(fila, columna);
        }
    }
}

    private void agregarSugerencia() {
        ListaEnlazada celdasDisponibles = new ListaEnlazada();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!botones[i][j].isDisabled()) {
                    celdasDisponibles.agregarAlFinal(new int[]{i, j});
                }
            }
        }
        Random random = new Random();
        while (!celdasDisponibles.estaVacia()) {
            int index = random.nextInt(celdasDisponibles.size());
            int[] celda = celdasDisponibles.get(index);
            if (tablero[celda[0]][celda[1]] != -1) {
                pilaSugerencias.push(celda);
                break;
            } else {
                celdasDisponibles.eliminar(index);
            }
        }
    }
    
    private void maquinaPerdio() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setDisable(true);
                if (tablero[i][j] == -1) {
                    botones[i][j].setText("X");
                }
            }
        }
        Label mensaje = new Label("La máquina ha perdido, ganó el jugador");
        Button botonNuevaPartida = new Button("Nueva partida");

        VBox vbox = new VBox(10, mensaje, botonNuevaPartida);
        StackPane pane = new StackPane(vbox);
        pane.setPadding(new Insets(10));
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        botonNuevaPartida.setOnAction(event -> {
            generarTablero();
            reiniciarJuego();
            stage.close();
        });
    }


    private void usarSugerencia() {
    if (dificultadAvanzada) {
        while (!listaSegura.isEmpty()) {
            int[] celdaSugerida = listaSegura.pop();
            int fila = celdaSugerida[0];
            int columna = celdaSugerida[1];
            if (!botones[fila][columna].isDisabled()) {
                System.out.println("Sugerencia (fila, columna): (" + fila + ", " + columna + ")");
                int minasCercanas = contarMinasCercanas(fila, columna);
                botones[fila][columna].setText(String.valueOf(minasCercanas));
                botones[fila][columna].setDisable(true);
                if (minasCercanas == 0) {
                    descubrirCasillasCercanas(fila, columna);
                }
                break;
            }
        }
    } else {
        while (!pilaSugerencias.isEmpty()) {
            int[] celdaSugerida = pilaSugerencias.pop();
            int fila = celdaSugerida[0];
            int columna = celdaSugerida[1];
            if (!botones[fila][columna].isDisabled()) {
                System.out.println("Sugerencia (fila, columna): (" + fila + ", " + columna + ")");
                int minasCercanas = contarMinasCercanas(fila, columna);
                botones[fila][columna].setText(String.valueOf(minasCercanas));
                botones[fila][columna].setDisable(true);
                if (minasCercanas == 0) {
                    descubrirCasillasCercanas(fila, columna);
                }
                break;
            }
        }
    }
}


    private void perder(String mensajePerdida) {
    juegoEnCurso =false;
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            botones[i][j].setDisable(true);
            if (tablero[i][j] == -1) {
                botones[i][j].setText("X");
            }
        }
    }
    Label mensaje = new Label(mensajePerdida);
    Button botonNuevaPartida = new Button("Nueva partida");

    VBox vbox = new VBox(10, mensaje, botonNuevaPartida);
    StackPane pane = new StackPane(vbox);
    pane.setPadding(new Insets(10));
    Scene scene = new Scene(pane);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
    botonNuevaPartida.setOnAction(event -> {
        generarTablero();
        reiniciarJuego();
        stage.close();
        agregarSugerencia();
    });
}


    private void ganar() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                botones[i][j].setDisable(true);
            }
        }
        Label mensaje = new Label("¡Ganaste!");
        Button botonNuevaPartida = new Button("Nueva partida");
        
        VBox vbox = new VBox(10, mensaje, botonNuevaPartida);
        StackPane pane = new StackPane(vbox);
        pane.setPadding(new Insets(10));
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        botonNuevaPartida.setOnAction(event -> {
            generarTablero();
            reiniciarJuego();
            stage.close();
        });
    }

private void reiniciarJuego() {
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            botones[i][j].setText("");
            botones[i][j].setDisable(false);
        }
    }
    minasRestantes = 10;
    juegoEnCurso = true;
}

    public static void main(String[] args) {
        launch(args);
    }
}

