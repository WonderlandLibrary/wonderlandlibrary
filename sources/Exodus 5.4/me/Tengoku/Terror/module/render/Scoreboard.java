/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class Scoreboard
extends Module {
    public static Setting hide;
    public static Setting hideNumbers;
    public static Setting height;
    public static Setting transparent;
    public static Setting drawShadow;

    @Override
    public void setup() {
        height = new Setting("Height", this, 70.0, 1.0, 138.0, false);
        Exodus.INSTANCE.settingsManager.addSetting(height);
        hide = new Setting("Hide", this, false);
        Exodus.INSTANCE.settingsManager.addSetting(hide);
        drawShadow = new Setting("Draw Shadow", this, false);
        Exodus.INSTANCE.settingsManager.addSetting(drawShadow);
        transparent = new Setting("Transparent", this, false);
        Exodus.INSTANCE.settingsManager.addSetting(transparent);
        hideNumbers = new Setting("Hide Numbers", this, false);
        Exodus.INSTANCE.settingsManager.addSetting(hideNumbers);
    }

    public Scoreboard() {
        super("Scoreboard", 0, Category.RENDER, "Change how the scoreboard looks.");
        this.toggle();
    }

    @Override
    public void onDisable() {
        this.toggle();
    }
}

