import java.util.ArrayList;

public class Zimmer {
    ArrayList<Person> personen= new ArrayList<Person>();
    ArrayList<String> nogos= new ArrayList<String>();
    ArrayList<String> likes= new ArrayList<String>();

    @Override
    public String toString(){
        String s="";
        for(Person p: personen){
            s+=p.getName()+" ";
        }
        s+="\nLikes: ";
        for(String sx: likes){
            s+=sx+" ";
        }
        s+="\nDislikes: ";
        for(String sx: nogos){
            s+=sx+" ";
        }
        s+="\n\n-----------------------------------------------------------------------------";
        return s;
    }
    public boolean getState(){
        for(String s: nogos){
            if(likes.contains(s)){
                return false;
            }
        }
        for(Person p: personen){
            String s=p.getName();
            if(nogos.contains(s)){
                return false;
            }
        }
        return true;
    }
}
