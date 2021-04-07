# CREER DATABASE
# -*- coding: utf-8 -*-

import sqlite3
from sqlite3 import Error
from pathlib import Path

databaseName = "ImeriSoin.db"


def connectBase():
    """ return a connector to the database
    """

    try:
        conn = sqlite3.connect(databaseName)
        return conn
    except:
        return False


def baseExists():
    """ check if a database exists with the appropriate name
            . we check if the file exists
            . AND we check if we can open it as a sqlite3 database
    """
    file = Path(databaseName)
    if file.exists() and connectBase():
        return True
    return False


def createBase():
    """ create the database with the tables room,patient
    """

    # create database
    executeSQL("""
    create table drug
    (
        id   INTEGER not null
            constraint drug_pk
                primary key,
        name text
    );
    """)

    executeSQL("""
        create unique index drug_id_uindex
        on drug (id);
    """)

    executeSQL("""
    create table room
    (
        id   INTEGER not null
            constraint room_pk
                primary key,
        name text    not null,
        path text    not null
    );
    """)

    executeSQL("""
    create unique index room_id_uindex
    on room (id);
    """)

    executeSQL("""
    create table drug_room
    (
        week    INTEGER default (strftime('%W%Y',datetime())),
        drug_id int     not null,
        room_id int     not null
    );
    """)

    executeSQL("""
        create unique index drug_room_week_room_id_uindex
        on drug_room (week, room_id);
    """)

    executeSQL("""
    create table patient
    (
        id      INTEGER not null
            constraint patient_pk
                primary key,
        status  text,
        week    INTEGER default (strftime('%W%Y', datetime())),
        room_id INTEGER
            references room
    );
    """)

    executeSQL("""
        create unique index patient_id_uindex
            on patient (id);
    """)

    executeSQL("""
        create unique index patient_room_id_week_uindex
    on patient (room_id, week);
    """)

    executeSQL("""
    create table orders
    (
        id        INTEGER
            constraint orders_pk
                primary key autoincrement,
        timestamp DATE default (datetime('now', 'localtime')),
        room_id   INTEGER not null
            references room,
        status    text    not null
    );
    """)

    executeSQL("""
        create unique index ordes_id_uindex
        on orders (id);
    """)


def executeSQL(s_SQL):
    with connectBase() as conn:
        c = conn.cursor()

        c.execute(s_SQL)

        conn.commit()


def findSQL(s_SQL):
    with connectBase() as conn:
        c = conn.cursor()
        c.execute(s_SQL)

        conn.commit()
        return c


def insert_base():
    add_room(1, "B1", "0F5F6F7L2L1F5R")
    add_room(2, "B2", "0F5F6L1L5R")
    add_room(3, "B3", "0F5F6F7R3R4F5L")
    add_room(4, "B4", "0F5F6R4R5L")


def add_room(id, name="", path=""):
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
        executeSQL(f"UPDATE room SET path = {path}, name = {name} WHERE id = {id};")
    else:
        executeSQL(f"INSERT INTO room (id, path, name) VALUES ({id}, '{path}', '{name}');")


# todo add getter room
def get_room():
    rows = findSQL("SELECT * FROM room;").fetchall()
    print(rows)

    for id, name, path in rows:
        yield id, name, path


def set_room_medicine(room: int, medicine: int, week: int = None):
    if not isinstance(room, int): return "id not correct"

    with connectBase() as conn:
        c = conn.cursor()

        if week is not None:
            c.execute(f"""
                INSERT INTO drug_room (room_id, drug_id, week) VALUES ({room}, {medicine}, {week})
            """)
        else:
            c.execute(f"""
                INSERT INTO drug_room (room_id, drug_id) VALUES ({room}, {medicine})
            """)


