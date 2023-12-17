// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.files.impl;

import java.util.Iterator;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import ru.tuskevich.ui.altmanager.alt.Alt;
import ru.tuskevich.ui.altmanager.alt.AltManager;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import ru.tuskevich.files.FileManager;

public class AltConfig extends FileManager.CustomFile
{
    public AltConfig(final String name, final boolean loadOnStart) {
        super(name, loadOnStart);
    }
    
    @Override
    public void loadFile() throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.getFile()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            final String[] arguments = line.split(":");
            if (arguments.length > 2) {
                AltManager.registry.add(new Alt(arguments[0], arguments[1], arguments[2], (arguments.length > 3) ? Alt.Status.valueOf(arguments[3]) : Alt.Status.Unchecked));
            }
            else {
                AltManager.registry.add(new Alt(arguments[0], arguments[1]));
            }
        }
        bufferedReader.close();
    }
    
    @Override
    public void saveFile() throws IOException {
        final PrintWriter alts = new PrintWriter(new FileWriter(this.getFile()));
        for (final Alt alt : AltManager.registry) {
            if (alt.getMask().equals("")) {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getStatus());
            }
            else {
                alts.println(alt.getUsername() + ":" + alt.getPassword() + ":" + alt.getMask() + ":" + alt.getStatus());
            }
        }
        alts.close();
    }
}
