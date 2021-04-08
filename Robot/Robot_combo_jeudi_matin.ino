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

#define END_m1 5        // Allumer moteur Droit
#define ENG_m2 4       // Allumer moteur gauche

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
int etat=0;
int pasloop = 0;


// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char dir;
int room=0;
int orderId;
String cheminChambre="";
int node;

//capteur de distance 
int reading; //DECLARATION DES VARIABLES POUR QUE L'ULTRASONIC SENSOR POUR LIRE LA VALEUR

//######################################
//##                                  ##
//##           protocole              ##
//##                                  ##
//######################################

const char *ssid = "IMERIR_IoT";  //ENTER YOUR WIFI SETTINGS
const char *password = "kohWoong5oox";

//const char *ssid = "iPhone";  //ENTER YOUR WIFI SETTINGS
//const char *password = "Charice712";

String host = "http://10.3.6.197:8000";//adresse ip de l'hote
//const char *host = "172.20.10.3";//adresse ip de l'hote
//const int httpsPort = 8000;  //HTTPS= 443 and HTTP = 80
const int robotId=01;


//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################
 /*Fonction Avancer tout droit*/
void goAhead(){
      analogWrite(END_m1, 850);  // Mettre la vitesse du Moteur Droit à 700
      analogWrite(ENG_m2, 800);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }

 /*Fonction tourner à Gauche  */
void goRight(){
      analogWrite(END_m1, 0);    // Eteindre Moteur Droit
      analogWrite(ENG_m2, 800);  //Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, HIGH);// Sens du Moteur Droit
      digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
  }
  
 /*Fonction tourner à Droite  */
void goLeft(){
     analogWrite(END_m1, 850);  //Mettre la vitesse du Moteur Droit à 700
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
  if ( WiFi.status() == WL_CONNECTED ) {
    HTTPClient http;
    String serverPath = host + "/getOrder";
    http.begin(serverPath.c_str());
    int httpResponseCode = http.GET();
    if ( httpResponseCode > 0 ) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      String payload = http.getString();
      
      StaticJsonDocument<200> output;
      deserializeJson(output, payload);
      room = output["room"];
      orderId=output["order"];
      int medoc=output["medicine"]; 
      
    } else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
      
    }
    // Free resources
    http.end();
  } else {
    Serial.println("WiFi Disconnected");
    
  }
}



//######################################
//##                                  ##
//##         réseaux demande chemin   ##
//##                                  ##
//######################################
String getPath( int room ) {
  if ( WiFi.status() == WL_CONNECTED ) {
    HTTPClient http;
    String serverPath = host + "/getPath/" + room ;
    http.begin(serverPath.c_str());
    int httpResponseCode = http.GET();
    if ( httpResponseCode > 0 )
    {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      String payload = http.getString();
      Serial.println(payload);

      StaticJsonDocument<200> output;
      deserializeJson(output, payload);
      String path = output["Path"];
      return path;
    }
    else
    {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
      return "erreur";
    }
    http.end();
  }
  else
  {
    Serial.println("WiFi Disconnected");
    return "erreur";
  }
}



/*******************************************************************
 * 
 *reseau set 
 *"/Robots/setPosition/"
 *
 *******************************************************************/
 void setPosition(int robotId,int node)
{
  if ( WiFi.status() == WL_CONNECTED ) {
    HTTPClient http;
    String serverPath = host + "/setPosition/" + robotId + "/" + node;
    http.begin(serverPath.c_str());
    int httpResponseCode = http.GET();
    if ( httpResponseCode > 0 ) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      //String payload = http.getString();
      //Serial.println(payload);
     // StaticJsonDocument<200> output;
      //deserializeJson(output, payload);
    } else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    // Free resources
    http.end();
  } else {
    Serial.println("WiFi Disconnected");
  }
}


/*******************************************************************
 * 
 *reseau set order
 *
 *
 *******************************************************************/
 void setOrder(int order,String orderStatus)
{
  if ( WiFi.status() == WL_CONNECTED ) {
    HTTPClient http;
    String serverPath = host + "/setOrder/" + order + "/" + orderStatus;
    http.begin(serverPath.c_str());
    int httpResponseCode = http.GET();
    if ( httpResponseCode > 0 ) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      //String payload = http.getString();
      //Serial.println(payload);
      //StaticJsonDocument<200> output;
      //deserializeJson(output, payload);
    } else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    // Free resources
    http.end();
  } else {
    Serial.println("WiFi Disconnected");
  }
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

}

void loop()
{
  //reading = ultrasonic.read(); 
  if(pasloop == 0)
  {
    getOrder();
    if(numChambre!=0)
    {
      cheminChambre = getPath(room);
      pasloop++;
    }
    else
    {
      Serial.println("pas d'ordre");
    }
    
  }
  /*
  Serial.println("reading");
  Serial.println(reading);
  if (reading < 5){
    stopRobot();
    delay(1000);
  }
  delay(50);
  */

  //######################################
  //##                                  ##
  //##           robot                  ##
  //##                                  ##
  //######################################
  int captD = digitalRead(D7); // Lire la valeur du capteur droit
  int captG = digitalRead(D5); // Lire la valeur du capteur gauche
 
  switch (etat) {
    
    //##################################### ETAT INITIAL : AVANCE #####################################
    
    case 0:
    goAhead();
    delay(300);
    etat = 1;
    break;
    
    //###################################################################################################

    //######################################### ETAT 1: SUIVI DE LIGNE #########################################
    
    case 1:
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
      etat = 2;
    }
    break;
    
    //###################################################################################################

    //####################### ETAT INTERSECTION (2): LIT LA CHAINE DE CARACTERE ##########################
    
    // EN FONCTION DE LA CHAINE, AVANCE OU TOURNE A GAUCHE OU A DROITE
    case 2:
    dir = direct[iteration];  
    noeud = direct[pos]-'0';
    stopRobot();
    delay(1000);

    Serial.println("noeud");
    Serial.println(noeud);

    if (node==room)
    {
      
      stopRobot();
      setOrder(orderId,"Delivered");
      delay(4000);
      Serial.println("départ de la chambre");
    }

    
    if ( dir== 'F')
    {
      etat = 0;
      iteration = iteration + 2;
      pos = pos +2;     
    }
 
    else if( dir == 'L')
    {
      goLeft();
      delay(500);
      iteration = iteration + 2;
      pos = pos +2;
      etat = 1;
    }
   
    else if( dir=='R')
    {
      goRight();
      delay(500);
      iteration = iteration + 2;
      pos = pos +2;
      etat = 1;
    }
    else if(dir =='\0')
    {
      Serial.println("on est done");
      statusOrder="Done";
      setOrder();
      pasloop = 0;
      iteration=1;
      pos =0;
    }
    
   
    break;
    
    //####################################################################################################
       
    
  }

}
