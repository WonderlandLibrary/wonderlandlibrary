/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import org.celestial.client.Celestial;
import org.celestial.client.files.FileManager;
import org.celestial.client.macro.Macro;
import org.lwjgl.input.Keyboard;

public class MacroConfig
extends FileManager.CustomFile {
    public MacroConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                if (Celestial.instance.macroManager == null) continue;
                Celestial.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
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
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Macro m : Celestial.instance.macroManager.getMacros()) {
                if (m == null) continue;
                out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

