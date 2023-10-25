/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import java.io.File;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import net.minecraft.client.Minecraft;

public class ConfigRemove
extends Command {
    private File dir;
    private File dataFile;

    public ConfigRemove() {
        super("ConfigRemove", "Removes a config", "ConfigRemove", "configremove");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length > 0) {
            String string2 = stringArray[0];
            File file = new File(Minecraft.getMinecraft().mcDataDir, "Exodus");
            this.dataFile = new File(file, String.valueOf(string2) + ".txt");
            this.dataFile.delete();
            Exodus.addChatMessage("Removed config " + string2);
        }
    }
}

