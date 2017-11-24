import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;

public class Distribution {

    public static void main(String[] args) {
        // check the parameter
        if (args.length < 1) {
            System.out.println("Zu wenige Parameter!");
            System.out.println("Benutzung: java -jar Distribution.jar <dateiname>");
            System.exit(1);
        } else if (args.length > 2) {
            System.out.println("Zu viele Parameter!");
            System.out.println("Benutzung: java -jar Distribution.jar <dateiname>");
            System.exit(1);
        }
        // get all persons
        ArrayList<Person> persons = addPersons(args[0]);
        // distribute the persons into rooms
        ArrayList<Room> rooms = distributePersons(persons);
        // calculate if this distribution is possible
        boolean possible = isPossible(rooms);
        if (possible) {
            System.out.println("Verteilung ist moeglich!");
            int numPersons = persons.size() + 1;
            int numRooms = rooms.size() + 1;
            System.out.println("Anzahl aller Personen: " + numPersons + "\n");
            System.out.println("Anzahl aller Zimmer: " + numRooms + "\n");
            for (Room z : rooms) {
                System.out.println(z.getInformation() + "\n");
            }
        } else {
            System.out.println("Verteilung ist nicht moeglich!");
        }
        // save the result
        writeIntoFile(args[0].substring(0, args[0].length() - 4) + "-loes.txt", rooms, possible);
    }

    // writes the result into a file
    public static void writeIntoFile(String path, ArrayList<Room> rooms, boolean distributionIsPossible) {
        try {
            // open a file to save the result into
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            if (distributionIsPossible) {
                for (Room z : rooms) {
                    // initializing a string which will contain all persons names in the current room
                    String s = "";
                    for (String p : z.getPersonNames()) {
                        // add the current person to the string
                        s += p + " ";
                    }
                    // add a new line and write into the file
                    bw.write(s + "\n");
                }
            } else {
                bw.write("Verteilung nicht moeglich!");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // checks whether all wishes are fulfilled
    public static boolean isPossible(ArrayList<Room> rooms) {
        if (rooms == null) {
            System.out.println("rooms is null");
            return false;
        }
        for (Room z : rooms) {
            for (Person p : z.getPersons()) {
                // check that all liked persons of a person are in the same room
                for (String s : p.getLikes()) {
                    if (!z.getPersonNames().contains(s)) {
                        return false;
                    }
                }
                // check that all disliked persons of a person are not in the room
                for (String s : p.getDislikes()) {
                    if (z.getPersonNames().contains(s)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // distributes the persons into rooms
    public static ArrayList<Room> distributePersons(ArrayList<Person> personen) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        A:
        for (Person person : personen) {
            // check if a rooms contains a liked person or the person is liked in a room
            // if so add the current person to that room
            for (Room z : rooms) {
                if (z.getLikes().contains(person.getName()) || !Collections.disjoint(z.getPersonNames(), person.getLikes())) {
                    z.addPerson(person);
                    continue A;
                }
            }
            // if no liked person is distibuted yet add this person into a single room
            Room z = new Room();
            z.addPerson(person);
            rooms.add(z);
        }
        // initialize a list for empty rooms
        ArrayList<Room> toRemove = new ArrayList<Room>();
        // Distribution of the not satisfied room (not all liked persons are in the room)
        B:
        for (Room zi : rooms) {
            for (Room zy : rooms) {
                if (zi.getPersons().size() > 0 && zy.getPersons().size() > 0 && zi != zy && !Collections.disjoint(zi.getLikes(), zy.getPersonNames()) && canBeMerged(zi, zy)) {
                    for (Person p : zy.getPersons()) {
                        zi.addPerson(p);
                    }
                    // clear the old room zy
                    zy.getPersons().clear();
                    zy.getPersonNames().clear();
                    zy.getLikes().clear();
                    zy.getDislikes().clear();
                    toRemove.add(zy);
                    continue B;
                }
            }
        }
        for (Room z : toRemove) {
            rooms.remove(z);
        }
        toRemove.clear();
        return rooms;
    }

    public static boolean canBeMerged(Room z1, Room z2) {
        // check if a disliked person of room z1 is in z2
        for (Person p : z1.getPersons()) {
            if (z2.getDislikes().contains(p.getName())) {
                return false;
            }
        }
        // check if a disliked person of room z2 is in z1
        for (Person p : z2.getPersons()) {
            if (z1.getDislikes().contains(p.getName())) {
                return false;
            }
        }
        return true;
    }

    // reads the file, processes, saves and returns the persons
    public static ArrayList<Person> addPersons(String filepath) {
        // read the data from the file
        ArrayList<String> data = readDataFromFile(filepath);
        ArrayList<Person> persons = new ArrayList<>();
        int numPersons = data.size();

        // loop through the file
        // 1. line = Name
        // 2. line = liked persons
        // 3. line = disliked persons
        // 4. line = blank
        for (int i = 0; i < numPersons; i += 4) {
            String name = data.get(i);
            // get the liked persons and remove the prefix (+ ; - )
            String raw_likes = data.get(i + 1).substring(1);
            String raw_dislikes = data.get(i + 2).substring(1);
            // remove the space if there's one
            if (raw_likes.length() > 0 && Character.toString(raw_likes.charAt(0)) == " ") {
                raw_likes = raw_likes.substring(1);
            }
            if (raw_dislikes.length() > 0 && Character.toString(raw_dislikes.charAt(0)) == " ") {
                raw_dislikes = raw_dislikes.substring(1);
            }
            // convert the liked persons to an arraylist
            ArrayList<String> likes;
            ArrayList<String> dislikes;
            if (raw_likes.length() > 0) {
                likes = new ArrayList<String>(Arrays.asList(raw_likes.split(" ")));
            } else {
                likes = new ArrayList<String>();
            }
            if (raw_dislikes.length() > 0) {
                dislikes = new ArrayList<String>(Arrays.asList(raw_dislikes.split(" ")));
            } else {
                dislikes = new ArrayList<String>();
            }
            // add the person
            persons.add(new Person(likes, dislikes, name));
        }
        return persons;
    }

    // reads the input file and returns an arraylist containing the lines
    public static ArrayList<String> readDataFromFile(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            ArrayList<String> data = new ArrayList<String>();
            String line = in.readLine();
            // remove the weird things at the start
            String letters = "qwertzuiopüasdfghjklöäyxcvbnmQWERTZUIOPÜASDFGHJKLÖÄYXCVBNM";
            while (!letters.contains(Character.toString(line.charAt(0)))) {
                line = line.substring(1);
            }
            while (line != null) {
                data.add(line);
                line = in.readLine();
            }
            in.close();
            return data;
        } catch (Exception e) {
            System.out.println("no such file");
            System.exit(0);
        }
        return null;
    }
}
