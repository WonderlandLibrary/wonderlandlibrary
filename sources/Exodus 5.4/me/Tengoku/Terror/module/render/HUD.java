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

public class HUD
extends Module {
    @Override
    public void onDisable() {
        this.toggle();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Mode").getValString();
        String string2 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Color").getValString();
        this.setDisplayName("HUD \ufffdf" + string);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Exodus");
        arrayList.add("Skeet");
        ArrayList<String> arrayList2 = new ArrayList<String>();
        arrayList2.add("RGB");
        arrayList2.add("Astolfo");
        arrayList2.add("OldSaturn");
        arrayList2.add("Static");
        arrayList2.add("Wave");
        ArrayList<String> arrayList3 = new ArrayList<String>();
        arrayList3.add("Red");
        arrayList3.add("Blue");
        arrayList3.add("Aqua");
        arrayList3.add("Purple");
        arrayList3.add("White");
        arrayList3.add("Candy");
        ArrayList<String> arrayList4 = new ArrayList<String>();
        arrayList4.add("Left");
        arrayList4.add("Right");
        ArrayList<String> arrayList5 = new ArrayList<String>();
        arrayList5.add("Text");
        ArrayList<String> arrayList6 = new ArrayList<String>();
        arrayList6.add("Arial");
        arrayList6.add("Normal");
        ArrayList<String> arrayList7 = new ArrayList<String>();
        arrayList7.add("Right");
        arrayList7.add("Full");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("HUD Mode", (Module)this, "Exodus", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ArrayList Color", (Module)this, "OldSaturn", arrayList2));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Wave Color", (Module)this, "Red", arrayList3));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ArrayList Speed", this, 8.0, 1.0, 15.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ArrayList", (Module)this, "Left", arrayList4));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("HUD Font", (Module)this, "Normal", arrayList6));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Bar Mode", (Module)this, "Right", arrayList7));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Show Options", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Show Background", this, true));
    }

    public HUD() {
        super("HUD", 0, Category.RENDER, "The clients HUD.");
        this.toggle();
    }
}

