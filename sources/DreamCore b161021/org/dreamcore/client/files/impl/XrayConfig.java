package org.dreamcore.client.files.impl;

import net.minecraft.block.Block;
import org.dreamcore.client.cmd.impl.XrayCommand;
import org.dreamcore.client.files.FileManager;

import java.io.*;

public class XrayConfig extends FileManager.CustomFile {

    public XrayConfig(String name, boolean loadOnStart) {
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
                String id = curLine.split(":")[1];
                XrayCommand.blockIDS.add(new Integer(id));
            }
            br.close();
        } catch (Exception ignored) {

        }
    }

    public void saveFile() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.getFile()));
            for (Integer integer : XrayCommand.blockIDS) {
                if (integer != null) {
                    out.write("blockID" + ":" + integer + ":" + Block.getBlockById(integer).getLocalizedName());
                    out.write("\r\n");
                }
            }
            out.close();
        } catch (Exception ignored) {

        }
    }
}
