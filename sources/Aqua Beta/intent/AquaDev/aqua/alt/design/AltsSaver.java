package intent.AquaDev.aqua.alt.design;

import intent.AquaDev.aqua.alt.design.AltManager;
import intent.AquaDev.aqua.alt.design.AltTypes;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

public class AltsSaver {
    public static ArrayList<AltTypes> AltTypeList;
    public static final File altFile;

    public static void saveAltsToFile() {
        try {
            if (!altFile.exists()) {
                try {
                    altFile.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PrintWriter writer = new PrintWriter(altFile);
            for (AltTypes slot : AltTypeList) {
                AltManager.i += 40;
                writer.write(String.valueOf((Object)slot.getEmail()) + ":" + slot.getPassword() + "\n");
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void altsReader() {
        try {
            String line;
            if (!altFile.exists()) {
                System.out.println("Alt not found. Error 22");
            }
            BufferedReader reader = new BufferedReader((Reader)new FileReader(altFile));
            AltTypes altTypes = new AltTypes("", "");
            while ((line = reader.readLine()) != null) {
                String[] arguments = line.split(":");
                altTypes.setPassword(arguments[0]);
                altTypes.setEmail(arguments[1]);
                AltTypeList.add((Object)new AltTypes(arguments[0], arguments[1]));
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        altFile = null;
        AltTypeList = new ArrayList();
    }
}
