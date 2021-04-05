#define END_m1 5        // Allumer moteur Droit
#define ENG_m2 4       // Allumer moteur gauche

#define END_m3 0        // Sens du moteur droit
#define ENG_m4 2      // Sens du moteur Gauche
#define IN5  14 // pin capteur droit
#define IN6  12 // pin capteur gauche

void setup() {  
   
 
    Serial.begin(9600);
    pinMode(IN5,INPUT);
    pinMode(IN6,INPUT);
    pinMode(END_m1, OUTPUT);
    pinMode(ENG_m2, OUTPUT);
    pinMode(END_m3, OUTPUT);
    pinMode(ENG_m4, OUTPUT);

  }

void goAhead(){

      analogWrite(END_m1, 700);
      analogWrite(ENG_m2, 700);
      digitalWrite(END_m3, LOW); 
      digitalWrite(ENG_m4, LOW);
  }

void goLeft(){

      analogWrite(END_m1, 0);
      analogWrite(ENG_m2, 700);
      digitalWrite(END_m3, HIGH);
      digitalWrite(ENG_m4, LOW);

  }

void goRight(){

     analogWrite(END_m1, 700);
     analogWrite(ENG_m2, 0);
     digitalWrite(END_m3, LOW); 
     digitalWrite(ENG_m4, HIGH); 
  }



void stopRobot(){  

      digitalWrite(END_m1, LOW);
      digitalWrite(END_m3, LOW);    
      digitalWrite(ENG_m2, LOW);  
      digitalWrite(ENG_m4, LOW);
  }
  
void suivre () {
      int captD = digitalRead(IN5);
      int captG = digitalRead(IN6);
      if ((captG == 0) && (  captD == 1)) //S'il y a du noir à gauche et du blanc à droite, tourner à gauche
      {
         Serial.println("Tourner à gauche");
         goLeft();
        }
        else if ((  captG == 1) && (captD == 0)) //S'il y a du blanc à gauche et du noir à droite, tourner à droite
        {
          Serial.println("Tourner à droite");
          goRight();
        }
        
}
void loop(){
   
  
  int captD = digitalRead(IN5);
  int captG = digitalRead(IN6);
  suivre();
}
           
         
 
