//######################################
//##                                  ##
//##           INCLUDE                ##
//##                                  ##
//######################################

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h> 
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

//######################################
//##                                  ##
//##           DEFINITION             ##
//##                                  ##
//######################################

#define END_m1 5        // Allumer moteur Droit
#define ENG_m2 4       // Allumer moteur gauche

#define END_m3 0        // Sens du moteur droit
#define ENG_m4 2      // Sens du moteur Gauche
#define D5  14 // pin capteur droit
#define D6  12 // pin capteur gauche
#define D7 13  // trigger
#define D8 15  // echoe

//######################################
//##                                  ##
//##           VAR                    ##
//##                                  ##
//######################################

int numChambre;
const char* cheminChambre;
int etat=0;
int pasloop = 0;
char web[]="5f6r4r3r5l";



// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char noeud;
char dir;

//capteur de distance 
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
  digitalWrite(D7, HIGH);
  delayMicroseconds(10);
  digitalWrite(D7, LOW);
  timer1_write(600000);
}



//######################################
//##                                  ##
//##           EcouterEcho            ##
//##                                  ##
//######################################

ICACHE_RAM_ATTR void EcouterEcho()
{
  if (digitalRead(D8) == 1)
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
//##           protocole              ##
//##                                  ##
//######################################

const char *ssid = "IMERIR_IoT";  //ENTER YOUR WIFI SETTINGS
const char *password = "kohWoong5oox";


const char *host = "10.3.6.197";//adresse ip de l'hote
//byte host[4] = {10, 3, 6, 197};
const int httpsPort = 8000;  //HTTPS= 443 and HTTP = 80
const int Id_Robot=01;

//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################
 /*Fonction Avancer tout droit*/
void goAhead(){
      analogWrite(END_m1, 800);  // Mettre la vitesse du Moteur Droit à 700
      analogWrite(ENG_m2, 800);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }

 /*Fonction tourner à Gauche  */
void goRight(){
      analogWrite(END_m1, 0);    // Eteindre Moteur Droit
      analogWrite(ENG_m2, 800);  //Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, HIGH);// Sens du Moteur Droit
      //digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }
  
 /*Fonction tourner à Droite  */
void goLeft(){
     analogWrite(END_m1, 800);  //Mettre la vitesse du Moteur Droit à 700
     analogWrite(ENG_m2, 0);    // Eteindre Moteur Gauche
     //digitalWrite(END_m3, LOW); // Sens du Moteur Droit
     digitalWrite(ENG_m4, HIGH);//Sens du Moteur Gauche
  }

 /*Fonction arreter le Robot */
void stopRobot(){  
      digitalWrite(END_m1, LOW); // Eteindre Moteur Droit
      //digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m2, LOW); // Eteindre Moteur Gauche
      //digitalWrite(ENG_m4, LOW); // Sens du Moteur Gauche
  }

//######################################
//##                                  ##
//##           réseaux demande ordre  ##
//##                                  ##
//######################################
void getOrder()
{
  // Creation du client sécurisé :
  WiFiClient httpsClient;    //Declare object of class WiFiClient
  //Serial.println(host);


 
  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);


  // Tentative de connexion :
  Serial.print("HTTPS Connecting");
  int r=0; //retry counter
  while((!httpsClient.connect(host, httpsPort)) && (r < 30))
  {
      delay(100);
      Serial.print(".");
      r++;
  }
  
  if(r==30) {Serial.println("Connection failed");}
  else {Serial.println("Connected to web");}


  // Envoie d'une intersection :
  int intersection = 5;
  String STR_intersection;
  STR_intersection = String(intersection);   //String to interger conversion


  // Construction de la requete :
  String Link;
  Link = "/";

  Serial.print("requesting URL: ");
  //Serial.println(host+Link);


  // Envoie requete chemin
  httpsClient.print(String("GET ")+ "/Robots/getOrder" + " HTTP/1.1\r\n" + "Host: 10.3.6.197" +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
  Serial.println("demande ordre");
 

  // Reception du message :
  while (httpsClient.connected())
  {
    delay(10);
    
  }


  // Impression du message :  
  Serial.println("reply was:");
  Serial.println("==========");
  String line;
  while(httpsClient.available())
  {        
    line = httpsClient.readStringUntil('\n');  //Read Line by Line
    Serial.println(line); //Print response
  }
  Serial.println("==========");
  Serial.println("closing connection");

      // déserialisation
      
  StaticJsonDocument<200> order;
  DeserializationError error = deserializeJson(order, line); //(document, input)

  // Test
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }
  int ordreChambre = order["room"];

//verif 
  
  Serial.println(ordreChambre);
  
  Serial.println(line);
  numChambre= ordreChambre;
  // Attente :
  delay(2000);  //GET Data at every 2 seconds
}
//######################################
//##                                  ##
//##         réseaux demande chemin   ##
//##                                  ##
//######################################
void getPath()
{ 
  // Creation du client sécurisé :
  WiFiClient httpsClient;    //Declare object of class WiFiClient
  //Serial.println(host);


 
  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);


  // Tentative de connexion :
  Serial.print("HTTPS Connecting");
  int r=0; //retry counter
  while((!httpsClient.connect(host, httpsPort)) && (r < 30))
  {
      delay(100);
      Serial.print(".");
      r++;
  }
  
  if(r==30) {Serial.println("Connection failed");}
  else {Serial.println("Connected to web");}


  // Envoie d'une intersection :
  int intersection = 5;
  String STR_intersection;
  STR_intersection = String(intersection);   //String to interger conversion


  // Construction de la requete :
  String Link;
  Link = "/";

  Serial.print("requesting URL: ");
  //Serial.println(host+Link);


  // Envoie requete chemin
  httpsClient.print(String("GET ")+ "/Robots/getPath/" + numChambre + " HTTP/1.1\r\n" + "Host: 10.3.6.197" +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
  //Serial.println("demande chemin");
 

  // Reception du message :
  while (httpsClient.connected())
  {
    delay(10);
    
  }


  // Impression du message :  
  Serial.println("reply was:");
  Serial.println("==========");
  String line;
  while(httpsClient.available())
  {        
    line = httpsClient.readStringUntil('\n');  //Read Line by Line
    Serial.println(line); //Print response
  }
  Serial.println("==========");
  Serial.println("closing connection");

      // déserialisation
      
StaticJsonDocument<200> chemin;
DeserializationError error = deserializeJson(chemin, line); //(document, input)

  // Test
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }
  cheminChambre = chemin["path"];
  

//verif 
Serial.println(cheminChambre);

  // Attente :
  delay(2000);  //GET Data at every 2 seconds

}
//######################################
//##                                  ##
//##         Reseau set node          ##
//##      "/Robots/setPosition/"      ##
//######################################


 void setNode()
{
 
   // Creation du client sécurisé :
  WiFiClient httpsClient;    //Declare object of class WiFiClient
  Serial.println(host);

  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);


  // Tentative de connexion :
  Serial.print("HTTP Connecting");
  int r=0; //retry counter
  while((!httpsClient.connect(host, httpsPort)) && (r < 30))
  {
      delay(100);
      Serial.print(".");
      r++;
  }
 
  if(r==30) {Serial.println("Connection failed");}
  else {Serial.println("Connected to web");}
    // Envoie d'une intersection :
  int intersection = 5;
  String STR_intersection;
  STR_intersection = String(intersection);   //String to interger conversion


  // Construction de la requete :
  String Link;
  Link = "/" ;

  Serial.print("requesting URL: ");
  Serial.println(host+Link);


  // Envoie de la requete :
  httpsClient.print(String("GET ") +"/Robots/setPosition/" + noeud + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Connection: close\r\n\r\n");
  Serial.println("Envoie position robot");
 
  // Reception du message :
  while (httpsClient.connected())
  {
    String line = httpsClient.readStringUntil('\n');
    if (line == "\r") {
      Serial.println("headers received");
      break;
    }
  }


  // Impression du message :
  Serial.println("reply was:");
  Serial.println("==========");
  String line;
  while(httpsClient.available())
  {        
    line = httpsClient.readStringUntil('\n');  //Read Line by Line
    Serial.println(line); //Print response
  }
  Serial.println("==========");
  Serial.println("closing connection");
  delay(2000);
  }