# Retourne tous les médicaments disponibles
def get_room_medicine(room: int, week: int = None):
    rows = None
    if week is not None:
        rows = findSQL(f"""
        SELECT room_id, week, drug_id
        FROM drug_room
        WHERE room_id = {room}
        AND week = {week};
        """).fetchall()
    else:
        rows = findSQL(f"""
        SELECT room_id, week, drug_id
        FROM drug_room
        WHERE room_id = {room}
        AND week = (strftime('%W%Y', datetime()));
        """).fetchall()

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
    rows = findSQL(f"""
        SELECT p.id, p.status, p.week, p.room_id, dr.drug_id || ' ' || d.name
        FROM patient p
            LEFT JOIN drug_room dr on dr.room_id = p.room_id and p.week = dr.week
            LEFT JOIN drug d on d.id = dr.drug_id;
         """)

    for id, status, week, room_id, drug in rows:
        print("patient ", id, status, week, room_id, drug)
        yield id, status, week, room_id, drug


def set_patient_status(patient_id: int, status: str):
    executeSQL(f'''
        UPDATE patient SET status = '{status}' WHERE id = {patient_id}
    ''')


def add_patient(id: int, room: int, week: int = None):
    print(room, id, week)
    is_exist = findSQL(f'''
            SELECT CASE WHEN EXISTS (
                SELECT * 
                FROM patient
                WHERE id = {id}
            )
            THEN CAST(1 AS BIT)
            ELSE CAST(0 AS BIT) END;
        ''').fetchone()[0]

    if not is_exist:
        if week is not None:
            executeSQL(f"""
                INSERT INTO patient (id, room_id, week) VALUES ({id}, {room}, {week})
            """)
        else:
            executeSQL(f"""
                INSERT INTO patient (id, room_id) VALUES ({id}, {room})
            """)

        return True

    return False


def get_robot():
    rows = findSQL("SELECT * FROM robot;")

    for id, name, status in rows:
        yield id, name, status


# todo add setter robot
def set_robot():
    pass


def get_orders():
    rows = findSQL(f"""
        SELECT o.id, o.room_id, r.name, dr.drug_id, d.name, o.status, o.timestamp
        FROM orders o
                 JOIN room r on r.id = o.room_id
                 JOIN drug_room dr ON r.id = dr.room_id
                 JOIN drug d ON dr.drug_id = d.id
        WHERE dr.week = (strftime('%W%Y', datetime()))
    """).fetchall()

    print("rows : ", rows)

    for id, room_id, room_name, drug_id, drug_name, status, timestamp in rows:
        room = str(room_id) + " " + room_name
        drug = str(drug_id) + " " + drug_name
        yield id, room, drug, status, timestamp


def get_order():
    order = findSQL(f'''
        SELECT o.id, o.room_id, dr.drug_id
        FROM orders o
                 JOIN drug_room dr on dr.week = (strftime('%W%Y', datetime()))
        WHERE o.room_id = 1
        ORDER BY timestamp
        LIMIT 1
    ''').fetchone()

    executeSQL(f"UPDATE orders SET status = 'in Progress' WHERE id = {order[0]}")

    return order


def add_order(room):
    # medicine_id not use because causes inconstancy in the database
    executeSQL(f'''
        INSERT INTO orders 
            (room_id, status) 
            VALUES ({room}, 'to do')
    ''')

    return "ok"


def set_order(order, status):
    executeSQL(f'''
        UPDATE orders SET status = {status} WHERE order_id = {order},
    ''')


def get_room_join():
    rows = findSQL(f'''
    SELECT r.id,
       r.path,
       r.name,
       p.id,
       p.status,
       d.id,
       d.name
    FROM room r
         LEFT JOIN patient p on r.patient_id = p.id
         LEFT JOIN drug d on r.drug_id = d.id;
    ''').fetchall()

    for room_id, room_path, room_name, patient_id, patient_status, drug_id, drug_name in rows:
        yield room_id, room_path, room_name, patient_id, patient_status, drug_id, drug_name


# help(createBase)
# Example d'appels
##if not baseExists(): createBase()
##conn = connectBase()
### et on peut utiliser ici le connecteur de la base
def __test__():
    print("..", add_patient(1, 1))

    print("..", set_medicine(1, "medicament 1"))
    print("..", set_medicine(2, "medicament 2"))

    print("..", set_room_medicine(1, 1))

    for id, name in get_medicine():
        print(id, name)

    add_order(1)
    print("..get Order", get_order())
    print("..get Room", get_room())


if __name__ == '__main__':
    if not baseExists():
        print("create_DB")
        createBase()
        insert_base()
        __test__()

    get_room_join()
    get_medicine()
