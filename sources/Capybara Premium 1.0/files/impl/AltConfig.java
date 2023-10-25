package fun.rich.client.files.impl;



import fun.rich.client.files.FileManager;
import fun.rich.client.ui.altmanager.alt.Alt;
import fun.rich.client.ui.altmanager.alt.AltManager;

import java.io.*;

public class AltConfig extends FileManager.CustomFile {
    public AltConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arguments = line.split(":");

            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2], arguments.length > 3 ? Alt.Status.valueOf(arguments[3]) : Alt.Status.Unchecked));
            } else {
                AltManager.registry.add(new Alt(arguments[0], arguments[1]));
            }
        }

        bufferedReader.close();
    }

    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));

        for (Alt alt : AltManager.registry) {
            if (alt.getMask().equals("")) {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getStatus());
            } else {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + alt.getStatus());
            }
        }
        alts.close();
    }
}