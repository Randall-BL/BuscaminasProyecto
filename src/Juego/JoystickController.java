package Juego;

import com.fazecast.jSerialComm.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class JoystickController {

    // establece el puerto serie y la velocidad de transmisión
    static SerialPort serialPort = SerialPort.getCommPort("COM3");
    static int baudRate = 9600;

    public static void start() {

        // abre el puerto serie
        if (!serialPort.openPort()) {
            System.out.println("No se puede abrir el puerto serie");
            return;
        }
        serialPort.setBaudRate(baudRate);

        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            byte[] readBuffer = new byte[1024];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
            String serialData = new String(readBuffer, 0, numRead);
            System.out.print(serialData);

            String[] values = serialData.split("\\r?\\n");
            for (String value : values) {
                if (value.contains("Valor X")) {
                    String xValue = value.replace("Valor X = ", "").trim();
                    if (isNumeric(xValue)) {
                        int xVal = Integer.parseInt(xValue);
                        if (xVal < -50) {
                            robot.keyPress(KeyEvent.VK_LEFT);
                            robot.keyRelease(KeyEvent.VK_LEFT);
                        } else if (xVal > 50) {
                            robot.keyPress(KeyEvent.VK_RIGHT);
                            robot.keyRelease(KeyEvent.VK_RIGHT);
                        }
                    }
                } else if (value.contains("Valor Y")) {
                    String yValue = value.replace("Valor Y = ", "").trim();
                    if (isNumeric(yValue)) {
                        int yVal = Integer.parseInt(yValue);
                        if (yVal < -50) {
                            robot.keyPress(KeyEvent.VK_DOWN);
                            robot.keyRelease(KeyEvent.VK_DOWN);
                        } else if (yVal > 50) {
                            robot.keyPress(KeyEvent.VK_UP);
                            robot.keyRelease(KeyEvent.VK_UP);
                        }
                    }
                }
            }

            // hace sonar el sonido de la mina durante un segundo
            //SonidoMina();

            // hace sonar el sonido de la bomba cuatro veces cada 0.25 segundos
            //SonidoBomba();

            try {
                Thread.sleep(10); // espera 1 segundo
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void SonidoMina() {
        // envía la señal al Arduino a través del puerto serie
        String command = "MINE_SOUND\n";
        serialPort.writeBytes(command.getBytes(), command.length());
    }

    static void SonidoBomba() {
        // envía la señal al Arduino a través del puerto serie
        String command = "BOMB_SOUND\n";
        serialPort.writeBytes(command.getBytes(), command.length());
    }
    
    static void toggleLED() {
    // envía la señal al Arduino a través del puerto serie
    String command = "TOGGLE_LED\n";
    serialPort.writeBytes(command.getBytes(), command.length());
}

    // Método para verificar si una cadena contiene un número válido
    static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?\\d+");
        return pattern.matcher(str).matches();
    }
}
