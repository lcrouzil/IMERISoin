#define END_m1 5        // Allumer moteur Droit
#define ENG_m2 4       // Allumer moteur gauche

#define END_m3 0        // Sens du moteur droit
#define ENG_m4 2      // Sens du moteur Gauche
#define D5  14 // pin capteur droit
#define D6  12 // pin capteur gauche

char web[] = "0f5f6l4l5r";

// COMPTEURS
int iteration = 1;
int pos = 0;

// VARIABLE POSITION ET DIRECTION
char noeud;
char dir;

// ON INITIALISE L'ETAT A 0
int etat=0;

void setup() {  
    Serial.begin(9600);
    pinMode(D5,INPUT);
    pinMode(D6,INPUT);
    pinMode(END_m1, OUTPUT);  // Moteur Droit en sortie
    pinMode(ENG_m2, OUTPUT);  // Moteur Gauche en sortie
    pinMode(END_m3, OUTPUT);  // Sens du Moteur Droit en sortie
    pinMode(ENG_m4, OUTPUT);  // Sens du Moteur Gauche en sortie
  }
 
  /*Fonction Avancer tout droit*/
void goAhead(){
      analogWrite(END_m1, 800);  // Mettre la vitesse du Moteur Droit à 700
      analogWrite(ENG_m2, 800);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, HIGH); // Sens du Moteur Droit
      digitalWrite(ENG_m4, HIGH); //Sens du Moteur Gauche
  }

 /*Fonction tourner à Gauche  */
void goLeft(){
      analogWrite(END_m1, 0);    // Eteindre Moteur Droit
      analogWrite(ENG_m2, 800);  // Mettre la vitesse du Moteur Gauche à 700
      digitalWrite(END_m3, LOW);// Sens du Moteur Droit
      digitalWrite(ENG_m4, HIGH); // Sens du Moteur Gauche
  }
 
 /*Fonction tourner à Droite  */
void goRight(){
     analogWrite(END_m1, 800);   // Mettre la vitesse du Moteur Droit à 700
     analogWrite(ENG_m2, 0);     // Eteindre Moteur Gauche
     digitalWrite(END_m3, HIGH);  // Sens du Moteur Droit
     digitalWrite(ENG_m4, LOW); // Sens du Moteur Gauche
  }

 /*Fonction arreter le Robot */
void stopRobot(){  
      digitalWrite(END_m1, LOW); // Eteindre Moteur Droit
      digitalWrite(END_m3, LOW); // Sens du Moteur Droit
      digitalWrite(ENG_m2, LOW); // Eteindre Moteur Gauche
      digitalWrite(ENG_m4, LOW); // Sens du Moteur Gauche
  }



 /*Fonction loop*/
void loop(){  
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
 
