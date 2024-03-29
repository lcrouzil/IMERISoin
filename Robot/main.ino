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

#define END_m1 5 // Allumer moteur Droit
#define ENG_m2 4 // Allumer moteur gauche

#define END_m3 0 // Sens du moteur droit
#define ENG_m4 2 // Sens du moteur Gauche
#define D5 14    // pin capteur gauche
//#define D6  12
#define D7 13 // pin capteur droit
//#define D8  15 //

//######################################
//##                                  ##
//##           VAR                    ##
//##                                  ##
//######################################

//char web[30];
int numChambre;
int etat = 0;
int pasloop = 0;

// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char noeud;
char dir;
String direct;
String statusOrder;
int orderId;
//######################################
//##                                  ##
//##           protocole              ##
//##                                  ##
//######################################

const char *ssid = "AlexisWifi"; //ENTER YOUR WIFI SETTINGS
const char *password = "alexis666";
// const char *ssid = "iPhone"; //ENTER YOUR WIFI SETTINGS
// const char *password = "Charice712";

// const char *host = "172.20.10.3"; //adresse ip de l'hote
const char *host = "192.168.43.231"; //adresse ip de l'hote
//const char *host = "172.20.10.3";//adresse ip de l'hote

const int httpsPort = 8000; //HTTPS= 443 and HTTP = 80
const int Id_Robot = 01;

//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################
/*Fonction Avancer tout droit*/
void goAhead()
{
    analogWrite(END_m1, 750);   // Mettre la vitesse du Moteur Droit à 700
    analogWrite(ENG_m2, 750);   // Mettre la vitesse du Moteur Gauche à 700
    digitalWrite(END_m3, HIGH); // Sens du Moteur Droit
    digitalWrite(ENG_m4, HIGH); //Sens du Moteur Gauche
}

/*Fonction tourner à Gauche  */
void goRight()
{
    analogWrite(END_m1, 0);     // Eteindre Moteur Droit
    analogWrite(ENG_m2, 750);   //Mettre la vitesse du Moteur Gauche à 700
    digitalWrite(END_m3, LOW);  // Sens du Moteur Droit
    digitalWrite(ENG_m4, HIGH); //Sens du Moteur Gauche
}

/*Fonction tourner à Droite  */
void goLeft()
{
    analogWrite(END_m1, 750);   //Mettre la vitesse du Moteur Droit à 700
    analogWrite(ENG_m2, 0);     // Eteindre Moteur Gauche
    digitalWrite(END_m3, HIGH); // Sens du Moteur Droit
    digitalWrite(ENG_m4, LOW);  //Sens du Moteur Gauche
}

/*Fonction arreter le Robot */
void stopRobot()
{
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
    WiFiClient httpsClient; //Declare object of class WiFiClient
    //Serial.println(host);

    httpsClient.setTimeout(100); // 15 Seconds
    delay(1000);

    // Tentative de connexion :
    Serial.print("HTTPS Connecting");
    int r = 0; //retry counter
    while ((!httpsClient.connect(host, httpsPort)) && (r < 30))
    {
        delay(100);
        Serial.print(".");
        r++;
    }

    if (r == 30)
    {
        Serial.println("Connection failed");
    }
    else
    {
        Serial.println("Connected to web");
    }

    // Envoie d'une intersection :

    // Construction de la requete :
    String Link;
    Link = "/";

    Serial.print("requesting URL: ");
    //Serial.println(host+Link);

    // Envoie requete chemin
    httpsClient.print(String("GET ") + "/getOrder" + " HTTP/1.1\r\n" + "Host: 10.3.6.197" + ":" + httpsPort + "\r\n" + "Connection: close\r\n\r\n");
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
    while (httpsClient.available())
    {
        line = httpsClient.readStringUntil('\n'); //Read Line by Line
        Serial.println(line);                     //Print response
    }
    Serial.println("==========");
    Serial.println("closing connection");

    // déserialisation

    StaticJsonDocument<200> order;
    DeserializationError error = deserializeJson(order, line); //(document, input)

    // Test
    if (error)
    {
        Serial.print(F("deserializeJson() failed: "));
        Serial.println(error.f_str());
        return;
    }
    numChambre = order["room"];
    orderId = order["order"];
    int medoc = order["medicine"];

    Serial.println(numChambre);
    Serial.println(orderId);
    Serial.println(medoc);
    //verif

    Serial.println(line);
    // Attente :
    //delay(2000);  //GET Data at every 2 seconds
}

