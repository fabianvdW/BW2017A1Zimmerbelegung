import java.util.ArrayList;
import java.util.Arrays;

public class Person {

    // Attributes
    private String name; // own name
    private ArrayList<String> likes = new ArrayList<>(); // names of the people liked
    private ArrayList<String> dislikes = new ArrayList<>(); //names of the pople disliked

    // Constructor with 3 arguments (liked people, diksliked people, name)
    public Person(ArrayList<String> likes, ArrayList<String> dislike, String name) {
        setName(name);
        setLikes(likes);
        setDislikes(dislike);
    }

    // Getter
    public ArrayList<String> getLikes() {
        return likes;
    }

    public ArrayList<String> getDislikes() {
        return dislikes;
    }

    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
        this.likes.removeAll(Arrays.asList("", null));
    }

    public void setDislikes(ArrayList<String> dislikes) {
        this.dislikes = dislikes;
        this.dislikes.removeAll(Arrays.asList("", null));
    }
}
