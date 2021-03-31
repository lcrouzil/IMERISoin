from typing import Optional

from fastapi import FastAPI

#call features
#import features

#for week and timestamp
import time
from datetime import datetime 
app = FastAPI()

from features import *

@app.get("/")
def read_root():
    return {"Hello": "World"}


# Patients et essais cliniques

@app.get("/Patients/addMedicine/{medicine}/{name}")
# Définit un nouveau médicament avec un numéro ID et nom
def addMedicine(medicine: str,name : str): #(medicine:nb du medoc/name:nom du medoc)
    return newMedicine(medicine,name)
    
    

@app.get("/Patients/addPatient/{room}/{patientID}/{week}")
# Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
def addPatient(room : int ,patientID : int,week : int):
    return newPatient(room,patientID,week)


@app.get("/Patients/getPatientCondition/{patientID}")
# Connaître l'état de santé d'un patient
def getPatientCondition(patientID : int):
    return patientCondition(patientID)

@app.get("/Patients/setPatientCondition")
# Définit la condition du patient entre cured/stable/dead
def setPatientCondition(patientID : int, condition : str):
    return newPatientCondition(patientID,condition)

@app.get("/Patients/setRoomMedicine")
# Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
def setRoomMedicine(room : int, medicine : int, week : Optional[int]):
    return newRoomMedicine(room,medicine,week)

@app.get("/Patients/getRoomMedicine")
# Retourne le médicament à fournir dans la room
def getRoomMedicine(room : int, week : Optional[int]):
    return roomMedicine(room,week)

@app.get("/Patients/getStats")
# Retourne les éléments en fonction des paramètres optionnels
def getStats(week : Optional[int], room : Optional[int], medicine : Optional[int], state : Optional[str]):
    return patientStats(week,room,medicine,state)



# Gestion des robots et des consignes


@app.get("/Robots/addOrder")
# Ajouter la consigne medicament pour telle room (status "to do")
def addOrder(room: int,medicine : int):
    return newOrder(room,medicine)
    

@app.get("/Robots/getOrder")
# Lit la première consigne disponible
def getOrder(void):
    return firstOrder()
    
    #ou code error si plus de consigne disponible

@app.get("/Robots/checkOrder")
# Retourne le statut d'avancement de la consigne
def CheckOrder(order : str):
    return runningOrder(order)
    

@app.get("/Robots/setOrder")
# Modifie le statut d'une consigne entre 'delivered'(medicament fourni) et 'done' (robot a la base) avec le timestamp
def SetOrder(order : str, status : str):
    return modifyStateOrder(order,status)


@app.get("/Robots/getPosition")
# Renvoie le noeud où le robot est actuellement
def getPosition(robot : int):
    return robotPosition(robot)
    
    
@app.get("/Robots/setPosition")
# Enregistre le noeud où est arrivé le robot + timestamp
def setPosition(robot:int, node:int):
    return robotAddNode(robot,node)


@app.get("/Robots/getHistory")
# Historique des mouvements du robot
def getHistory(robot: int):
    return robotHistory(robot)
    

@app.get("/Robots/alertRobot")
# Crée une alerte (s'il est perdu)
def alertRobot(robot: int):
   
    return code

@app.get("/Robots/alerts")
def alerts():
    #renvoie la liste des alertes en cours (non réglées)
    return {"liste des alertes)" : afaire}

@app.get("/Robots/fixAlert")
def fixAlert(alert : str): #quel type l'alerte?
    #alerte résolue (passe en statut réglé)
    return code

#Gestion des chemins et reservation

@app.get("/Robots/getPath/{room}")
def getPath(room : int):
    path = "RIEN"
    if(room == 1) :
        path = "0F5F6F7L2L1F5R"
    elif(room == 2):
        path = "0F5F6L1L5R"
    elif(room == 3):
        path = "0F5F6F7R3R4F5L"
    elif(room == 4):
        path = "0F5F6R4R5L"
    else :
        path = "This room don't exist"
    
    #donne le chemin sous forme d'une chaîne de caractère
    return {"room": room, "path": path }


@app.get("/Robots/setPath/{room}")
def setPath(room : int, path: str):
    #enregistre le chemin pour aller dans la chambre et revenir
    return {"code": 200}

@app.get("/Robots/getNode")
def getNode(node : int):
    #indique si un noeud est réservé ou pas
    return {"node":node, "booked":booked}

@app.get("/Robots/setNode")
def setNode(node: int):
    #réserve un noeud
    return {"node":node,"booked":booked}