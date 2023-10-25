/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventReceivePacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity
extends Module {
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Velocity Mode").getValString();
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("Horizontal").getValDouble();
        double d2 = Exodus.INSTANCE.settingsManager.getSettingByName("Vertical").getValDouble();
        if (string.equalsIgnoreCase("Packet")) {
            this.setDisplayName("Velocity \ufffdf" + string);
        }
        if (string.equalsIgnoreCase("Reduce")) {
            this.setDisplayName("Velocity \ufffdf" + (int)d + "%" + " " + (int)d2 + "%");
        }
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Packet");
        arrayList.add("Reduce");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Velocity Mode", (Module)this, "Packet", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Horizontal", this, 0.0, 0.0, 100.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Vertical", this, 0.0, 0.0, 100.0, true));
    }

    public Velocity() {
        super("Velocity", 0, Category.COMBAT, "Prevents you from taking knockback.");
    }

    @EventTarget
    public void onPacket(EventReceivePacket eventReceivePacket) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Velocity Mode").getValString();
        if (string.equalsIgnoreCase("Packet")) {
            if (eventReceivePacket.getPacket() instanceof S12PacketEntityVelocity) {
                eventReceivePacket.setCancelled(true);
            }
            if (eventReceivePacket.getPacket() instanceof S27PacketExplosion) {
                eventReceivePacket.setCancelled(true);
            }
            if (string.equalsIgnoreCase("Reduce") && eventReceivePacket.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity)eventReceivePacket.getPacket();
                if (s12PacketEntityVelocity.entityID != Minecraft.thePlayer.getEntityId()) {
                    return;
                }
                eventReceivePacket.setCancelled(true);
                double d = (double)s12PacketEntityVelocity.getMotionX() / 8000.0;
                double d2 = (double)s12PacketEntityVelocity.getMotionY() / 8000.0;
                double d3 = (double)s12PacketEntityVelocity.getMotionZ() / 8000.0;
                double d4 = Exodus.INSTANCE.settingsManager.getSettingByName("Horizontal").getValDouble() / 100.0;
                double d5 = Exodus.INSTANCE.settingsManager.getSettingByName("Vertical").getValDouble() / 100.0;
                d *= d4;
                d2 *= d5;
                d3 *= d4;
                if (Exodus.INSTANCE.settingsManager.getSettingByName("Horizontal").getValDouble() == 0.0 && Exodus.INSTANCE.settingsManager.getSettingByName("Vertical").getValDouble() == 0.0) {
                    return;
                }
                Minecraft.thePlayer.motionX = d;
                Minecraft.thePlayer.motionY = d2;
                Minecraft.thePlayer.motionZ = d3;
            }
        }
    }
}

