import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Verteilung {
    private static String path = "zimmerbelegung1.txt";

    public static void main(String[] args) {
        addPersonen();
        System.out.println(liesDatenVonFile(path).size());
    }

    public static void addPersonen(){
        ArrayList<String> namen = new ArrayList<>();
        ArrayList<Person> personen = new ArrayList<>();
        int anzPersonen = liesDatenVonFile(path).size();
        for (int i = 0; i < anzPersonen; i+=4) {
            namen.add(liesDatenVonFile(path).get(i));
            //System.out.println(namen);//DEBUG
        }
    }

    public static ArrayList<String> liesDatenVonFile(String path){
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            ArrayList<String> daten= new ArrayList<String>();
            String line = in.readLine();

            while(line  != null)
            {
                daten.add(line);
                //System.out.println(line);//DEBUG
                line=in.readLine();
            }
            in.close();
            return daten;
        }catch(Exception e){
            System.out.println("no such file");
            System.exit(0);
        }
        return null;
    }
}
