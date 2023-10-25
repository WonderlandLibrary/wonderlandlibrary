/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import de.Hero.settings.Setting;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class ConfigLoad
extends Command {
    private File dir;
    private File dataFile;

    public ConfigLoad() {
        super("ConfigLoad", "Loads a config", "ConfigLoad", "configload");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length > 0) {
            String string2 = stringArray[0];
            Exodus.addChatMessage("Loaded config " + string2);
            this.dir = new File(Minecraft.getMinecraft().mcDataDir, "Exodus");
            if (!this.dir.exists()) {
                this.dir.mkdir();
            }
            this.dataFile = new File(this.dir, String.valueOf(string2) + ".txt");
            ArrayList<String> arrayList = new ArrayList<String>();
            BufferedReader object3 = new BufferedReader(new FileReader(this.dataFile));
            Object object = object3.readLine();
            while (object != null) {
                arrayList.add((String)object);
                object = object3.readLine();
            }
            try {
                object3.close();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            for (String string3 : arrayList) {
                Setting setting;
                Module module;
                String[] stringArray2 = string3.split(":");
                if (string3.toLowerCase().startsWith("mod:")) {
                    module = Exodus.INSTANCE.moduleManager.getModuleByName(stringArray2[1]);
                    if (module == null) continue;
                    module.setToggled(Boolean.parseBoolean(stringArray2[2]));
                    module.setKey(Integer.parseInt(stringArray2[3]));
                    continue;
                }
                if (!string3.toLowerCase().startsWith("set:") || (module = Exodus.INSTANCE.moduleManager.getModuleByName(stringArray2[2])) == null || (setting = Exodus.INSTANCE.settingsManager.getSettingByModule(stringArray2[1], module)) == null) continue;
                if (setting.isCheck()) {
                    setting.setValBoolean(Boolean.parseBoolean(stringArray2[3]));
                }
                if (setting.isCombo()) {
                    setting.setValString(stringArray2[3]);
                }
                if (!setting.isSlider()) continue;
                setting.setValDouble(Double.parseDouble(stringArray2[3]));
            }
        }
    }
}

