/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.ModManager;
import me.AveReborn.mod.mods.RENDER.BlockESP;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FileUtil {
    private Minecraft mc = Minecraft.getMinecraft();
    private String fileDir;

    public FileUtil() {
        this.fileDir = String.valueOf(String.valueOf(this.mc.mcDataDir.getAbsolutePath())) + "/" + Client.CLIENT_NAME;
        File fileFolder = new File(this.fileDir);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        try {
            this.loadKeys();
            this.loadValues();
            this.loadMods();
            this.loadBlocks();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void saveBlocks() {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/blocks.txt");
        try {
            if (!f2.exists()) {
                f2.createNewFile();
            }
            PrintWriter pw2 = new PrintWriter(f2);
            Iterator<Integer> iterator = BlockESP.getBlockIds().iterator();
            while (iterator.hasNext()) {
                int id2 = iterator.next();
                pw2.print(String.valueOf(String.valueOf(String.valueOf(id2))) + "\n");
            }
            pw2.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadBlocks() throws IOException {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/blocks.txt");
        if (!f2.exists()) {
            f2.createNewFile();
        } else {
            String line;
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            while ((line = br2.readLine()) != null) {
                try {
                    int id2 = Integer.valueOf(line);
                    BlockESP.getBlockIds().add(id2);
                }
                catch (Exception id2) {
                    // empty catch block
                }
            }
        }
    }

    public void saveKeys() {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/keys.txt");
        try {
            if (!f2.exists()) {
                f2.createNewFile();
            }
            PrintWriter pw2 = new PrintWriter(f2);
            for (Mod m2 : ModManager.getModList()) {
                String keyName = m2.getKey() < 0 ? "None" : Keyboard.getKeyName(m2.getKey());
                pw2.write(String.valueOf(String.valueOf(m2.getName())) + ":" + keyName + "\n");
            }
            pw2.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadKeys() throws IOException {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/keys.txt");
        if (!f2.exists()) {
            f2.createNewFile();
        } else {
            String line;
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            while ((line = br2.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                Mod m2 = ModManager.getModByName(split[0]);
                int key = Keyboard.getKeyIndex(split[1]);
                if (m2 == null || key == -1) continue;
                m2.setKey(key);
            }
        }
    }

    public void saveMods() {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/mods.txt");
        try {
            if (!f2.exists()) {
                f2.createNewFile();
            }
            PrintWriter pw2 = new PrintWriter(f2);
            for (Mod m2 : ModManager.getModList()) {
                pw2.print(String.valueOf(String.valueOf(m2.getName())) + ":" + m2.isEnabled() + "\n");
            }
            pw2.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadMods() throws IOException {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/mods.txt");
        if (!f2.exists()) {
            f2.createNewFile();
        } else {
            String line;
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            while ((line = br2.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                Mod m2 = ModManager.getModByName(split[0]);
                boolean state = Boolean.parseBoolean(split[1]);
                if (m2 == null) continue;
                m2.set(state, false);
            }
        }
    }

    public void saveValues() {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/values.txt");
        try {
            if (!f2.exists()) {
                f2.createNewFile();
            }
            PrintWriter pw2 = new PrintWriter(f2);
            for (Value value : Value.list) {
                String valueName = value.getValueName();
                if (value.isValueBoolean) {
                    pw2.print(String.valueOf(String.valueOf(valueName)) + ":b:" + value.getValueState() + "\n");
                    continue;
                }
                if (value.isValueDouble) {
                    pw2.print(String.valueOf(String.valueOf(valueName)) + ":d:" + value.getValueState() + "\n");
                    continue;
                }
                if (!value.isValueMode) continue;
                pw2.print(String.valueOf(String.valueOf(valueName)) + ":s:" + value.getModeTitle() + ":" + value.getCurrentMode() + "\n");
            }
            pw2.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void loadValues() throws IOException {
        File f2 = new File(String.valueOf(String.valueOf(this.fileDir)) + "/values.txt");
        if (!f2.exists()) {
            f2.createNewFile();
        } else {
            String line;
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            while ((line = br2.readLine()) != null) {
                if (!line.contains(":")) continue;
                String[] split = line.split(":");
                for (Value value : Value.list) {
                    if (!split[0].equalsIgnoreCase(value.getValueName())) continue;
                    if (value.isValueBoolean && split[1].equalsIgnoreCase("b")) {
                        value.setValueState(Boolean.parseBoolean(split[2]));
                        continue;
                    }
                    if (value.isValueDouble && split[1].equalsIgnoreCase("d")) {
                        value.setValueState(Double.parseDouble(split[2]));
                        continue;
                    }
                    if (!value.isValueMode || !split[1].equalsIgnoreCase("s") || !split[2].equalsIgnoreCase(value.getModeTitle())) continue;
                    value.setCurrentMode(Integer.parseInt(split[3]));
                }
            }
        }
    }
}

