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
import org.celestial.client.ui.components.draggable.DraggableModule;

public class HudConfig
extends FileManager.CustomFile {
    public HudConfig(String name, boolean loadOnStart) {
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
                String x = curLine.split(":")[1];
                String y = curLine.split(":")[2];
                for (DraggableModule hudModule : Celestial.instance.draggableManager.getMods()) {
                    if (!hudModule.getName().equals(curLine.split(":")[0])) continue;
                    hudModule.drag.setXPosition(Integer.parseInt(x));
                    hudModule.drag.setYPosition(Integer.parseInt(y));
                }
            }
            br.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (DraggableModule draggableModule : Celestial.instance.draggableManager.getMods()) {
                out.write(draggableModule.getName() + ":" + draggableModule.drag.getXPosition() + ":" + draggableModule.drag.getYPosition());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

