# -*- coding: utf-8 -*-

from typing import Optional

import time
from datetime import datetime

from database import *


# Patients et essais cliniques

def setMedicine(medicine, name):
    ''' setMedicine
    Définit un nouveau médicament avec un nom
    Parameters : medicine/name
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré '''
    code = 404
    a = set_medicine(medicine, name)
    print("boolean",a)
    if isinstance(medicine, int) and isinstance(name, str) :
        if(set_medicine(medicine, name)) :
            code = 200
            return {"code": code}
        else :
            error = "la database fait n'imp"
            return {"code": code,"error":error}
    else:
        error = "Les arguments ne sont pas du bon type"
        return {"code":code,"error":error}
        


def addPatient(room: int, patientID: int, week: int = None):
    ''' addPatient
    Nouveau patient dans une room donnée (selon la semaine donnée ou en cours)
    Parameters : room/patientID/week
    JSON : {"code":200,"patientID":patientID} si enregistré
           {"code":404,"error":error} si non enregistré '''

    code = 200
    if isinstance(room, (int, float)) == False or isinstance(room, (int, float)) == False:
        code = 404
        error = "Pas bon type dans les arguments"
        return {"code": code,"error":error}
    else:
        add_patient(patientID, room, week)
        return {"code": code, "room": room, "patientID": patientID, "week": week}
    


def setRoom(room: int, name: str, path: str):
    ''' setRoom
    Crée ou modifie une chambre
    Parameters : room/name/(path)
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    code = 200
    if isinstance(room, (int, float)) == False:
        code = 404
        error = "Pas bon type dans les arguments"
        return {"code": code,"error":error}
    else:
        add_room(room, name, path)
        return {"code": code, "room": room, "patientID": patientID, "week": week}
    
    return 1


def getPatientCondition(patientID: int):
    ''' getPatientCondition
    Renvoie l'état de santé d'un patient entre "Cured"/"Stable"/"Dead"
    Parameters : patientID
    JSON : {"condition" : condition}
    non implémenté'''
    # Questionner la database
    return {"condition": condition}



def setPatientCondition(patient_id: int, condition: str):
    ''' setPatientCondition
    Définit la condition du patient entre cured/stable/dead
    Parameters : patientID,condition
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''

    code = 404
    if isinstance(patient_id, int) and isinstance(condition, str):
        if (set_patient_status(patient_id, condition)):
            code = 200
            return {"code":code}
    else :
        error = "Pas bon type argument"

    return {"code": code,"error":error}


def setRoomMedicine(room, medicine, week = None):
    ''' setRoomMedicine
    Définit le médicament à donner dans la room pour la semaine indiquée (ou semaine en cours si paramètre pas donné)
    Parameters : room/medicine/(week)
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
       
    code = 404
    if isinstance(room, int) and isinstance(medicine, int):
        if (set_room_medicine(room, medicine, week)):
            code = 200
            return {"code":code}
    else :
        error = "Pas bon type argument"

    return {"code": code,"error":error}


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
    non implémenté'''
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


    return {"list": tab}


def listPatients():
    ''' listPatients
    Retourne la liste de tous les patients
    Parameters : NULL
    JSON : {‘list’: [{‘patientId’: patientId, ‘SS’: ss, ‘week’: week, ‘condition’: condition} , …]}'''
    tab = []
    for id, status, week, room_id, drug in get_patient():
        tab.append({"id": id, "status": status, "week": week, "room_id": room_id, "drug": drug})

    #print(tab)

    return {"list": tab}


def listRooms():
    ''' listRooms
    Retourne la liste de toutes les rooms
    Parameters : NULL
    JSON : {‘list’: [{‘room’: room, ‘node’: node, ‘path’: node}, ... ]}'''
    tab = []
    for id, name, path in get_room():
        tab.append({"id": id, "path": path, "name": name})

    #print(tab)

    return {"list": tab}


def listRobots():
    ''' listRobots
    Retourne la liste de tous les robots
    Parameters : NULL
    JSON : {‘list’: [{‘robot’: robot, ‘name’: name}, ... ]}'''
    tab = {}
    for id, name in get_robot():
        tab[id] = name

    #print(tab)

    return {"robots": tab}


def listOrders():
    ''' listOrders
    Retourne la liste de tous les ordres
    Parameters : NULL
    Json : {'list': [{'robot': robot, 'name': name},...]}'''
    tab = []
    for id, room, drug, status, timestamp in get_orders():
        print("get_orders : ", id, room, drug, status, timestamp)
        tab.append({"id": id, "room": room, "drug": drug, "status": status, "timestamp": timestamp})

    #print(tab)

    return {"list": tab}


# Gestion des robots et des consignes


def addOrder(room: int):
    ''' addOrder
    Ajouter la consigne medicament pour telle room (status "to do")
    Parameters : room
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    code = 200
    if isinstance(room, (int, float)) == False:
        code = 404
        error = "La room doit être un int"
        return {"code":code,"error":error}
    else:
        add_order(room)
    return {"code": code}


def getOrder():
    ''' getOrder
    Lit la première consigne disponible avec le statut "to do"
    Parameters: NULL
    JSON : {"order": order, "room": room, "medicine" : medicine}
    (Si pas d'ordre l'erreur n'est pas géré) '''
    order = {}
    order = get_order()
    return {"order": order[0], "room": order[1], "medicine": order[2]}



def checkOrder(order: str, status=""):
    ''' checkOrder
    Retourne le statut d'avancement de la consigne
    Parameters : order/status
    JSON : {"order: order,"status: status}
    non implémenté'''
    # Questionner la database
    return {"order": order, "status": status}


def setOrder(order: str, status: str):
    ''' setOrder
    Modifie le statut d'une consigne entre 'delivered'(medicament fourni) et 'done' (robot a la base) avec le timestamp
    Parameters: order/status
    JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré'''
    set_order(order,status)
    #Le renvoi du code de cet erreur n'est pas géré dans cette version car nous gardons une version stable pour la démonstration
    if (True):
        code = 200
    else:
        code = 404
    return {"code": code}


def getPosition(robot_id: int):
    ''' getPosition
    Renvoie le noeud où le robot est actuellement
    Parameters: numéro du robot
    JSON : {"robot" : robot_id, "node": node, "timestamp": timestamp}
    non implémenté'''
    # Questionner la database
    return {"robot": robot_id, "node": node, "timestamp": timestamp}


def setPosition(robot: int, node: int):
    ''' setPosition
    Enregistre le noeud où est arrivé le robot + timestamp
    Parameters : robot/node
    JSON : JSON : {"code":200} si enregistré
           {"code":404,"error":error} si non enregistré
    non implémenté'''
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
    #Il est possible d'accéder au path grâce à la database mais pour cette version de démo
    #nous gardons l'envoi du chemin de cette manière
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


def getHistory(robot: int):
    ''' getHistory
    Historique des mouvements du robot
    Parameters : Robot
    JSON : {‘robot’: robot, ‘history’: [{’node’: node, ‘timestamp’: timestamp},...]},..}
    non implémenté'''
    # Questionner la database
    return {"liste des mouvements node/timestamp": todo}


def alertRobot(robot: int):
    ''' alertRobot
    Crée une alerte (s'il est perdu)
    Parameters : Robot
    JSON : JSON : JSON : {"code":200,"alert":alert} si enregistré
           {"code":404,"error":error} si non enregistré'''
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
