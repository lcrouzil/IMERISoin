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

char web[] = "0f5f6f7r3r4f5l";
int numChambre;
const char* cheminChambre;
int etat=0;



// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char noeud;
char dir;

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
}

int pasloop = 0;

void loop()
{
  if(pasloop == 0)
  {
    getOrder();
    getPath();
    pasloop++;
  }
  

  
    //delay(200);
    //######################################
    //##                                  ##
    //##           robot                  ##
    //##                                  ##
    //######################################
  int captD = digitalRead(D5); // Lire la valeur du capteur droit
  int captG = digitalRead(D6); // Lire la valeur du capteur gauche
 
  switch (etat) {
    
    //##################################### ETAT INITIAL : AVANCE #####################################
    
    case 0:
    goAhead();
    if (captD==0 && captG==0)
    {
      etat=1;
    }
    break;
    
    //###################################################################################################
    
    

    
    //######################################### ETAT 1: AVANCE #########################################
    
    // CHANGE D'ETAT SI UN DES CAPTEUR DETECTE LA LIGNE
    case 1:
    goAhead(); // avance tant qu'un des 2 capteurs ne detecte rien
    if (captD==1 && captG==0) // Si capteur gauche = noir --> etat 3
    {
      etat=3;
    }  
    else if (captD==0 && captG==1) // // Si capteur droit = noir --> etat 2
    {
      etat=2;
    }  
    break;
    
    //###################################################################################################



    
    //##################################### ETAT 2: TOURNE A DROITE #####################################
    
    // CHANGE D'ETAT SI LE CAPTEUR DROIT NE DETECTE PLUS OU SI LE DROIT ET GAUCHE DETECTE DU NOIR
    case 2:
    goRight(); // tourne à droite tant que les 2 capteurs != blanc ou != noir
    if (captD==0 && captG==0) // si les 2 capteurs == blanc --> etat 1
    {
      etat=1;
    }
    else if (captD==1 && captG==1)  // si les 2 capteurs == noir --> etat 4
    {
      etat=4;
    }
    break;
    
    //###################################################################################################



    
    //##################################### ETAT 3: TOURNE A GAUCHE #####################################
    
    // CHANGE D'ETAT SI LE CAPTEUR GAUCHE NE DETECTE PLUS OU SI LE GAUCHE ET DROIT DETECTE DU NOIR
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
    
    //####################################################################################################




    //####################### ETAT INTERSECTION (4): LIT LA CHAINE DE CARACTERE ##########################
    
    // EN FONCTION DE LA CHAINE, AVANCE OU TOURNE A GAUCHE OU A DROITE
    case 4:
    stopRobot();
    delay(2000);
    dir = web[iteration];  
    noeud = web[pos];
   
    Serial.println(noeud);
    if ( dir== 'f')
    {
      etat = 5;
    }
 
    if( dir == 'l')
    {
      etat = 6;
    }
   
    if( dir=='r')
    {
      etat = 7;
    }
    iteration = iteration + 2;
   
    break;
    
    //####################################################################################################
       
 


 
    //################################# ETAT 5: TRAVERSE L'INTERSECTION ##################################
    
    // DES QU'IL PASSE L'INTERSECTION, IL RESUIT LA LIGNE EN FONCTION DU NOEUD D'AVANT 
    case 5:
    goAhead();
    if (captG == 0)
    {
      etat = 3;
    }
    else if (captD == 0)
    {
      etat = 2;
    }
    break;
    
    //####################################################################################################



    
    //############################# ETAT 6: TOURNE A DROITE A L'INTERSECTION #############################
    
    // AU BOUT DE 150ms, IL RESUIT LA LIGNE
    case 6:
    goRight();
    delay(300);
    if (captD==0) 
    {
      //if (captG == 0)
      //{
        etat = 3;
      //}
    }
    break;
    
    //####################################################################################################
 



    //############################# ETAT 7: TOURNE A GAUCHE A L'INTERSECTION #############################
    
    // AU BOUT DE 150ms, IL RESUIT LA LIGNE
    case 7:
    goLeft();
    delay(300);
    if (captG==0) {
      //if (captD == 0)
    //{
      etat = 2;
   // }
    }
    break;
    
    //####################################################################################################
    
  }

}
