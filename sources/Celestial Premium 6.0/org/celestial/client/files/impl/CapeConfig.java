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
import org.celestial.client.files.FileManager;
import org.celestial.client.ui.GuiCapeSelector;

public class CapeConfig
extends FileManager.CustomFile {
    public CapeConfig(String name, boolean loadOnStart) {
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
                if (GuiCapeSelector.Selector.getCapeName() == null) continue;
                GuiCapeSelector.Selector.setCapeName(curLine);
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
            out.write(GuiCapeSelector.Selector.getCapeName());
            out.write("\r\n");
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

