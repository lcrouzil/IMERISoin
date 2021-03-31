/*
 * HTTPS Secured Client GET Request
 * Copyright (c) 2019, circuits4you.com
 * Adapted for students
 * All rights reserved.
 * https://circuits4you.com 
 * Connects to WiFi HotSpot. */

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h> 
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>


/* Set these to your desired credentials. */
const char *ssid = "IMERIR_IoT";  //ENTER YOUR WIFI SETTINGS
const char *password = "kohWoong5oox";

//Link to read data from https://jsonplaceholder.typicode.com/comments?postId=7
//Web/Server address to read/write from 
const char *host = "10.3.6.197";//adresse ip de l'hote 
const int httpsPort = 8000;  //HTTPS= 443 and HTTP = 80
const int Id_Robot=01;




//SHA1 finger print of certificate use web browser to view and copy
//const char fingerprint[] PROGMEM = "F6 23 3E AC 7A 1D 03 63 15 E2 4F 57 B6 10 23 2E 22 53 51 4E";




//=======================================================================
//                    Power on setup
//=======================================================================

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




//=======================================================================
//                    Main Program Loop
//=======================================================================

void loop()
{
  // Creation du client sécurisé :
  WiFiClient httpsClient;    //Declare object of class WiFiClient
  Serial.println(host);


  // Définition de la clé SHA1 :
 // Serial.printf("Using fingerprint '%s'\n", fingerprint);
  //httpsClient.setFingerprint(fingerprint);
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


  // Envoie de la requete connexion:

  // Envoie requete Id_robot
  httpsClient.print(String("GET ")+ "/Robots/getPath" + Link + "3" + " HTTP/1.1\r\n" + "Host: " + host +":"+ httpsPort  + "\r\n" + "Connection: close\r\n\r\n");
  Serial.println("demande chemin");
 

  // Reception du message :
  while (httpsClient.connected())
  {
    delay(10);
    /*String line = httpsClient.readStringUntil('\n');
    Serial.println (line);
     Serial.println ("WESH1");
    if (line == "\r") {
      
      Serial.println("headers received");
      break;
    }*/
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
}