//######################################
//##                                  ##
//##         Setup                    ##
//##                                  ##
//######################################
void setup()
{
  
  Serial.begin(115200);
 
  //moteur 
  pinMode(END_m1,OUTPUT);//Allumer moteur Droit
  pinMode(ENG_m2,OUTPUT);//Allumer moteur gauche
  pinMode(END_m3,OUTPUT);//Sens du moteur droit
  pinMode(ENG_m4,OUTPUT);//Sens du moteur gauche
  
  //REGLAGE CAPTEUR LUMINOSITE
  pinMode(D5, INPUT);
  pinMode(D6, INPUT);
  
  // REGLAGE DE LA PIN TRIGGER :
  pinMode(D7, OUTPUT);
  

  // REGLAGE DE LA PIN ECHO :
  pinMode(D8, INPUT);
  attachInterrupt( digitalPinToInterrupt(D6), EcouterEcho, CHANGE);


  // INITIALISATION DE LA VARIABLE :
  distance = 0;


  // ACTIVATION AUTOMATIQUE DU CAPTEUR ULTRASON :
  timer1_attachInterrupt( ActiverTrigger );
  timer1_enable( TIM_DIV16, TIM_EDGE, TIM_SINGLE );
  timer1_write(600000);

  WiFi.mode(WIFI_STA);        //Only Station No AP, This line hides the viewing of ESP as wifi hotspot
  
  WiFi.begin(ssid, password);     //Connect to your WiFi router
  Serial.println("");

  Serial.print("Connecting");
  // Wait for connection
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }


  //If connection successful show IP address in serial monitor
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());  //IP address assigned to your ESP
}


