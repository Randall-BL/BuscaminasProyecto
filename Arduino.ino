int xPin = A1;
int yPin = A0;
int buttonPin = 5;
int buzzerPin = 8;
int ledPin = 7; // Nuevo pin para el LED
int buttonVal;
double xVal;
double yVal;
int dt = 500;
int valorMapeadoX, valorMapeadoY;
String incomingCommand;

void setup() {
  pinMode(xPin, INPUT);
  pinMode(yPin, INPUT);
  pinMode(buttonPin, INPUT);
  digitalWrite(buttonPin, HIGH);
  pinMode(buzzerPin, OUTPUT);
  pinMode(ledPin, OUTPUT); // Configura el pin del LED como salida

  Serial.begin(9600);
}

void loop() {
  xVal = analogRead(xPin);
  yVal = analogRead(yPin);
  buttonVal = digitalRead(buttonPin);
  valorMapeadoX = map(xVal, 0, 1023, -90, 90);
  valorMapeadoY = map(yVal, 0, 1023, -90, 90);
  Serial.print("Valor X = ");
  Serial.print(valorMapeadoX);
  Serial.print("\n");
  Serial.print("Valor Y = ");
  Serial.print(valorMapeadoY);
  Serial.print("\n");
  Serial.print("Valor del boton =");
  Serial.println(buttonVal);
  Serial.print("\n");
  delay(dt);

  if (Serial.available() > 0) {
    incomingCommand = Serial.readStringUntil('\n');
    incomingCommand.trim();

    if (incomingCommand == "MINE_SOUND") {
      playMineSound();
    } else if (incomingCommand == "BOMB_SOUND") {
      playBombSound();
    } else if (incomingCommand == "TOGGLE_LED") { // Nueva condición para el comando TOGGLE_LED
      digitalWrite(ledPin, HIGH);
      delay(1000);
      digitalWrite(ledPin, LOW);
    }
  }

  if (buttonVal == 0) {
    digitalWrite(buzzerPin, 1);
  } else {
    digitalWrite(buzzerPin, 0);
  }
}

void playMineSound() {
  int frequency = 1000;
  int duration = 1000;

  tone(buzzerPin, frequency, duration);
}

void playBombSound() {
  int frequency = 2000;
  int duration = 250;
  int repetitions = 4;

  for (int i = 0; i < repetitions; i++) {
    tone(buzzerPin, frequency, duration);
    delay(duration);
  }
}
