// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.files.impl;

import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import ru.fluger.client.cmd.macro.Macro;
import org.lwjgl.input.Keyboard;
import ru.fluger.client.Fluger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import ru.fluger.client.files.FileManager;

public class MacroConfig extends FileManager.CustomFile
{
    public MacroConfig(final String name, final boolean loadOnStart) {
        super(name, loadOnStart);
    }
    
    @Override
    public void loadFile() {
        try {
            final FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            final DataInputStream in = new DataInputStream(fileInputStream);
            final BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                final String curLine = line.trim();
                final String bind = curLine.split(":")[0];
                final String value = curLine.split(":")[1];
                if (Fluger.instance.macroManager != null) {
                    Fluger.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
                }
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveFile() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (final Macro m : Fluger.instance.macroManager.getMacros()) {
                if (m != null) {
                    out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                    out.write("\r\n");
                }
            }
            out.close();
        }
        catch (Exception ex) {}
    }
}