//######################################
//##                                  ##
//##         Loop                     ##
//##                                  ##
//######################################

void loop()
{
     //######################################
    //##                                  ##
    //##           robot                  ##
    //##                                  ##
    //######################################
  int captD = digitalRead(D5); // Lire la valeur du capteur droit
  int captG = digitalRead(D6); // Lire la valeur du capteur gauche
 
  switch (etat) 
  {
    
    //##################################### ETAT INITIAL : AVANCE #####################################
    
    case 0:
    goAhead();
    Serial.println(distance);
    while(distance<0.5)
    {
      stopRobot();
    }
    if (!captD && !captG)
    {
      etat=1;
    }
    break;
    
    //###################################################################################################
    
    

    
    //######################################### ETAT 1: suivie de ligne #########################################
    
    // CHANGE D'ETAT SI UN DES CAPTEUR DETECTE LA LIGNE
    case 1:
    goAhead(); // avance tant qu'un des 2 capteurs ne detecte rien
    while(distance<0.5)
    {
      stopRobot();
    }
    if (captD==1 && !captG) // Si capteur gauche = noir --> etat 3
    {
      goRight();
    }  
    if (!captD && captG==1) // // Si capteur droit = noir --> etat 2
    {
      goLeft();
    } 
    if (captD==1 && captG==1)
    {
      etat=2;
    }
    break;
    

    //##################################### ETAT 2: intersection #####################################
    
    
    
    case 2:
    
    
    //dir = cheminChambre[iteration];  
    //noeud = cheminChambre[pos];
    dir = web[iteration];  
    noeud = web[pos];
    //ordre();
    while(distance<0.5)
    {
      stopRobot();
    }
   
    if ( dir== 'f')
    {
      stopRobot();
      delay(2000);
      etat = 0;
    }
 
    if( dir == 'l')
    {
      stopRobot();
      delay(2000);
      goLeft();
      delay(500);
      
      etat = 1;
    }
   
    if( dir=='r')
    {
      stopRobot();
      delay(2000);
      goRight();
      delay(500);
      
      etat = 1;
    }
    
    iteration = iteration + 2;// permet d'avancer sur la chaine en prenant en compte que les ordres
    pos = pos + 2;            // permet d'avancer sur la chaine en prenant en compte que les noeuds 
   
    break;
   
    }
}
