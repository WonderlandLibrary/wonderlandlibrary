/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class HeadRotations
extends Module {
    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Jittery");
        arrayList.add("Smooth");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Head Rotations Mode", (Module)this, "Smooth", arrayList));
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        Minecraft.thePlayer.rotationYawHead = EventMotion.getYaw();
        Minecraft.thePlayer.renderYawOffset = EventMotion.getYaw();
        Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public HeadRotations() {
        super("Head Rotations", 0, Category.RENDER, "Grabs the server pitch and applies it to the event pitch.");
    }
}

