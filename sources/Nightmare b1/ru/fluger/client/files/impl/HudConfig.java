// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.files.impl;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import ru.fluger.client.ui.components.draggable.component.DraggableComponent;
import ru.fluger.client.Fluger;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import ru.fluger.client.files.FileManager;

public class HudConfig extends FileManager.CustomFile
{
    public HudConfig(final String name, final boolean loadOnStart) {
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
                final String x = curLine.split(":")[1];
                final String y = curLine.split(":")[2];
                for (final DraggableComponent hudModule : Fluger.instance.draggableHUD.getComponents()) {
                    if (hudModule.getName().equals(curLine.split(":")[0])) {
                        hudModule.setX(Integer.parseInt(x));
                        hudModule.setY(Integer.parseInt(y));
                    }
                }
            }
            br.close();
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void saveFile() {
        try {
            final BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (final DraggableComponent draggableModule : Fluger.instance.draggableHUD.getComponents()) {
                out.write(draggableModule.getName() + ":" + draggableModule.getX() + ":" + draggableModule.getY());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception ex) {}
    }
}
