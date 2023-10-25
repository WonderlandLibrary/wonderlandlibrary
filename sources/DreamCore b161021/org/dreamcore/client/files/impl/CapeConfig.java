package org.dreamcore.client.files.impl;

import org.dreamcore.client.files.FileManager;
import org.dreamcore.client.ui.GuiCapeSelector;

import java.io.*;

public class CapeConfig extends FileManager.CustomFile {

    public CapeConfig(String name, boolean loadOnStart) {
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
                if (GuiCapeSelector.Selector.getCapeName() != null) {
                    GuiCapeSelector.Selector.setCapeName(curLine);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            out.write(GuiCapeSelector.Selector.getCapeName());
            out.write("\r\n");
            out.close();
        } catch (Exception ignored) {

        }
    }
}