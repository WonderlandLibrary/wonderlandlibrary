/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class Animations
extends Module {
    public Animations() {
        super("Animations", 0, Category.RENDER, "");
        this.toggle();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Block Animation").getValString();
        this.setDisplayName("Animations \ufffdf" + string);
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("1.7");
        arrayList.add("Test1");
        arrayList.add("Test2");
        arrayList.add("Exhibition");
        arrayList.add("Push");
        arrayList.add("Exodus");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Block Animation", (Module)this, "Exodus", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Descale", this, true));
    }

    @Override
    public void onDisable() {
        this.toggle();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

