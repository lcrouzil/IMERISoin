//######################################
//##                                  ##
//##           INCLUDE                ##
//##                                  ##
//######################################

//#include <Ultrasonic.h>
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
#define D7 13 // pin capteur droit
//#define D8  15 // pin Trig du capteur Ultrason
//#define D6  12 // pin Echo du capteur Ultrason

//Ultrasonic ultrasonic (15, 12); // trigger pin + ECHO PIN

//######################################
//##                                  ##
//##           VAR                    ##
//##                                  ##
//######################################

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

// VARIABLE CPATEUR ULTRASON 
//float distance;

//######################################
//##                                  ##
//##           protocole              ##
//##                                  ##
//######################################

//Identifiants WIFI
const char *ssid = "IMERIR_IoT";  
const char *password = "kohWoong5oox";
const char *host = "172.20.10.3"; //adresse ip de l'hote
const int httpsPort = 8000;       //HTTPS= 443 and HTTP = 80
const int Id_Robot=01;

//######################################
//##                                  ##
//##           fonctions              ##
//##                                  ##
//######################################

/*Fonction Avancer tout droit*/
void goAhead()
{
    analogWrite(END_m1, 850);  // Mettre la vitesse du Moteur Droit à 700
    analogWrite(ENG_m2, 800);  // Mettre la vitesse du Moteur Gauche à 700
    digitalWrite(END_m3, LOW); // Sens du Moteur Droit
    digitalWrite(ENG_m4, LOW); //Sens du Moteur Gauche
}

/*Fonction tourner à Droite  */
void goRight()
{
    analogWrite(END_m1, 0);     // Eteindre Moteur Droit
    analogWrite(ENG_m2, 800);   //Mettre la vitesse du Moteur Gauche à 700
    digitalWrite(END_m3, HIGH); // Sens du Moteur Droit
    digitalWrite(ENG_m4, LOW);  //Sens du Moteur Gauche
}

/*Fonction tourner à Gauche  */
void goLeft()
{
    analogWrite(END_m1, 850);   //Mettre la vitesse du Moteur Droit à 700
    analogWrite(ENG_m2, 0);     // Eteindre Moteur Gauche
    digitalWrite(END_m3, LOW);  // Sens du Moteur Droit
    digitalWrite(ENG_m4, HIGH); //Sens du Moteur Gauche
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
//##      Réseaux demande ordre       ##
//##                                  ##
//######################################
void getOrder()
{
    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object de class WiFiClient
    httpsClient.setTimeout(100); 
    delay(1000);//1 seconde
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
    int intersection = 5;
    String STR_intersection;
    STR_intersection = String(intersection); //Convertir intersection en String

    // Construction de la requete :
    String Link;
    Link = "/";
    Serial.print("requesting URL: ");

    // Envoie requete chemin
    httpsClient.print(String("GET ") + "/getOrder" + " HTTP/1.1\r\n" + "Host: 10.3.6.197" + ":" + httpsPort + "\r\n" + "Connection: close\r\n\r\n");
    Serial.println("demande ordre");

    // Reception du message :
    while (httpsClient.connected())
    {
        delay(10); //10 msecondes
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

    // Déserialisation
    StaticJsonDocument<200> order;
    DeserializationError error = deserializeJson(order, line); //(document, input)

    // Test
    if (error)
    {
        Serial.print(F("deserializeJson() failed: "));
        Serial.println(error.f_str());
        return;
    }
    int ordreChambre = order["room"];
    int orderId = order["order"];
    int medoc = order["medicine"];

    //verification
    Serial.println(ordreChambre);
    Serial.println(line);
    numChambre = ordreChambre;
}

//######################################
//##                                  ##
//##      Réseaux demande chemin      ##
//##                                  ##
//######################################
void getPath()
{
    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object de class WiFiClient
    httpsClient.setTimeout(100); 
    delay(1000); //1 seconde 

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
    int intersection = 5;
    String STR_intersection;
    STR_intersection = String(intersection); //Convertir intersection en String

    // Construction de la requete :
    String Link;
    Link = "/";
    Serial.print("requesting URL: ");

    // Envoie requete chemin
    httpsClient.print(String("GET ") + "/getPath/" + numChambre + " HTTP/1.1\r\n" + "Host: 10.3.6.197" + ":" + httpsPort + "\r\n" + "Connection: close\r\n\r\n");
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
        line = httpsClient.readStringUntil('\n'); //Lire ligne par ligne
        Serial.println(line);                     //Afficher reponse
    }
    Serial.println("==========");
    Serial.println("closing connection");

    // Déserialisation
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

    //Verification
    Serial.println(direct);
}

//###################FIN GET PATH#########################//

//######################################
//##                                  ##
//##       Réseaux setPosition        ##
//##                                  ##
//######################################
void setPosition()
{

    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object of class WiFiClient
    Serial.println(host);
    httpsClient.setTimeout(100);
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
    int intersection = 5;
    String STR_intersection;
    STR_intersection = String(intersection); //Convertir intersection en String

    // Construction de la requete :
    String Link;
    Link = "/";

    Serial.print("requesting URL: ");
    Serial.println(host + Link);

    // Envoie de la requete :
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
        line = httpsClient.readStringUntil('\n'); //Lire ligne par ligne
        Serial.println(line);                     //Afficher reponse
    }
    Serial.println("==========");
    Serial.println("closing connection");
}

