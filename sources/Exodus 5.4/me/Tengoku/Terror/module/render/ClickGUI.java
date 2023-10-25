/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class ClickGUI
extends Module {
    public ClickGUI() {
        super("ClickGUI", 54, Category.RENDER, "The Click GUI.");
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Clean");
        arrayList.add("Exodus");
        arrayList.add("IntelliJ");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Design", (Module)this, "Exodus", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Sound", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Sync", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Rainbow", this, false));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("GuiRed", this, 255.0, 0.0, 255.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("GuiGreen", this, 26.0, 0.0, 255.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("GuiBlue", this, 42.0, 0.0, 255.0, true));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Design").getValString();
        if (string.equalsIgnoreCase("Clean")) {
            mc.displayGuiScreen(Exodus.INSTANCE.clickGui);
        }
        if (string.equalsIgnoreCase("Default")) {
            mc.displayGuiScreen(Exodus.INSTANCE.clickguisecond);
        }
        if (string.equalsIgnoreCase("Panel")) {
            mc.displayGuiScreen(Exodus.INSTANCE.skeet);
        }
        if (string.equalsIgnoreCase("Exodus")) {
            mc.displayGuiScreen(Exodus.INSTANCE.LemonGUI);
        }
        if (string.equalsIgnoreCase("IntelliJ")) {
            mc.displayGuiScreen(Exodus.INSTANCE.clickGuiIntellij);
        }
        if (string.equalsIgnoreCase("NiceGUI")) {
            mc.displayGuiScreen(Exodus.INSTANCE.niceGUI);
        }
        if (string.equalsIgnoreCase("Skeet")) {
            mc.displayGuiScreen(Exodus.INSTANCE.skeet);
        }
        if (string.equalsIgnoreCase("Recode")) {
            mc.displayGuiScreen(Exodus.INSTANCE.recode);
        }
        this.toggle();
    }
}

