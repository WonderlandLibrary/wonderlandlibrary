/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.altManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.ui.altManager.GuiAltSlot;
import net.minecraft.client.Minecraft;

public class AltManager {
    public static ArrayList<String> altList = new ArrayList();
    public static ArrayList<GuiAltSlot> guiSlotList = new ArrayList();
    public static final File altFile = new File(Minecraft.getMinecraft().mcDataDir + "/" + Client.CLIENT_NAME + "/alts.txt");

    public static void saveAltsToFile() {
        try {
            PrintWriter writer = new PrintWriter(altFile);
            for (GuiAltSlot slot : guiSlotList) {
                writer.write(String.valueOf(String.valueOf(slot.getUsername())) + ":" + slot.getPassword() + "\n");
            }
            writer.close();
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void loadAltsFromFile() {
        guiSlotList.clear();
        try {
            if (!altFile.exists()) {
                altFile.createNewFile();
            } else {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(altFile));
                while ((line = bufferedReader.readLine()) != null) {
                    String[] alt2 = line.split(":");
                    if (alt2.length < 2) continue;
                    String username = alt2[0];
                    String password = alt2[1];
                    guiSlotList.add(new GuiAltSlot(username, password));
                }
                bufferedReader.close();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

