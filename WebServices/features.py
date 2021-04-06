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
def newPatient(room: int, patientID: int, week: int = None):
    if add_patient(patientID, room, week):
        code = 200
    else:
        code = 404
    return {"code": code, "room": room, "patientID": patientID, "week": week}


# Crée ou modifie une chambre
def newRoom(room: int, name: str, path: str):
    add_room(room, name, path)
    return 1


# Renvoie l'état de santé d'un patient entre "Cured"/"Stable"/"Dead"
def patientCondition(patientID: int, condition: str):
    # Questionner la database
    return {"condition": condition}


# Définit la condition du patient entre cured/stable/dead
def newPatientCondition(patient_id: int, condition: str):
    # Ajouter dans la database entre "cured"/"stable"/"dead"

    set_patient_status(patient_id, condition)

    if (1):
        code = 200
    else:
        code = 404
    return {"code": code}


# Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
def newRoomMedicine(room: int, medicine: int, week: Optional[int] = 0):
    set_room_medicine(room, medicine, week)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Retourne le médicament à fournir dans la room
def roomMedicine(room: int, week: Optional[int] = 0):
    tab = []
    tab = get_room_medicine(room, week)

    return {"list": tab}


# Retourne les éléments en fonction des paramètres optionnels
def patientStats(week: Optional[int], room: Optional[int], medicine: Optional[int], state: Optional[str]):
    # Questionner la database
    return {"week": week}  # retourner une liste de tout


# Retourne tous les médicaments disponibles
def getMedicines():
    tab = []
    for id, name in get_medicine():
        tab.append({"id": id, "name": name})
        # tab[id] = name

    print(tab)

    return {"list": tab}


# Retourne la liste de tous les patients
def getPatients():
    # Database : pareil que getMedicines?
    tab = []
    for id, status in get_patient():
        tab.append({"id": id, "status": status})

    print(tab)

    return {"list": tab}


# Retourne la liste de toutes les rooms
def getRooms():
    tab = []
    for id, name, path in get_room():
        tab.append({"id": id, "path": path, "name": name})

    print(tab)

    return {"list": tab}


# Retourne la liste de tous les robots
def getRobots():
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    print(tab)

    return {"robots": tab}


def getOrders():
    tab = []
    for id, room, drug, status, timestamp in get_orders():
        print("get_orders : ", id, room, drug, status, timestamp)
        tab.append({"id": id, "room": room, "drug": drug, "status": status, "timestamp": timestamp})

    print(tab)

    return {"list": tab}


# Gestion des robots et des consignes


# Ajouter la consigne medicament pour telle room (status "to do")
def newOrder(room: int):
    code = 200
    if isinstance(room, (int, float)) == False:
        code = 404
        error = "La room doit être un int"
    else:
        add_order(room)
    return {"code": code}


# Lit la première consigne disponible
def firstOrder():
    order = {}
    order = get_order()
    return {"order": order[0], "room": order[1], "medicine": order[2]}


# TEST ORDER
def OrderTest():
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    print(tab)

    return {"robots": tab}


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
def robotPosition(robot_id: int):
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


def getJsonObjectRoom():
    tab = []
    for room_id, room_path, room_name, patient_id, patient_status, drug_id, drug_name in get_room_join():

        patient = None

        if patient_id is not None:
            patient = {
                "id": patient_id,
                "status": patient_status
            }

        drug = None
        if drug_id is not None:
            drug = {
                "id": drug_id,
                "name": drug_name
            }

        tab.append({"id": room_id,
                    "patient": patient,
                    "medicine": drug,
                    "path": room_path,
                    "name": room_name
                    })

        # tab.append({"id": room_id,
        #             "patient": {
        #                 "id": patient_id,
        #                 "status": patient_status
        #             },
        #             "medicine": {
        #                 "id": drug_id,
        #                 "name": drug_name
        #             },
        #             "path": room_path,
        #             "name": room_name
        #             })

    return {"list": tab}
