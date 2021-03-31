# CREER DATABASE
# -*- coding: utf-8 -*-

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
    if file.exists() and connectBase():
        return True
    return False


def createBase():
    ''' create the database with the tables ROOM,PATIENT
        '''

    # création de la base
    try:
        conn = sqlite3.connect(databaseName)
    except Error as e:
        quit

    c = conn.cursor()

    c.execute('''CREATE TABLE DRUG(
        id   INTEGER
            constraint DRUG_pk
                primary key autoincrement,
        name text
    )
    ''')

    c.execute('''
        create unique index DRUG_int_uindex
            on DRUG (id);
    ''')

    c.execute('''
        create unique index DRUG_name_uindex
            on DRUG (name);
    ''')

    c.execute('''create table PATIENT
        (
            id      INTEGER not null
                constraint PATIENT_pk
                    primary key,
            name    text    not null,
            status  text,
            drug_id INTEGER
                references DRUG
        );
    ''')

    c.execute('''
            create unique index PATIENT_id_uindex
                on PATIENT (id);
    ''')

    c.execute('''create table ROOM
            (
                id         INTEGER not null
                    constraint ROOM_pk
                        primary key,
                patient_id int
                    references PATIENT,
                path       text
            );
    ''')

    c.execute('''
        create unique index ROOM_id_uindex
            on ROOM (id);
    ''')

    c.execute('''
        create unique index ROOM_patient_id_uindex
           on ROOM (patient_id);
    ''')

    conn.commit()
    conn.close()


# help(createBase)
# Example d'appels
##if not baseExists(): createBase()
##conn = connectBase()
### et on peut utiliser ici le connecteur de la base


if __name__ == '__main__':
    if not baseExists():
        print("create_DB")
        createBase()
