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
    ''' create the database with the tables room,patient
        '''

    # création de la base
    try:
        conn = sqlite3.connect(databaseName)
    except Error as e:
        quit

    c = conn.cursor()

    c.execute('''create table drug
        (
            id   INTEGER not null
                constraint drug_pk
                    primary key,
            name text
        );
    ''')

    c.execute('''
        create unique index drug_int_uindex
            on drug (id);
    ''')

    c.execute('''
        create table patient
        (
            id     INTEGER not null
                constraint patient_pk
                    primary key,
            name   text    not null,
            status text
        );
    ''')

    c.execute('''
        create unique index patient_id_uindex
            on patient (id);
    ''')

    c.execute('''
        create table room
        (
            id         INTEGER not null
                constraint room_pk
                    primary key,
            patient_id INTEGER
                references patient,
            path       text,
            drug_id    INTEGER
                references drug
        );
    ''')

    c.execute('''
        create unique index room_id_uindex
            on room (id);
    ''')

    c.execute('''
        create unique index room_patient_id_uindex
            on room (patient_id);
    ''')

    conn.commit()
    conn.close()


def insert_base():
    add_room(1, "0F5F6F7L2L1F5R")
    add_room(2, "0F5F6L1L5R")
    add_room(3, "0F5F6F7R3R4F5L")
    add_room(4, "0F5F6R4R5L")


def add_room(id, path):
    if not isinstance(id, int): return "id not correct"
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            INSERT INTO room (id, path) VALUES ({id}, '{path}');
        ''')

def set_room_medicine(room : int, medicine : int, week : int):
    if not isinstance(id, int): return "id not correct"
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            INSERT INTO room (id, drug_id) VALUES ({id}, {medicine});
        ''')

# Retourne tous les médicaments disponibles
def get_room_medicine(room : int,week : int):
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM room WHERE id = {room};
        ''')

        rows = c.fetchall()

        for id, name in rows:
            print(id, name)
            yield id, name

def add_medicine(id, name):
    if not isinstance(id, int): return "id not correct"
    if not isinstance(name, str) or len(name) <= 0: return "name not correct"

    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM drug WHERE id = {id};
        ''')

        rows = c.fetchall()
        print(rows)

        if rows:
            c.execute(f'''
                UPDATE drug SET name = '{name}' WHERE id = {id};
            ''')
        else:
            c.execute(f'''
                INSERT INTO drug (id, name) VALUES ({id}, '{name}');
            ''')


def add_patient(id, name):
    if not isinstance(id, int): return "id not correct"
    if not isinstance(name, str) or len(name) <= 0: return "name not correct"
    # if not isinstance(status, str) or len(status) <= 0: return "name not correct"

    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM patient WHERE id = {id};
        ''')

        rows = c.fetchall()
        print(rows)

        if rows:
            c.execute(f'''
                UPDATE patient SET name = '{name}' WHERE id = {id};
            ''')
        else:
            c.execute(f'''
                INSERT INTO patient (id, name) VALUES ({id}, '{name}');
            ''')


# Retourne tous les médicaments disponibles
def get_medicine():
    with connectBase() as coon:
        c = coon.cursor()
        c.execute(f'''
            SELECT * FROM drug;
        ''')

        rows = c.fetchall()

        for id, name in rows:
            print(id, name)
            yield id, name


# Retourne tous les patients
def get_patient():
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM patient;
        ''')

        rows = c.fetchall()

        for id, name, status in rows:
            yield id, name, status

def get_room():
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM room;
        ''')

        rows = c.fetchall()

        for id, name, status in rows:
            yield id, name, status

def get_robot():
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            SELECT * FROM robot;
        ''')

        rows = c.fetchall()

        for id, name, status in rows:
            yield id, name, status

# help(createBase)
# Example d'appels
##if not baseExists(): createBase()
##conn = connectBase()
### et on peut utiliser ici le connecteur de la base


def __test__():
    print("..", add_medicine(1, "medicament 1"))
    print("..", add_medicine(2, "medicament 2"))
    print("..", add_medicine(1, "medicament 3"))
    print("..", add_medicine(2, "medicament 4"))

    for id, name in get_medicine():
        print(id, name)


if __name__ == '__main__':
    if not baseExists():
        print("create_DB")
        createBase()
        insert_base()
        __test__()