//######################################
//##                                  ##
//##         réseaux demande chemin   ##
//##                                  ##
//######################################
void getPath()
{
    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object of class WiFiClient
    //Serial.println(host);

    httpsClient.setTimeout(100); // 15 Seconds
    delay(1000);

    // Tentative de connexion :
    Serial.print("HTTPS Connecting");
    int r = 0; //retry counter
    while ((!httpsClient.connect(host, httpsPort)) && (r < 30))
    {
        delay(100);
        Serial.print(".");
        r++;
    }

    if (r == 30)
    {
        Serial.println("Connection failed");
    }
    else
    {
        Serial.println("Connected to web");
    }

    // Envoie d'une intersection :
    // int intersection = 5;
    // String STR_intersection;
    // STR_intersection = String(intersection); //String to interger conversion

    // Construction de la requete :
    String Link;
    Link = "/";

    Serial.print("requesting URL: ");
    //Serial.println(host+Link);

    // Envoie requete chemin
    httpsClient.print(String("GET ") + "/getPath/" + numChambre + " HTTP/1.1\r\n" + "Host: 10.3.6.197" + ":" + httpsPort + "\r\n" + "Connection: close\r\n\r\n");
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
    while (httpsClient.available())
    {
        line = httpsClient.readStringUntil('\n'); //Read Line by Line
        Serial.println(line);                     //Print response
    }
    Serial.println("==========");
    Serial.println("closing connection");

    // déserialisation

    StaticJsonDocument<200> chemin;
    DeserializationError error = deserializeJson(chemin, line); //(document, input)

    // Test
    if (error)
    {
        Serial.print(F("deserializeJson() failed: "));
        Serial.println(error.f_str());
        return;
    }
    const char *cheminChambre = chemin["path"];
    direct = String(cheminChambre);

    //verif
    Serial.println(direct);

    // Attente :
    //delay(2000);  //GET Data at every 2 seconds
}

/*******************************************************************
 * 
 *reseau set 
 *"/Robots/setPosition/"
 *
 *******************************************************************/
