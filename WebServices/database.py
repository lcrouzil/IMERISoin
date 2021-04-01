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
            id INTEGER not null
                constraint room_pk
                    primary key,
            patient_id INTEGER
                references patient,
            drug_id    INTEGER
                references drug,
            path       TEXT,
            name       TEXT
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

    c.execute('''
        create table orders
        (
            id      INTEGER
                constraint order_pk
                    primary key autoincrement,
            room_id INTEGER not null
                references room,
            status  TEXT,
            date    DATETIME default CURRENT_TIMESTAMP
        );''')

    c.execute('''
        create unique index order_id_uindex
            on orders (id);
    ''')

    conn.commit()
    conn.close()


def executeSQL(s_SQL):
    with connectBase() as conn:
        c = conn.cursor()

        c.execute(s_SQL)


def findSQL(s_SQL):
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(s_SQL)

        return c


def insert_base():
    add_room(1, "0F5F6F7L2L1F5R")
    add_room(2, "0F5F6L1L5R")
    add_room(3, "0F5F6F7R3R4F5L")
    add_room(4, "0F5F6R4R5L")


def add_room(id, path):
    if not isinstance(id, int): return "id not correct"

    is_exist = findSQL(f'''
        SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM drug
            WHERE id = {id}
        )
        THEN CAST(1 AS BIT)
        ELSE CAST(0 AS BIT) END;
    ''').fetchone()[0]

    print(is_exist)

    if is_exist:
        executeSQL(f"UPDATE room SET path = {path} WHERE id = {id};")
    else:
        executeSQL(f"INSERT INTO room (id, path) VALUES ({id}, '{path}');")


# todo add getter room
def get_room():
    return findSQL("SELECT * FROM room").fetchall()


def set_room_medicine(room: int, medicine: int, week: int):
    if not isinstance(id, int): return "id not correct"
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(f'''
            INSERT INTO room (id, drug_id) VALUES ({id}, {medicine});
        ''')


# Retourne tous les médicaments disponibles
def get_room_medicine(room: int, week: int):
    rows = findSQL(f"SELECT * FROM room WHERE id = {room};").fetchall()

    for id, name in rows:
        print(id, name)
        yield id, name


def get_medicine():
    rows = findSQL("SELECT * FROM drug;").fetchall()

    print("rows : ", rows)

    for id, name in rows:
        yield id, name


def set_medicine(id, name):
    if not isinstance(id, int): return "id not correct"
    if not isinstance(name, str) or len(name) <= 0: return "name not correct"

    is_exist = findSQL(f'''
        SELECT CASE WHEN EXISTS (
            SELECT * 
            FROM drug
            WHERE id = {id}
        )
        THEN CAST(1 AS BIT)
        ELSE CAST(0 AS BIT) END;
    ''').fetchone()[0]

    print(is_exist)

    if is_exist:
        executeSQL(f"UPDATE drug SET name = '{name}' WHERE id = {id};")
    else:
        executeSQL(f"INSERT INTO drug (id, name) VALUES ({id}, '{name}');")


def get_patient():
    rows = findSQL("SELECT * FROM patient;")

    for id, name, status in rows:
        yield id, name, status


def add_patient(id, name):
    if not isinstance(id, int): return "id not correct"
    if not isinstance(name, str) or len(name) <= 0: return "name not correct"
    # if not isinstance(status, str) or len(status) <= 0: return "name not correct"

    is_exist = findSQL(f'''
            SELECT CASE WHEN EXISTS (
                SELECT *
                FROM patient
                WHERE id = {id}
            )
            THEN CAST(1 AS BIT)
            ELSE CAST(0 AS BIT) END;
        ''').fetchone()[0]

    print("rows :", is_exist)

    if is_exist:
        executeSQL(f'''
            UPDATE patient SET name = '{name}' WHERE id = {id};
        ''')
    else:
        executeSQL(f'''
            INSERT INTO patient (id, name) VALUES ({id}, '{name}');
        ''')


def get_robot():
    rows = findSQL("SELECT * FROM robot;")

    for id, name, status in rows:
        yield id, name, status


# todo add setter robot
def set_robot():
    pass


def get_order():
    order = findSQL(f'''SELECT o.id, o.room_id, r.drug_id
        FROM orders o
                 LEFT JOIN room r on r.id = o.room_id
        ORDER BY date
        LIMIT 1
    ''').fetchone()

    executeSQL(f"UPDATE orders SET status = 'in Progress' WHERE id = {order[0]}")

    return order


def add_order(room, medicine_id):
    # medicine_id not use because causes inconstancy in the database
    executeSQL(f'''
        INSERT INTO orders 
            (room_id, status) 
            VALUES ({room}, 'to do')
    ''')


def set_order(order, status):
    executeSQL(f'''
        UPDATE orders SET status = {status} WHERE order_id = {order},
    ''')


# help(createBase)
# Example d'appels
##if not baseExists(): createBase()
##conn = connectBase()
### et on peut utiliser ici le connecteur de la base
def __test__():
    print("..", set_medicine(1, "medicament 1"))
    print("..", set_medicine(2, "medicament 2"))
    print("..", set_medicine(1, "medicament 3"))
    print("..", set_medicine(2, "medicament 4"))

    print("..", add_patient(1, "Patient 4"))

    for id, name in get_medicine():
        print(id, name)

    add_order(1, 1)
    print("..get Order", get_order())
    print("..get Room", get_room())


if __name__ == '__main__':
    if not baseExists():
        print("create_DB")
        createBase()
        insert_base()
        __test__()


    get_medicine()
