/*
  Some code snippets used from Serial Event example by Tom Igoe
 http://www.arduino.cc/en/Tutorial/SerialEvent
 */

String inString = "";        
boolean stringComplete = false;
const int LIGHTPIN0 = 2;
const int LIGHTPIN1 = 3;

void setup() {
  Serial.begin(9600);
  inString.reserve(200);
  pinMode(LIGHTPIN0, OUTPUT);
  pinMode(LIGHTPIN1, OUTPUT);
  digitalWrite(LIGHTPIN0, HIGH);  
  delay(1000);             
  digitalWrite(LIGHTPIN0, LOW);   
  delay(1000);
}
void loop() {
  while (Serial.available() > 0) {
    int inChar = Serial.read();

    if (inChar != '\n') {
      inString += (char)inChar;
    }

    else {
      int OUTPIN = (inString.charAt(0) + 2);
      if (inString.charAt(1) == '1') {
        digitalWrite(OUTPIN, HIGH);  
        delay(1000);            
      }
      else if (inString.charAt(1) == '0'){
        digitalWrite(OUTPIN, LOW);  
        delay(1000); 
      }
      inString = "";
    }
  }
}
