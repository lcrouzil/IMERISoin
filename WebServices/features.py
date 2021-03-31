#-*- coding: utf-8 -*-

from typing import Optional

import time
from datetime import datetime 

# Patients et essais cliniques


# Définit un nouveau médicament avec un nom
def newMedicine(medicine: int,name: str):
    #Ajouter dans la database
    if(True) :
        code = 200
    else :
        code = 404
    return {"code": code}


# Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
def newPatient(room : int ,patientID : int,week : int):
    #Ajouter dans la database
    week = str(week)
    week = datetime.strptime(week, "%Y%m%d").strftime("%Y/%m/%d")
    if(1) :
        code = 200
    else :
        code = 404
    return {"code": code,"room": room,"patientID": patientID,"week": week}


# Renvoie l'état de santé d'un patient entre "Cured"/"Stable"/"Dead"
def patientCondition(patientID : int):
    #Questionner la database
    return {"condition": condition}


# Définit la condition du patient entre cured/stable/dead
def newPatientCondition(patientID : int, condition : str) :
    #Ajouter dans la database entre "cured"/"stable"/"dead"
    if(1) :
        code = 200
    else :
        code = 404
    return{"code": code}


# Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
def newRoomMedicine(room : int, medicine : int, week : Optional[int]):
    #Ajouter dans la database la medicine à donner dans la room
    if(1) :
        code = 200
    else :
        code = 404
    return{"code": code}


# Retourne le médicament à fournir dans la room
def roomMedicine(room : int, week : Optional[int]):
    #Questionner la database
    return {"room": room,"week": week,"medicine": medicine}


# Retourne les éléments en fonction des paramètres optionnels
def patientStats(week : Optional[int], room : Optional[int], medicine : Optional[int], state : Optional[str]):
    #Questionner la database
    return {"week":week} #retourner une liste de tout


# Gestion des robots et des consignes


# Ajouter la consigne medicament pour telle room (status "to do")
def newOrder(room: int,medicine : int):
    #Ajouter dans la database la consigne à faire (Aller dans telle room avec tel médicament)
    if(1) :
        code = 200
    else :
        code = 404
    return{"code": code}


# Lit la première consigne disponible
def firstOrder(void):
    #Questionner la database
    return {"order": order, "room": room, "medicine": medicine}


# Retourne le statut d'avancement de la consigne
def runningOrder(order : str) :
    #Questionner la database
    return {"order": order, "status": status}


# Modifie le statut d'une consigne entre 'delivered'(medicament fourni) et 'done' (robot a la base) avec le timestamp
def modifyStateOrder(order : str, status : str):
    #Ajouter dans la database le nouvel état de la consigne avec le timestamp
    if(True) :
        code = 200
    else :
        code = 404
    return{"code": code}


# Renvoie le noeud où le robot est actuellement
def robotPosition(robot:int, node:int):
    #Questionner la database
    return {"robot": robot_id, "node": node, "timestamp": timestamp}


# Enregistre le noeud où est arrivé le robot + timestamp
def robotAddNode(robot:int, node:int):
    #Ajouter dans la database le nouveau noeud où se situe le robot
    if(True) :
        code = 200
    else :
        code = 404
    return{"code": code}


# Historique des mouvements du robot
def robotHistory(robot: int):
    # Questionner la base de données
    return {"liste des mouvements node/timestamp" : afaire}