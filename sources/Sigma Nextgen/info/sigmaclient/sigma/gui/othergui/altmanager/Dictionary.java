package info.sigmaclient.sigma.gui.othergui.altmanager;

import java.util.Random;

public class Dictionary {
    static String[] strings = new String[]{
            "Corneille","Nevan","Brynjar","Varali","Keziah","Bohemond","Finley","Aylward","Wavery","Emmly","Dalya","Ole","Villette","Carina","Dracon","Meda","Elisa","Kimora","Birdella","Agot","Keyanna",
            "Warrick","Qismat","Aoko","Berta","Gabouray","Jain","Kanak","Remige","Suletu","Douglas","Hubbard","Maika","Raphael","Celtic","Marius","Evonnia","Molina","Bryce","Marcus","Halina","Solada",
            "Daruka","Keiran","Ginny","Lumina","Margie","Reiko","Toru","Kelley","Jennelle","Jaala","Carpenter","Naldo","Tiberio","Darius","Emiliana","Jaspin","Cherise","Jayda","Rhaxma","Elana","Adalyn",
            "Kramer","Neaira","Carlson","Illinois","Orah","Thuan","Cadwalader","Alienor","Jorden","Balwina","Sayeko","Kamdyn","Burford","Melusine","Charae","Eston","Pahuac","Castiel","Garima","Haunani","Katinka",
            "Java","Kasi","Rogene","Kahale","Dacey","Bethwyn","Alizeh","Andeana","Isondo","Kalista","Kasim","Salim","Izabelle","Ignace","Tamaki","Teshi","Vecepia","Chinara","Lonnie","Rayne","Maho",
            "Hart","Orwa","Taraji","Celia","Germaine","Canice","Meris","Arnaud","Cano","Ayume","Snow","Decima","Evans","Moeshe","Riggs","Isandro","Blush","Bran","Fabiano","Sabella","Garden",
            "Hart","Orwa","Taraji","Celia","Germaine","Canice","Meris","Arnaud","Cano","Ayume","Snow","Decima","Evans","Moeshe","Riggs","Isandro","Blush","Bran","Fabiano","Sabella","Garden"
    };
    public static String autoGet(){
        strings = new String[]{
            "Corneille","Nevan","Brynjar","Varali","Keziah","Bohemond","Finley","Aylward","Wavery","Emmly","Dalya","Ole","Villette","Carina","Dracon","Meda","Elisa","Kimora","Birdella","Agot","Keyanna",
            "Warrick","Qismat","Aoko","Berta","Gabouray","Jain","Kanak","Remige","Suletu","Douglas","Hubbard","Maika","Raphael","Celtic","Marius","Evonnia","Molina","Bryce","Marcus","Halina","Solada",
            "Daruka","Keiran","Ginny","Lumina","Margie","Reiko","Toru","Kelley","Jennelle","Jaala","Carpenter","Naldo","Tiberio","Darius","Emiliana","Jaspin","Cherise","Jayda","Rhaxma","Elana","Adalyn",
            "Kramer","Neaira","Carlson","Illinois","Orah","Thuan","Cadwalader","Alienor","Jorden","Balwina","Sayeko","Kamdyn","Burford","Melusine","Charae","Eston","Pahuac","Castiel","Garima","Haunani","Katinka",
            "Java","Kasi","Rogene","Kahale","Dacey","Bethwyn","Alizeh","Andeana","Isondo","Kalista","Kasim","Salim","Izabelle","Ignace","Tamaki","Teshi","Vecepia","Chinara","Lonnie","Rayne","Maho",
            "Hart","Orwa","Taraji","Celia","Germaine","Canice","Meris","Arnaud","Cano","Ayume","Snow","Decima","Evans","Moeshe","Riggs","Isandro","Blush","Bran","Fabiano","Sabella","Garden",
            "Hart","Orwa","Taraji","Celia","Germaine","Canice","Meris","Arnaud","Cano","Ayume","Snow","Decima","Evans","Moeshe","Riggs","Isandro","Blush","Bran","Fabiano","Sabella","Garden"
        };
        Random random = new Random();
        StringBuilder t = new StringBuilder();
        t.append(strings[random.nextInt(strings.length)]);
        if(random.nextBoolean()){
            t = new StringBuilder(t.toString().toLowerCase());
        }
        if(t.toString().length() < 5){
            t.append("").append(strings[random.nextInt(strings.length)]);
        }
        if(t.toString().length() > 10){
            t = new StringBuilder(t.substring(0, 10));
        }
        for(int i = 0;i < random.nextInt(2) + 3;i++){
            t.append(random.nextInt(9));
        }
        return t.toString();
    }
}
