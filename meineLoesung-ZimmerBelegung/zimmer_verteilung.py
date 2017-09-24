#!/usr/bin/env python2
# coding=utf8

# Importieren der sys-Bibliothek zum Bearbeiten der Parameter
import sys


def parse_input(input_file):
    """

    :param input_file: Aufgaben-Datei, die eingelesen werden soll
    :return: Liste mit den 'will-ich' bzw. 'will ich nicht'-Wuenschen
    """
    students = []
    with open(input_file, 'rb') as f:
        current_student = ''
        # Durchgehen jeder Zeile der Datei
        for line in f.readlines():
            if current_student == '' and line != '\n':
                # Hinzufuegen einer Person / eines Schuelers
                current_student = line.rstrip('\n')
                students += [[current_student]]
            elif line.startswith('+'):
                # Hinzufuegen der Wuensche zu der zuletzt gennanten Person
                other_students = line.split(' ')
                for i in range(len(other_students)):
                    other_students[i] = other_students[i].rstrip('\n')
                    if other_students[i] == '':
                        other_students.remove(other_students[i])
                # remove the +
                other_students.pop(0)
                students[len(students) - 1] += [other_students]
            elif line.startswith('-'):
                # Hinzufuegen der 'will-ich-nicht'-Wuensche zu der zuletzt genannten Person
                other_students = line.split(' ')
                for i in range(len(other_students)):
                    other_students[i] = other_students[i].rstrip('\n')
                    if other_students[i] == '':
                        other_students[i] = None
                # remove the -
                other_students.pop(0)
                if other_students == [None]:
                    other_students = []
                students[len(students) - 1] += [other_students]
                # Zuruecksetzten der bearbeiteten Person, damit eine neue eingelesen werden kann
                current_student = ''
    # Zurueckgeben der Liste der Personen mit deren Wuenschen
    return students


def generate_like_notlike_list(students_wishes):
    """

    :param students_wishes: Liste mit den Wuenschen der einzelnen Personen
    :return: Liste mit den Personen, die bei einer Person im Zimmer sein muessen oder nicht im Zimmer sein duerfen,
     um alle Wuensche zu erfuellen
    """
    output = []
    for i in range(len(students_wishes)):
        name = students_wishes[i][0]
        output += [[name, students_wishes[i][1] + [name], students_wishes[i][2]]]
        # check every student if he likes or not likes the current student
        for a in range(len(students_wishes)):
            # check for like or not like in other students wishes
            for b in range(2):
                if name in students_wishes[a][b + 1]:
                    # add to the current student list
                    output[i][b + 1] += [students_wishes[a][0]]
                    # also add students the temp student wants
                    if b == 0:
                        output[i][1] += students_wishes[a][1]
                    # remove doublings
                    output[i][b + 1] = sorted(set(output[i][b + 1]), key=output[i][b + 1].index)
    return output


def already_in_list(big_list1, list2):
    """

    :param big_list1: Liste, die andere Listen enthaellt
    :param list2: Liste
    :return: Ja/Nein, ob list2 ,egal in welcher Reihenfolge, nicht schon in big_list1 vorhanden ist
    """
    for list in big_list1:
        if len(list) == len(list2):
            all_items_are_in_list = True
            for item in list:
                if item not in list2:
                    all_items_are_in_list = False
            if all_items_are_in_list:
                return True
    return False


def compress_list(total_list):
    """

    :param total_list: Liste mit den 'muss' und 'darf-nicht'-Beziehungen
    :return: Liste mit entstandenen Gruppen
    """
    output = []
    for student in total_list:
        if not already_in_list(output, student[1]):
            output += [student[1]]
    return output


def is_member_in_two_lists(list1, list2):
    """

    :param list1: Liste mit Personen
    :param list2: Liste mit anderen Personen
    :return: Ja/Nein, ob eine Person in beiden Listen vorhanden ist
    """
    for member in list1:
        if member in list2:
            return True
    return False


# Ueberpruefen, ob eine Aufgaben Datei angegeben wurde
if len(sys.argv) < 2:
    print 'Syntax: zimmer_verteilung.py <eingabe_datei>'
    sys.exit(0)
# Die Wuensche der Personen einlesen
students_wishes = parse_input(sys.argv[1])
total_list = students_wishes
for i in range(2):
    total_list = generate_like_notlike_list(total_list)
for student in total_list:
    # Ueberpruefen, ob eine Person bei einer anderen Person sowohl im Zimmer sein muesste,
    #  als auch nicht in deren Zimmer sein darf
    if is_member_in_two_lists(student[1], student[2]):
        print "Die Wuensche sind nicht umsetzbar."
        sys.exit(0)
# Konvertieren der 'muss' und 'darf-nicht'-Beziehungen in Liste mit den Zimmerverteilungen
groups = compress_list(total_list)
# Ausgeben der Gruppen
for i in range(len(groups)):
    print str(i + 1) + ". Gruppe: " + str(groups[i])
