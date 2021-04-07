# -*- coding: utf-8 -*-

from typing import Optional

import time
from datetime import datetime

from database import *


# Patients et essais cliniques

def setMedicine(medicine: int, name: str):
    ''' setMedicine
    Définit un nouveau médicament avec un nom
    Parameters : medicine/name
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré '''
    set_medicine(medicine, name)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
def newPatient(room: int, patientID: int, week: int = None):
    ''' addPatient
    Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
    Parameters : room/patientID/week
    JSON : {"code":200,"patientID":patientID} si enregistré
           {"code":404,"error":error} si non enregistré '''
    print(room, patientID, week)
    if add_patient(patientID, room, week):
        code = 200
    else:
        code = 404
    return {"code": code, "room": room, "patientID": patientID, "week": week}


def setRoom(room: int, name: str, path: str):
    ''' setRoom
    Crée ou modifie une chambre
    Parameters : room/name/(path)
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    add_room(room, name, path)
    return 1


def getPatientCondition(patientID: int):
    ''' getPatientCondition
    Renvoie l'état de santé d'un patient entre "Cured"/"Stable"/"Dead"
    Parameters : patientID
    JSON : {"condition" : condition}
    '''
    # Questionner la database
    return {"condition": condition}



def setPatientCondition(patient_id: int, condition: str):
    ''' setPatientCondition
    Définit la condition du patient entre cured/stable/dead
    Parameters : patientID,condition
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    # Ajouter dans la database entre "cured"/"stable"/"dead"

    set_patient_status(patient_id, condition)

    if (1):
        code = 200
    else:
        code = 404
    return {"code": code}


def setRoomMedicine(room: int, medicine: int, week: Optional[int] = 0):
    ''' setRoomMedicine
    Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
    Parameters : room/medicine/(week)
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    set_room_medicine(room, medicine, week)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


def getRoomMedicine(room: int, week: Optional[int] = 0):
    ''' getRoomMedicine
    Retourne le médicament à fournir dans la room
    Parameters : room/(week)
    JSON : {"room": room,"week": week,"medecine": medicine}'''
    tab = []
    tab = get_room_medicine(room, week)

    return {"list": tab}


def patientStats(week: Optional[int], room: Optional[int], medicine: Optional[int], condition: Optional[str]):
    ''' getStats
    Retourne les éléments en fonction des paramètres optionnels
    Parameters :(week)/(room)/(medicine)/(condition)
    JSON : {"list":["week":week,"room":room,"medicine":medicine,"condition":condition]}
    '''
    # Questionner la database
    return {"week": week}  # retourner une liste de tout


def listMedicines():
    ''' listMedicines
    Retourne tous les médicaments disponibles
    Parameters : NULL
    JSON : {‘list’: [{‘medicine’: medicine, ‘name’: name}, …]}
'''
    tab = []
    for id, name in get_medicine():
        tab.append({"id": id, "name": name})
        # tab[id] = name

    print(tab)

    return {"list": tab}


def listPatients():
    ''' listPatients
    Retourne la liste de tous les patients
    Parameters : NULL
    JSON : {‘list’: [{‘patientId’: patientId, ‘SS’: ss, ‘week’: week, ‘condition’: condition} , …]}'''
    # Database : pareil que getMedicines?
    tab = []
    for id, status, week, room_id, drug in get_patient():
        tab.append({"id": id, "status": status, "week": week, "room_id": room_id, "drug": drug})

    print(tab)

    return {"list": tab}


def listRooms():
    ''' listRooms
    Retourne la liste de toutes les rooms
    Parameters : NULL
    JSON : {‘list’: [{‘room’: room, ‘node’: node, ‘path’: node}, ... ]}'''
    tab = []
    for id, name, path in get_room():
        tab.append({"id": id, "path": path, "name": name})

    print(tab)

    return {"list": tab}


def listRobots():
    ''' listRobots
    Retourne la liste de tous les robots
    Parameters : NULL
    JSON : {‘list’: [{‘robot’: robot, ‘name’: name}, ... ]}'''
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    print(tab)

    return {"robots": tab}


def listOrders():
    ''' Commentaire'''
    tab = []
    for id, room, drug, status, timestamp in get_orders():
        print("get_orders : ", id, room, drug, status, timestamp)
        tab.append({"id": id, "room": room, "drug": drug, "status": status, "timestamp": timestamp})

    print(tab)

    return {"list": tab}


# Gestion des robots et des consignes


# Ajouter la consigne medicament pour telle room (status "to do")
def addOrder(room: int):
    ''' addOrder'''
    code = 200
    if isinstance(room, (int, float)) == False:
        code = 404
        error = "La room doit être un int"
    else:
        add_order(room)
    return {"code": code}


# Lit la première consigne disponible
def getOrder():
    ''' getOrder'''
    order = {}
    order = get_order()
    return {"order": order[0], "room": order[1], "medicine": order[2]}


# TEST ORDER
def OrderTest():
    ''' OrderTest'''
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    print(tab)

    return {"robots": tab}


# Retourne le statut d'avancement de la consigne
def checkOrder(order: str, status=""):
    ''' checkOrder'''
    # Questionner la database
    return {"order": order, "status": status}


# Modifie le statut d'une consigne entre 'delivered'(medicament fourni) et 'done' (robot a la base) avec le timestamp
def setOrder(order: str, status: str):
    ''' setOrdder'''
    set_order(order,status)
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


# Renvoie le noeud où le robot est actuellement
def getPosition(robot_id: int):
    ''' getPosition'''
    # Questionner la database
    return {"robot": robot_id, "node": node, "timestamp": timestamp}


# Enregistre le noeud où est arrivé le robot + timestamp
def setPosition(robot: int, node: int):
    ''' setPosition'''
    # Ajouter dans la database le nouveau noeud où se situe le robot
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


def getPath(room:int):
    ''' getPath
    Donne le chemin sous forme d'une chaîne de caractère
    Parameters : room
    JSON : {"room" : room,"path" : room}
    '''
    path = "RIEN"
    if (room == 1):
        path = "0F5F6L1L5R"
    elif (room == 2):
        path = "0F5F6F7L2L1F5R"
    elif (room == 3):
        path = "0F5F6F7R3R4F5L"
    elif (room == 4):
        path = "0F5F6R4R5L"
    else:
        path = "This room doesn't exist"
    # Donne le chemin sous forme d'une chaîne de caractère
    return {"room": room, "path": path}


# Historique des mouvements du robot
def getHistory(robot: int):
    ''' getHistory'''
    # Questionner la base de données
    return {"liste des mouvements node/timestamp": afaire}


# Crée une alerte (s'il est perdu)
def alertRobot(robot: int):
    ''' alertRobot'''
    # Ajouter base de données
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


def getObjectRoom():
    ''' getObjectRoom'''
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
