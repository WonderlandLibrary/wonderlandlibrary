/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.files.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import org.celestial.client.files.FileManager;
import org.celestial.client.ui.GuiConfig;

public class LastConfig
extends FileManager.CustomFile {
    public static String configName;

    public LastConfig(String name, boolean loadOnStart) {
        super(name, loadOnStart);
    }

    @Override
    public void loadFile() {
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(this.getFile()));
            while ((line = br.readLine()) != null) {
                configName = line;
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
            if (GuiConfig.lastConfig != null && !GuiConfig.lastConfig.getName().isEmpty()) {
                String str = GuiConfig.lastConfig.getName();
                out.write(str);
                out.write("\r\n");
                out.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

