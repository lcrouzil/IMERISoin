#CREER DATABASE
#-*- coding: utf-8 -*-

import sqlite3
from sqlite3 import Error
from pathlib import Path

databaseName = "ImeriSoin.db"


def connectBase():
    ''' return a connector to the database
    '''
    try:
        conn = sqlite3.connect(databaseName)
        return conn
    except:
        return False
    
def baseExists():
    ''' check if a database exists with the appropriate name
            . we check if the file exists
            . AND we check if we can open it as a sqlite3 database
    '''
    file = Path(databaseName)
    if file.exists () and connectBase():
        return True
    return False

def createBase():
    ''' create the database with the tables ROOM,PATIENT
        '''

    #création de la base 
    try:
        conn = sqlite3.connect(databaseName)
    except Error as e:
        quit

    c = conn.cursor()
    c.execute('''CREATE TABLE ROOM (
                        patient_id  real,
                        id          real,
                        path        text  )''')
    c.execute('''CREATE TABLE PATIENT (
                        id          real,
                        drug_id     real,
                        status      text,
                        name        text  )''')
    conn.commit()
    conn.close()

help(createBase)
# Example d'appels
##if not baseExists(): createBase()
##conn = connectBase()
### et on peut utiliser ici le connecteur de la base