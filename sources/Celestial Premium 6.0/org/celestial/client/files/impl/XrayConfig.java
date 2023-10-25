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
import net.minecraft.block.Block;
import org.celestial.client.cmd.impl.XrayCommand;
import org.celestial.client.files.FileManager;

public class XrayConfig
extends FileManager.CustomFile {
    public XrayConfig(String name, boolean loadOnStart) {
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
                String id = curLine.split(":")[1];
                XrayCommand.blockIDS.add(new Integer(id));
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
            for (Integer integer : XrayCommand.blockIDS) {
                if (integer == null) continue;
                out.write("blockID:" + integer + ":" + Block.getBlockById(integer).getLocalizedName());
                out.write("\r\n");
            }
            out.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

