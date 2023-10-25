/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.files.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.celestial.client.files.FileManager;
import org.celestial.client.ui.components.altmanager.alt.Alt;
import org.celestial.client.ui.components.altmanager.alt.AltManager;

public class AltConfig
extends FileManager.CustomFile {
    public AltConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() throws IOException {
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
        while ((line = bufferedReader.readLine()) != null) {
            String[] arguments = line.split(":");
            for (int i = 0; i < 2; ++i) {
                arguments[i].replace(" ", "");
            }
            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2], arguments.length > 3 ? Alt.Status.valueOf(arguments[3]) : Alt.Status.Unchecked));
                continue;
            }
            AltManager.registry.add(new Alt(arguments[0], arguments[1]));
        }
        bufferedReader.close();
    }

    @Override
    public void saveFile() throws IOException {
        PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
        for (Object o : AltManager.registry) {
            Alt alt = (Alt)o;
            if (alt.getMask().equals("")) {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + (Object)((Object)alt.getStatus()));
                continue;
            }
            alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + (Object)((Object)alt.getStatus()));
        }
        alts.close();
    }
}

