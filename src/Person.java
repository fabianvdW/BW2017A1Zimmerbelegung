import java.util.ArrayList;

public class Person {
    private ArrayList<String> like = new ArrayList<>();
    private ArrayList<String> dislike = new ArrayList<>();
    private String name;
    public Person(String name){
        this.name=name;
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
    public boolean equals(Object o){
        if(o instanceof Person){
            Person p=(Person)o ;
            return p.getName().equals(this.name);
        }
        return false;
    }
    @Override
    public String toString() {
        String s = "";
        s+= name + " ";
        /*s+="Likes: ";
        for(String so: getLike()){
            s+=so+" ";
        }
        s+="Dislikes: ";
        for(String so: getDislike()){
            s+=so+" ";
        }*/
        return s;
    }
}
