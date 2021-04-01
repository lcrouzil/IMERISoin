# -*- coding: utf-8 -*-

from typing import Optional

import time
from datetime import datetime

from database import *


# Patients et essais cliniques


# Définit un nouveau médicament avec un nom
def newMedicine(medicine: int, name: str):
    set_medicine(medicine, name)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
def newPatient(room: int, patientID: int, name: str, week: int):
    add_patient(patientID, name)
    # week = str(week)
    # week = datetime.strptime(week, "%Y%m%d").strftime("%Y/%m/%d")
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code, "room": room, "patientID": patientID, "name": name, "week": week}


# Crée ou modifie une chambre
def newRoom(room: int, name: str):
    add_room(room,name)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Renvoie l'état de santé d'un patient entre "Cured"/"Stable"/"Dead"
def patientCondition(patientID: int):
    # Questionner la database
    return {"condition": condition}


# Définit la condition du patient entre cured/stable/dead
def newPatientCondition(patientID: int, condition: str):
    # Ajouter dans la database entre "cured"/"stable"/"dead"
    if (1):
        code = 200
    else:
        code = 404
    return {"code": code}


# Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
def newRoomMedicine(room: int, medicine: int, week: Optional[int]=0):
    set_room_medicine(room,medicine,week)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Retourne le médicament à fournir dans la room
def roomMedicine(room: int, week: Optional[int] = 0):
    tab = []
    tab = get_room_medicine(room,week)

    return {"list": tab}
    

# Retourne les éléments en fonction des paramètres optionnels
def patientStats(week: Optional[int], room: Optional[int], medicine: Optional[int], state: Optional[str]):
    # Questionner la database
    return {"week": week}  # retourner une liste de tout


# Retourne tous les médicaments disponibles
def getMedicines():
    tab = []
    for id, name in get_medicine():
        tab.append({"medicine": id, "name": name})
        # tab[id] = name

    print(tab)

    return {"list": tab}


# Retourne la liste de tous les patients
def getPatients():
    # Database : pareil que getMedicines?
    tab = []
    for id, name, status in get_patient():
        tab.append({"id": id, "name": name, "status": status})

    print(tab)

    return {"patients": tab}


# Retourne la liste de toutes les rooms
def getRooms():
    tab = []
    for id, patient_id, drug_id, path, name in get_room():
        tab.append({"id": id, "patient_id": patient_id, "drug_id": drug_id, "path": path, "name": name})

    print(tab)

    return {"rooms": tab}


# Retourne la liste de tous les robots
def getRobots():
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    print(tab)

    return {"robots": tab}


# Gestion des robots et des consignes


# Ajouter la consigne medicament pour telle room (status "to do")
def newOrder(room: int, medicine: int):
    b = add_order(room, medicine)
    print(b)
    return 1


# Lit la première consigne disponible
def firstOrder():

    order = get_order()
    tab = {"order" : order[0], "room": order[1], "medicine": order[2]}

    return tab


# Retourne le statut d'avancement de la consigne
def runningOrder(order: str, status=""):
    # Questionner la database
    return {"order": order, "status": status}


# Modifie le statut d'une consigne entre 'delivered'(medicament fourni) et 'done' (robot a la base) avec le timestamp
def modifyStateOrder(order: str, status: str):
    # Ajouter dans la database le nouvel état de la consigne avec le timestamp
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Renvoie le noeud où le robot est actuellement
def robotPosition(robot_id: int, node: int):
    # Questionner la database
    return {"robot": robot_id, "node": node, "timestamp": timestamp}


# Enregistre le noeud où est arrivé le robot + timestamp
def robotAddNode(robot: int, node: int):
    # Ajouter dans la database le nouveau noeud où se situe le robot
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Historique des mouvements du robot
def robotHistory(robot: int):
    # Questionner la base de données
    return {"liste des mouvements node/timestamp": afaire}


# Crée une alerte (s'il est perdu)
def robotLost(robot: int):
    # Ajouter base de données
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}
