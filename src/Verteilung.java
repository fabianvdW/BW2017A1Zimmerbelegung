import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Verteilung {
    private static String path = "zimmerbelegung5.txt";
    private static boolean istMoeglich = true;

    public static void main(String[] args) {
        ArrayList<Person> personen = addPersonen();
        System.out.println(personen.size());
        for (Person p : personen) {
            System.out.println(p.toString());
        }
        ArrayList<Zimmer> zimmer = verteile(personen);
        System.out.println("");
        System.out.println("Verteilung ist moeglich: " + istMoeglich);
        System.out.println("");
        if (istMoeglich) {
            int persos=0;
            for(Zimmer z:zimmer){
                persos+=z.personen.size();
            }
            System.out.println(persos);
            System.out.println("IST MÖGLICH: " + istTrue(zimmer));
            for (Zimmer z : zimmer) {
                System.out.println(z.toString());
            }
        }
    }

    public static boolean istTrue(ArrayList<Zimmer> zimmer) {
        for (Zimmer z : zimmer) {
            for (Person p : z.personen) {
                for (String s : p.getLike()) {
                    if (!z.personen.contains(new Person(s))) {
                        System.out.println("False bei Zimmer: " + z.toString());
                        System.out.println("False bei Person(like): : " + p.toString());
                        return false;
                    }
                }
                for (String s : p.getDislike()) {
                    if (z.personen.contains(new Person(s))) {
                        System.out.println("False bei Zimmer: " + z.toString());
                        System.out.println("False bei Person(dislike): : " + p.toString());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<Zimmer> verteile(ArrayList<Person> personen) {
        ArrayList<Zimmer> zimmer = new ArrayList<Zimmer>();
        A:
        for (Person person : personen) {
            for (Zimmer z : zimmer) {
                if (z.likes.contains(person.getName())) {
                    z.personen.add(person);
                    z.likes.addAll(person.getLike());
                    z.nogos.addAll(person.getDislike());
                    if (!z.getState()) {
                        istMoeglich = false;
                        System.exit(100);
                        return null;
                    }
                    zimmer = mergeEinzelZimmer(zimmer);
                    continue A;
                }
            }
            for (String s : person.getLike()) {
                for (Zimmer z : zimmer) {
                    if (z.personen.contains(new Person(s))) {
                        z.personen.add(person);
                        z.likes.addAll(person.getLike());
                        z.nogos.addAll(person.getDislike());
                        if (!z.getState()) {
                            istMoeglich = false;
                            System.exit(100);
                            return null;
                        }
                        zimmer = mergeEinzelZimmer(zimmer);
                        continue A;
                    }
                }
            }
            Zimmer z = new Zimmer();
            z.personen.add(person);
            z.likes.addAll(person.getLike());
            z.nogos.addAll(person.getDislike());
            zimmer.add(z);
        }
        ArrayList<Zimmer> toRemove = new ArrayList<Zimmer>();
        for (Zimmer z : zimmer) {
            if (z.personen.size() == 0) {
                toRemove.add(z);
            }
        }

        //SWAP
        A:for (int i = 0; i <zimmer.size() ; i++) {
            for (int j = i+1; j < zimmer.size(); j++) {
                Zimmer zi = zimmer.get(i);
                if(zi.personen.size()==0){
                    continue A;
                }
                Zimmer zj = zimmer.get(j);
                if (zj.personen.size() > 0) {
                    if (kannZusammenGelegtWerden(zi, zj)) {
                        for (Person p : zj.personen) {
                            zi.personen.add(p);
                        }
                        zi.likes.addAll(zj.likes);
                        zi.nogos.addAll(zj.nogos);
                        zj.personen.clear();
                        toRemove.add(zj);
                    }
                }
            }
        }
        for(Zimmer z: toRemove){
            zimmer.remove(z);
        }
        toRemove.clear();

        for(Zimmer z: zimmer){
            if(z.personen.size()==1){
                System.out.println("alleine für immer: "+z.personen.get(0).getName());
                Person p= z.personen.get(0);
                for(Zimmer zx: zimmer){
                    if(zx.likes.contains(p.getName())){
                        zx.personen.add(p);
                        zx.likes.addAll(p.getLike());
                        zx.nogos.addAll(p.getDislike());
                        z.personen.clear();
                        if(!z.getState()){
                            istMoeglich=false;
                            System.exit(100);
                            return null;
                        }
                    }
                }
            }
        }

        for(Zimmer z: toRemove){
            zimmer.remove(z);
        }

        return zimmer;
    }

    public static ArrayList<Zimmer> mergeEinzelZimmer(ArrayList<Zimmer> zimmer) {
        ArrayList<Zimmer> toRemove = new ArrayList<Zimmer>();
        A: for (Zimmer z : zimmer) {
            if (z.personen.size() == 1) {
                Person p = z.personen.get(0);
                for (Zimmer zx : zimmer) {
                    if (zx.likes.contains(p.getName())) {
                        zx.personen.add(p);
                        zx.likes.addAll(p.getLike());
                        zx.nogos.addAll(p.getDislike());
                        z.personen.clear();
                        toRemove.add(z);
                        if (!z.getState()) {
                            istMoeglich = false;
                            System.exit(100);
                            return null;
                        }
                        continue A;
                    }
                }
                if (p.getLike().size() > 0) {
                    for (String s : p.getLike()) {
                        for (Zimmer zx : zimmer) {
                            if (zx.personen.contains(new Person(s))) {
                                zx.personen.add(p);
                                zx.likes.addAll(p.getLike());
                                zx.nogos.addAll(p.getDislike());
                                z.personen.clear();
                                toRemove.add(z);
                                if (!z.getState()) {
                                    istMoeglich = false;
                                    System.exit(100);
                                    return null;
                                }
                            }
                        }
                    }
                }

            }
        }
        for (Zimmer z : toRemove) {
            zimmer.remove(z);
        }
        return zimmer;

    }
    public static boolean kannZusammenGelegtWerden(Zimmer z1, Zimmer z2) {
        for (Person p : z1.personen) {
            if (z2.nogos.contains(p.getName())) {
                return false;
            }
        }
        for (Person p : z2.personen) {
            if (z1.nogos.contains(p.getName())) {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Person> addPersonen() {
        ArrayList<String> daten = liesDatenVonFile(path);
        ArrayList<Person> personen = new ArrayList<>();
        int anzPersonen = daten.size();
        for (int i = 0; i < anzPersonen; i += 4) {
            String name = daten.get(i);
            String likess = daten.get(i + 1);
            //System.out.println(likess);
            likess = likess.substring(2);
            ArrayList<String> likes = new ArrayList<String>();
            for (int k = 0; k < likess.length() - 1; ) {
                String likeName = "";
                char c = ' ';
                int startIndex = k;
                do {
                    if (startIndex == likess.length()) {
                        c = ' ';
                    } else {
                        c = likess.charAt(startIndex);
                        startIndex++;
                        if (c != ' ') {
                            likeName += "" + c;
                        }
                    }
                } while (c != ' ');
                k += likeName.length() + 1;
                likes.add(likeName);
            }
            String dislikess = daten.get(i + 2);
            dislikess = dislikess.substring(2);
            ArrayList<String> dislikes = new ArrayList<String>();
            for (int k = 0; k < dislikess.length(); ) {
                String likeName = "";
                char c = ' ';
                int startIndex = k;
                do {
                    if (startIndex == dislikess.length()) {
                        c = ' ';
                    } else {
                        c = dislikess.charAt(startIndex);
                        startIndex++;
                        if (c != ' ') {
                            likeName += "" + c;
                        }
                    }
                } while (c != ' ');
                k += likeName.length() + 1;
                dislikes.add(likeName);
            }
            personen.add(new Person(likes, dislikes, name));

        }
        //System.out.println(daten);//DEBUG
        return personen;
    }

    public static ArrayList<String> liesDatenVonFile(String path) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));

            ArrayList<String> daten = new ArrayList<String>();
            String line = in.readLine();
            line = line.substring(1);
            while (line != null) {
                daten.add(line);
                //System.out.println(line);//DEBUG
                line = in.readLine();
            }
            in.close();
            return daten;
        } catch (Exception e) {
            System.out.println("no such file");
            System.exit(0);
        }
        return null;
    }
}
