package Juego;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp {
    public static void main(String[] args) {
        JoystickController joystickController = new JoystickController();

        Thread joystickThread = new Thread(new Runnable() {
            @Override
            public void run() {
                joystickController.start();
            }
        });

        joystickThread.start();

        // Inicia la aplicación JavaFX Buscaminas
        Application.launch(Buscaminas.class, args);
    }
}