//#####################FIN SET POSITION##########################//

//######################################
//##                                  ##
//##       Réseaux setOrder           ##
//##                                  ##
//######################################
void setOrder()
{

    // Creation du client sécurisé :
    WiFiClient httpsClient; //Declare object of class WiFiClient
    Serial.println(host);
    httpsClient.setTimeout(100);
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
    httpsClient.print(String("GET ") + "/setOrder/" + orderId + "/" + statusOrder + "/" + " HTTP/1.1\r\n" + "Host: " + host + "\r\n" + "Connection: close\r\n\r\n");
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
//#####################FIN SET ORDER##########################//

//######################################
//##                                  ##
//##               Setup              ##
//##                                  ##
//######################################
void setup()
{
    Serial.begin(115200);
    WiFi.mode(WIFI_STA); //Only Station No AP, Masque la visualisation de l'ESP en tant que hotspot WiFi
    WiFi.begin(ssid, password); //Connexion au routeur de la WiFi
    Serial.println("");
    Serial.print("Connecting");
    // Attendre la connexion
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }

    //Si la connexion est réussis afficher l'adresse IP sur le moniteur série
    Serial.println("");
    Serial.print("Connected to ");
    Serial.println(ssid);
    Serial.print("IP address: ");
    Serial.println(WiFi.localIP()); //IP address assigned to your ESP
}
//#####################FIN SETUP##########################//

//######################################
//##                                  ##
//##         Fonction loop            ##
//##                                  ##
//######################################
void loop()
{
    if (pasloop == 0)
    {
        //Demander l'ordre au serveur
        getOrder();
        //Si le numéro de chambre n'est pas reçu demander le chemin
        if (numChambre != 0)
        {
            getPath();
            pasloop++;
        }
        else
        {
            Serial.println("pas d'ordre");
        }
    }
// Capteur Droit et Gauche du robot
    int captD = digitalRead(D7); // Lire la valeur du capteur droit
    int captG = digitalRead(D5); // Lire la valeur du capteur gauche

//######################################
//##                                  ##
//##         Machine à état           ##
//##                                  ##
//######################################
    switch (etat)
    {

        //##################################### ETAT INITIAL : AVANCE #####################################

    case 0:
        goAhead(); // Avance
        delay(300); //0.3 seconde
        etat = 1; // Repartir sur l'état 1
        break;

        //###################################################################################################

        //######################################### ETAT 1: SUIVI DE LIGNE #########################################

    case 1:
        goAhead(); // avance tant qu'un des 2 capteurs ne detecte rien
        if (captD == 1 && captG == 0) // Si le capteur Droit detecte du noir et le capteur Gauche detecte du blanc
        {
            goRight(); // Trouner à Droite
        }
        else if (captD == 0 && captG == 1) // Si le capteur Droit detecte du blanc et le capteur Gauche detecte du noir
        {
            goLeft(); // Tourner à Gauche
        }
        else if (captD == 1 && captG == 1) // Si les deux capteurs detecte du noir donc intersection 
        {
            etat = 2; // Repartir sur l'état 2 (intersection)
        }
        break;

    //###################################################################################################

    //####################### ETAT INTERSECTION (2): LIT LA CHAINE DE CARACTERE ##########################

    // EN FONCTION DE LA CHAINE, AVANCE OU TOURNE A GAUCHE OU A DROITE
    case 2:
        stopRobot();              // arrêter le robot
        delay(1000);              // 1 seconde
        dir = direct[iteration];  //Lire dans le chemin reçu les instructions
        noeud = direct[pos] - '0';//Lire dans le chemin reçu les noeuds
        
        // Si le noeud actuelle c'est le numéro de la chambre arrêter le robot et renvoyer Delivered au serveur
        if (noeud == numChambre)
        {
            stopRobot(); 
            statusOrder = "Delivered"; 
            setOrder();
            delay(3000);
        }
        
        // Si il lit F dans dir repartir en état0 avancer 
        if (dir == 'F')
        {
            iteration = iteration + 2;// incrémente iteration
            pos = pos + 2;            // incrémente pos
            etat = 0;                 // repartir en état 0
        }
        
        // Si il lit L dans dir tourner à Gauche et repartir en état 1 suivie de ligne
        else if (dir == 'L')
        {
            Serial.println("L");
            goLeft();                 //Tourne à Gauche
            delay(500);
            iteration = iteration + 2;// incrémente iteration
            pos = pos + 2;            // incrémente pos
            etat = 1;                 // repartir en état 1 suivie de ligne
        }

        // Si il lit R dans dir tourner à Droite et repartir en état 1 suivie de ligne
        else if (dir == 'R')
        {
            goRight();                //Tourne à Gauche
            delay(500);
            iteration = iteration + 2;// incrémente iteration
            pos = pos + 2;            // incrémente pos
            etat = 1;                 // repartir en état 1 suivie de ligne
        }

        //Si le robot arrive au dernier caractère de la chaine du chemin il s'arrête et renvoie Done au serveur remet pasloop à 0 pour repartir
        else if (dir == '\0')
        {
            statusOrder = "Done"; 
            setOrder();
            pasloop = 0;
            iteration = 1;
            pos = 0;
        }

        break;
    }
//#####################FIN SWITCH##########################//
}
//#####################FIN LOOP##########################//
