import java.lang.reflect.Array;
import java.util.ArrayList;

public class Person {
    private ArrayList<String> like = new ArrayList<>();
    private ArrayList<String> dislike = new ArrayList<>();
    private String name;

    public Person(String name){
        this.name = name;
        this.dislike = null;
        this.like = null;
    }

    public Person(ArrayList<String> like, ArrayList<String> dislike, String name) {
        this.like = like;
        this.dislike = dislike;
        this.name = name;
    }

    public ArrayList<String> getLike() {
        return like;
    }

    public ArrayList<String> getDislike() {
        return dislike;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String s = "";
        s+= name + " ";
        return s;
    }
}