void setPosition()
{

    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object of class WiFiClient
    Serial.println(host);

    httpsClient.setTimeout(100); // 15 Seconds
    delay(1000);

    // Tentative de connexion :
    Serial.print("HTTP Connecting");
    int r = 0; //retry counter
    while ((!httpsClient.connect(host, httpsPort)) && (r < 30))
    {
        delay(100);
        Serial.print(".");
        r++;
    }

    if (r == 30)
    {
        Serial.println("Connection failed");
    }
    else
    {
        Serial.println("Connected to web");
    }
    // Envoie d'une intersection :
    // int intersection = 5;
    // String STR_intersection;
    // STR_intersection = String(intersection); //String to interger conversion

    // Construction de la requete :
    String Link;
    Link = "/";

    Serial.print("requesting URL: ");
    Serial.println(host + Link);

    // Envoie de la requete :
    //int(noeud) changer en noeud
    httpsClient.print(String("GET ") + "/setPosition/" + noeud + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Connection: close\r\n\r\n");
    Serial.println("Envoie position robot");

    // Reception du message :
    while (httpsClient.connected())
    {
        String line = httpsClient.readStringUntil('\n');
        if (line == "\r")
        {
            Serial.println("headers received");
            break;
        }
    }

    // Impression du message :
    Serial.println("reply was:");
    Serial.println("==========");
    String line;
    while (httpsClient.available())
    {
        line = httpsClient.readStringUntil('\n'); //Read Line by Line
        Serial.println(line);                     //Print response
    }
    Serial.println("==========");
    Serial.println("closing connection");
    //delay(2000);
}

/*******************************************************************
 * 
 *reseau set order
 *
 *
 *******************************************************************/
void setOrder()
{

    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object of class WiFiClient
    Serial.println(host);

    httpsClient.setTimeout(100); // 15 Seconds
    delay(1000);

    // Tentative de connexion :
    Serial.print("HTTP Connecting");
    int r = 0; //retry counter
    while ((!httpsClient.connect(host, httpsPort)) && (r < 30))
    {
        delay(100);
        Serial.print(".");
        r++;
    }

    if (r == 30)
    {
        Serial.println("Connection failed");
    }
    else
    {
        Serial.println("Connected to web");
    }

    // Construction de la requete :
    String Link;
    Link = "/";

    Serial.print("requesting URL: ");
    Serial.println(host + Link);

    // Envoie de la requete :
    httpsClient.print(String("GET ") + "/setOrder/" + orderId + "/" + statusOrder + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Connection: close\r\n\r\n");
    Serial.println("Envoie position robot");

    // Reception du message :
    while (httpsClient.connected())
    {
        String line = httpsClient.readStringUntil('\n');
        if (line == "\r")
        {
            Serial.println("headers received");
            break;
        }
    }

    // Impression du message :
    Serial.println("reply was:");
    Serial.println("==========");
    String line;
    while (httpsClient.available())
    {
        line = httpsClient.readStringUntil('\n'); //Read Line by Line
        Serial.println(line);                     //Print response
    }
    Serial.println("==========");
    Serial.println("closing connection");
    //delay(2000);
}

/* Fonction demi-tour*/
void demi_tour()
{
    digitalWrite(END_m1, HIGH); // Moteur Droit au max
    digitalWrite(END_m3, HIGH); // Sens du Moteur Droit
    digitalWrite(ENG_m2, HIGH); // Moteur Gauche au max
    digitalWrite(ENG_m4, LOW);  // Sens du Moteur Gauche
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
    WiFi.mode(WIFI_STA); //Only Station No AP, This line hides the viewing of ESP as wifi hotspot

    WiFi.begin(ssid, password); //Connect to your WiFi router
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
    Serial.println(WiFi.localIP()); //IP address assigned to your ESP

    pinMode(END_m1, OUTPUT);
    pinMode(ENG_m2, OUTPUT);
    pinMode(END_m3, OUTPUT);
    pinMode(ENG_m4, OUTPUT);
    
    pinMode(D5, INPUT);
    pinMode(D7, INPUT);
    /********************************************************************/
}

void loop()
{
    //Serial.println(pasloop);
    if (pasloop == 0)
    {
        getOrder();
        if (numChambre != 0)
        {
            getPath();
            pasloop = 1;
        }
        else
        {
            Serial.println("pas d'ordre");
        }
    }

    //######################################
    //##                                  ##
    //##           robot                  ##
    //##                                  ##
    //######################################
    int captD = digitalRead(D7); // Lire la valeur du capteur droit
    int captG = digitalRead(D5); // Lire la valeur du capteur gauche
    //dir = cheminChambre[iteration];
    //noeud = cheminChambre[pos]-'0';

    switch (etat)
    {

        //##################################### ETAT INITIAL : AVANCE #####################################

    case 0:
        goAhead();
        delay(300);
        /*
    if (!captD && !captG)
    {
      etat=1;
    }
    */
        etat = 1;
        break;

        //###################################################################################################

        //######################################### ETAT 1: SUIVI DE LIGNE #########################################

    case 1:

        if (captD == 1 && captG == 0) // Si capteur gauche = noir --> etat 3
        {
            goRight();
        }
        else if (captD == 0 && captG == 1) // // Si capteur droit = noir --> etat 2
        {
            goLeft();
        }
        else if (captD == 1 && captG == 1)
        {
            etat = 2;
        }
        else if (captD == 0 && captG == 0)
        {
            // goAhead();                    // avance tant qu'un des 2 capteurs ne detecte rien
        }
        break;

    //###################################################################################################

    //####################### ETAT INTERSECTION (2): LIT LA CHAINE DE CARACTERE ##########################

    // EN FONCTION DE LA CHAINE, AVANCE OU TOURNE A GAUCHE OU A DROITE
    case 2:
        stopRobot();
        delay(1000);

        /*
    if (iteration > direct.length())
    {
      Serial;println("on est done");
      statusOrder="Done";
      setOrder();
      pasloop = 0;
      iteration=1;
      pos =0;
    }*/
        //setPosition();
        //delay(2000); //arret du robot
        dir = direct[iteration];
        noeud = direct[pos] - '0';

        Serial.println("noeud");
        Serial.println(noeud);
        Serial.println("chambre");
        Serial.println(numChambre);
        if (noeud == numChambre)
        {

            stopRobot();
            statusOrder = "Delivered";
            setOrder();
            delay(3000);
            Serial.println("départ de la chambre");
        }

        if (dir == 'F')
        {
            Serial.println("F");
            goAhead();
            delay(350);
            iteration = iteration + 2;
            pos = pos + 2;
            etat = 1;
        }

        else if (dir == 'L')
        {

            Serial.println("L");
            goLeft();
            delay(500);
            iteration = iteration + 2;
            pos = pos + 2;
            etat = 1;
        }

        else if (dir == 'R')
        {
            Serial.println("R");
            goRight();
            delay(500);
            iteration = iteration + 2;
            pos = pos + 2;
            etat = 1;
        }
        else if (dir == '\0')
        {
            Serial.println("on est done");

            goAhead();
            delay(900);
            demi_tour(); // Fais un demi-tour et se remet en position de départ
            delay(550);
            stopRobot();
            delay(1000);
            statusOrder = "Done";
            setOrder();
            pasloop = 0;
            iteration = 1;
            pos = 0;
            etat = 0;
        }

        break;

        //####################################################################################################
    }
}