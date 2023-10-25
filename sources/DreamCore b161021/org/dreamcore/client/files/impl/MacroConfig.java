package org.dreamcore.client.files.impl;

import org.lwjgl.input.Keyboard;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.files.FileManager;
import org.dreamcore.client.macro.Macro;

import java.io.*;

public class MacroConfig extends FileManager.CustomFile {

    public MacroConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    public void loadFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream(this.getFile().getAbsolutePath());
            DataInputStream in = new DataInputStream(fileInputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String bind = curLine.split(":")[0];
                String value = curLine.split(":")[1];
                if (dreamcore.instance.macroManager != null) {
                    dreamcore.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value));
                }
            }
            br.close();
        } catch (Exception e) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Macro m : dreamcore.instance.macroManager.getMacros()) {
                if (m != null) {
                    out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue());
                    out.write("\r\n");
                }
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
