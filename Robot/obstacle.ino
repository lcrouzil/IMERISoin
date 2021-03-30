/*
 * Ce programme permet de mesurer la distance entre le robot et un obstacle
 * grace au capteur ultrason.
 * 
 * Les connexions entre le NodeMCU et le capteur est le suivant :
 *   - la pin D5 est utilisée pour déclencher le TRIGGER
 *   - la pin D6 est utilisée pour lire l'état de ECHO
 * 
 * Remarque : le programme utilise les interruptions pour fonctionner.
 */


//######################################
//##                                  ##
//##           DEFINITION             ##
//##                                  ##
//######################################

#define D1 5
#define D2 4
#define D3 0
#define D4 2
#define D5 14
#define D6 12



//######################################
//##                                  ##
//##            VARIABLES             ##
//##                                  ##
//######################################

long temps_debut;
long temps_fin;
long ecart_temps;
float distance;



//######################################
//##                                  ##
//##         ActiverTrigger           ##
//##                                  ##
//######################################

ICACHE_RAM_ATTR void ActiverTrigger()
{
  // DECLENCHEMENT DU TRIGGER
  digitalWrite(D5, HIGH);
  delayMicroseconds(10);
  digitalWrite(D5, LOW);
  timer1_write(600000);
}



//######################################
//##                                  ##
//##           EcouterEcho            ##
//##                                  ##
//######################################

ICACHE_RAM_ATTR void EcouterEcho()
{
  if (digitalRead(D6) == 1)
  {
    temps_debut = micros();
  }
  else
  {
    temps_fin = micros();
    
    // CALCUL DE LA DISTANCE
    ecart_temps = temps_fin - temps_debut;
    distance = 0.000340 * (float)ecart_temps / 2.0;
  }
  
}



//######################################
//##                                  ##
//##             SETUP                ##
//##                                  ##
//######################################

void setup()
{
  // REGLAGE DU PORT UART :
  Serial.begin(9600);

  //moteur 
  pinMode(D1,OUTPUT);//puissance moteur g
  pinMode(D2,OUTPUT);//puissance moteur d
  pinMode(D3,OUTPUT);//sens moteur g
  pinMode(D4,OUTPUT);// sens moteur d

  
  // REGLAGE DE LA PIN TRIGGER :
  pinMode(D5, OUTPUT);
  

  // REGLAGE DE LA PIN ECHO :
  pinMode(D6, INPUT);
  attachInterrupt( digitalPinToInterrupt(D6), EcouterEcho, CHANGE);


  // INITIALISATION DE LA VARIABLE :
  distance = 0;


  // ACTIVATION AUTOMATIQUE DU CAPTEUR ULTRASON :
  timer1_attachInterrupt( ActiverTrigger );
  timer1_enable( TIM_DIV16, TIM_EDGE, TIM_SINGLE );
  timer1_write(600000);
}



//######################################
//##                                  ##
//##             LOOP                 ##
//##                                  ##
//######################################

void loop()
{
  // AFFICHAGE DU RESULTAT
  //Serial.println("Distance (m) :");
  //Serial.println(distance);
  //delay(1000);

  // avance 
  //digitalWrite(D1,HIGH);
  //digitalWrite(D2,HIGH);
  //digitalWrite(D3,LOW);
  //digitalWrite(D4,LOW);
  while(distance<0.5)
  {
    digitalWrite(D1,LOW);
    digitalWrite(D2,LOW);
  }
  
}
