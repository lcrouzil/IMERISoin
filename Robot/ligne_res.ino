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

//######################################
//##                                  ##
//##           VAR                    ##
//##                                  ##
//######################################

//char web[] = "5f6f7l5r";
int etat=0;
int iteration = 1;
char dir;

//######################################
//##                                  ##
//##           protocole              ##
//##                                  ##
//######################################

const char *ssid = "IMERIR_IoT";  //ENTER YOUR WIFI SETTINGS
const char *password = "kohWoong5oox";


const char *host = "10.3.6.197";//adresse ip de l'hote 
const int httpsPort = 8000;  //HTTPS= 443 and HTTP = 80
const int Id_Robot=01;

//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################
 /*Fonction Avancer tout droit*/
void goAhead(){
      analogWrite(END_m1, 700);  // Mettre la vitesse du Moteur Droit à 700
      analogWrite(ENG_m2, 700);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }

 /*Fonction tourner à Gauche  */
void goLeft(){
      analogWrite(END_m1, 0);    // Eteindre Moteur Droit
      analogWrite(ENG_m2, 700);  //Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, HIGH);// Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }
  
 /*Fonction tourner à Droite  */
void goRight(){
     analogWrite(END_m1, 700);  //Mettre la vitesse du Moteur Droit à 700
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



void setup()
{
  delay(1000);
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
}



void loop()
{
//######################################
//##                                  ##
//##           réseaux                ##
//##                                  ##
//######################################
  // Creation du client sécurisé :
  WiFiClient httpsClient;    //Declare object of class WiFiClient
  Serial.println(host);


 
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
  Serial.println(host+Link);


  // Envoie requete chemin
  httpsClient.print(String("GET ")+ "/Robots/getPath" + Link + "3" + " HTTP/1.1\r\n" + "Host: " + host +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
  Serial.println("demande chemin");
 

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

//verif 
Serial.println(cheminChambre);

  // Attente :
  delay(2000);  //GET Data at every 2 seconds

//######################################
//##                                  ##
//##           robot                  ##
//##                                  ##
//######################################
int captD = digitalRead(D5); // Lire la valeur du capteur droit
int captG = digitalRead(D6); // Lire la valeur du capteur gauche
  
  switch (etat) {
     
  case 0:
  goAhead();
  if (captD==0 && captG==0)
  {
    etat=1;
  }
  break;

   
  case 1:
  goAhead();
  if (captD==1 && captG==0)
  {
    etat=3;
  }  
  else if (captD==0 && captG==1)
  {
    etat=2;
  }  
  break;

   
  case 2:
  goRight();
  if (captD==0 && captG==0)
  {
    etat=1;
  }
  else if (captD==1 && captG==1)
  {
    etat=4;
  }
  break;

   
  case 3:
  goLeft();
  if (captD==0 && captG==0)
  {
    etat=1;
  }
  else if (captD==1 && captG==1)
  {
    etat=4;
  }
  break;
     
  case 4:
  dir = cheminChambre[iteration];  
  Serial.println(dir);
  if ( dir== 'f')
  {
    //Serial.println("salut");
    etat = 5;
  }

  if( dir == 'r')
  {
    Serial.println("r");
    etat = 7;
  }
 
  if( dir=='l')
  {
    Serial.println("l");
    etat = 6;
  }
  iteration = iteration + 2;
 
  break;
     


     
  case 5:
  goAhead();
  delay(200);
  if (captG == 0)
  {
    etat = 3;
  }
  else if (captD == 0)
  {
    etat = 2;
  }
  break;


  case 6:
  goRight();
  delay(200);
  if (captD == 0)
  {
    etat = 3;
  }
  break;


  case 7:
  goLeft();
  delay(200);
  if (captG == 0)
  {
    etat = 2;
  }
  break;
  }
}
