//######################################
//##                                  ##
//##           INCLUDE                ##
//##                                  ##
//######################################

#include <Ultrasonic.h>
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

#define END_m1 5      // Allumer moteur Droit
#define ENG_m2 4     // Allumer moteur gauche
#define END_m3 0    // Sens du moteur droit
#define ENG_m4 2   // Sens du moteur Gauche
#define D5  14    // capteur gauche
#define D6  12   // echo
#define D7 13   // capteur droit
#define D8 15  // trigger

Ultrasonic ultrasonic (15, 12); // trigger pin + ECHO PIN

//######################################
//##                                  ##
//##           VAR                    ##
//##                                  ##
//######################################

//char web[30];
int numChambre;
const char* cheminChambre;
int etat=0;
int pasloop = 0;

// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char noeud;
char dir;
String direct;
int reading; //DECLARATION DES VARIABLES POUR QUE L'ULTRASONIC SENSOR POUR LIRE LA VALEUR

//capteur de distance 
long temps_debut;
long temps_fin;
long ecart_temps;
float distance;

//######################################
//##                                  ##
//##           protocole              ##
//##                                  ##
//######################################
const char *ssid = "IMERIR_IoT";  //ENTER YOUR WIFI SETTINGS
const char *password = "kohWoong5oox";
//const char *ssid = "iPhone";  //ENTER YOUR WIFI SETTINGS
//const char *password = "Charice712";

const char *host = "10.3.6.197";//adresse ip de l'hote
//const char *host = "172.20.10.3";//adresse ip de l'hote

const int httpsPort = 8000;  //HTTPS= 443 and HTTP = 80
const int Id_Robot=01;

//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################
 /*Fonction Avancer tout droit*/
void goAhead(){
      analogWrite(END_m1, 750);  // Mettre la vitesse du Moteur Droit à 700
      analogWrite(ENG_m2, 700);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }

 /*Fonction tourner à Gauche  */
void goRight(){
      analogWrite(END_m1, 0);    // Eteindre Moteur Droit
      analogWrite(ENG_m2, 700);  //Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, HIGH);// Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }
  
 /*Fonction tourner à Droite  */
void goLeft(){
     analogWrite(END_m1, 750);  //Mettre la vitesse du Moteur Droit à 700
     analogWrite(ENG_m2, 0);    // Eteindre Moteur Gauche
     digitalWrite(END_m3, LOW); // Sens du Moteur Droit
     digitalWrite(ENG_m4, HIGH);//Sens du Moteur Gauche
  }

 /*Fonction arreter le Robot */
void stopRobot(){  
      digitalWrite(END_m1, LOW); // Eteindre Moteur Droit
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m2, LOW); // Eteindre Moteur Gauche
      digitalWrite(ENG_m4, LOW); // Sens du Moteur Gauche
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
  httpsClient.print(String("GET ")+ "/getOrder" + " HTTP/1.1\r\n" + "Host: 10.3.6.197" +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
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
  httpsClient.print(String("GET ")+ "/getPath/" + numChambre + " HTTP/1.1\r\n" + "Host: 10.3.6.197" +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
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
  const char* cheminChambre = chemin["path"];
  direct = String(cheminChambre);
  
  //verif 
  Serial.println(direct);
  
  // Attente :
  delay(2000);  //GET Data at every 2 seconds
}

/*******************************************************************
 * 
 *reseau set 
 *"/Robots/setPosition/"
 *
 *******************************************************************/
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

/*****************************************************
 * 
 *Setup
 *
 ******************************************************/
void setup()
{
  //delay(1000);(enlever a voir pour test)
  Serial.begin(115200);
  //WiFi.mode(WIFI_OFF);        //Prevents reconnection issue (taking too long to connect)
  //delay(1000);( pas forcé a enelver)
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
  
/********************************************************************/
  // REGLAGE DE LA PIN TRIGGER :
  pinMode(D8, OUTPUT);
  
  // REGLAGE DE LA PIN ECHO :
  pinMode(D7, INPUT);
  
  // INITIALISATION DE LA VARIABLE :
  distance = 0;
/*
  // ACTIVATION AUTOMATIQUE DU CAPTEUR ULTRASON :
  timer1_attachInterrupt( ActiverTrigger );
  timer1_enable( TIM_DIV16, TIM_EDGE, TIM_SINGLE );
  timer1_write(600000);
  */
}


void loop()
{
  reading = ultrasonic.read(); 
  if(pasloop == 0)
  {
    getOrder();
    getPath();
    pasloop++;
  }
  reading = ultrasonic.read();
  Serial.println("reading");
  Serial.println(reading);
  if (reading < 13){
    stopRobot();
    delay(1000);
  }
  delay(50);
  //######################################
  //##                                  ##
  //##           robot                  ##
  //##                                  ##
  //######################################
  int captD = digitalRead(D7); // Lire la valeur du capteur droit
  int captG = digitalRead(D5    ); // Lire la valeur du capteur gauche
  //dir = cheminChambre[iteration];  
  //noeud = cheminChambre[pos]-'0';


  
  switch (etat) {
    Serial.println("switch");
    //##################################### ETAT INITIAL : AVANCE #####################################
    case 0:
    Serial.println("case 0");
    goAhead();
    if (!captD && !captG)
    {
      etat=1;
    }
    break;
    //###################################################################################################

    //######################################### ETAT 1: SUIVI DE LIGNE #########################################
    
    case 1:
    Serial.println("case 1");
    goAhead(); // avance tant qu'un des 2 capteurs ne detecte rien
    if (captD==1 && captG==0) // Si capteur gauche = noir --> etat 3
    {
      goRight();
    }  
    else if (captD==0 && captG==1) // // Si capteur droit = noir --> etat 2
    {
      goLeft();
    }  
    else if (captD == 1 && captG == 1)
    {
      etat =2;
    }
    break;
    
    //###################################################################################################


    //####################### ETAT INTERSECTION (2): LIT LA CHAINE DE CARACTERE ##########################
    
    // EN FONCTION DE LA CHAINE, AVANCE OU TOURNE A GAUCHE OU A DROITE
    case 2:
    Serial.println("case 2");
    stopRobot();
    //setNode();
    delay(2000);
    dir = direct[iteration];  
    noeud = direct[pos]-'0';
    
    if ( dir== 'F')
    {
      Serial.println("F");
      iteration = iteration + 2;
      pos = pos +2;
      etat = 0;
      
    }
 
    else if( dir == 'L')
    {
      Serial.println("L");
      goLeft();
      delay(500);
      iteration = iteration + 2;
      pos = pos +2;
      etat = 1;
      
    }
   
    else if( dir=='R')
    {
      Serial.println("R");
      goRight();
      delay(500);
      iteration = iteration + 2;
      pos = pos +2;
      etat = 1;
    }
    
   
    break;
    
    //#################################################################################################### 
  }

}
