import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Room {

    //Attributes
    private ArrayList<Person> persons = new ArrayList<Person>(); // persons in the room
    private ArrayList<String> personNames = new ArrayList<String>();
    private ArrayList<String> likes = new ArrayList<String>(); // all liked persons from all people in the room
    private ArrayList<String> dislikes = new ArrayList<String>(); // all disliked persons from all people in the room


    //Getter and Setter
    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public ArrayList<String> getPersonNames() {
        return personNames;
    }

    public void setPersonNames(ArrayList<String> personNames) {
        this.personNames = personNames;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(ArrayList<String> dislikes) {
        this.dislikes = dislikes;
    }

    // Methods

    public void remove_repeated_items(ArrayList<String> al) {
        HashSet<String> hs = new HashSet<>();
        hs.addAll(al);
        al.clear();
        al.addAll(hs);
    }

    public void clean_arrays() {
        getPersons().removeAll(Arrays.asList("", null));
        getPersonNames().removeAll(Arrays.asList("", null));
        getLikes().removeAll(Arrays.asList("", null));
        getDislikes().removeAll(Arrays.asList("", null));
        remove_repeated_items(getLikes());
        remove_repeated_items(getDislikes());
    }

    // add a person to the room
    public void addPerson(Person p) {
        // add the person and its attributes to the attributes of the room
        getPersons().add(p);
        getPersonNames().add(p.getName());
        getLikes().addAll(p.getLikes());
        getDislikes().addAll(p.getDislikes());
        // clean the attributes
        clean_arrays();
    }

    // output the room with all people and their liked people and nogos as a String
    public String getInformation() {
        String s = "";
        for (String name : getPersonNames()) {
            s += name + " ";
        }
        return s;
    }

    // returns the state of the room
    // true: room is possible (no person in the room / no liked person is a nogo)
    // false: room is not possible
    public boolean getState() {
        // clean the attributes
        clean_arrays();
        // checking whether a liked person is a nogo
        for (String s : getDislikes()) {
            if (getLikes().contains(s)) {
                return false;
            }
        }
        // checking whether a person in the room is a nogo
        for (Person p : getPersons()) {
            String s = p.getName();
            if (getDislikes().contains(s)) {
                return false;
            }
        }
        return true;
    }
}
