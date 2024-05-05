package ru.smertnix.celestial.files.impl;



import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.smertnix.celestial.files.FileManager;
import ru.smertnix.celestial.ui.altmanager.alt.Alt;
import ru.smertnix.celestial.ui.altmanager.alt.AltManager;
import ru.smertnix.celestial.ui.altmanager.alt.Alt.Status;

public class AltConfig extends FileManager.CustomFile {
    public AltConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] arguments = line.split(":");

                AltManager.registry.add(new Alt(arguments[0], arguments[1], "", Status.Unchecked, Boolean.parseBoolean(arguments[4]), arguments[3].replaceAll("_", ":")));
        }

        bufferedReader.close();
    }

    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));

        for (Alt alt : AltManager.registry) {
            if (alt.getMask().equals("")) {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getDate().replaceAll(":", "_") + ":" + alt.random);
            } else {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + alt.getDate().replaceAll(":", "_") + ":" + alt.random);
            }
        }
        alts.close();
    }
}