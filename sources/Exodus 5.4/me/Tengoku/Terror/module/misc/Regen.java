/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class Regen
extends Module {
    public Regen() {
        super("Regen", 0, Category.MISC, "Regens you automatically.");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("Regen Packets").getValDouble();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Regen Mode").getValString();
        this.setDisplayName("Regen \ufffdf" + string);
        if (string.equalsIgnoreCase("Packet")) {
            if (Minecraft.thePlayer.hurtTime > 0) {
                this.packetRegen((int)d);
            }
        }
        if (string.equalsIgnoreCase("Potion")) {
            if (Minecraft.thePlayer.hurtTime > 0) {
                this.packetRegen((int)((double)((int)d) / 2.0 * (double)Minecraft.thePlayer.getActivePotionEffect(Potion.regeneration).getAmplifier()));
            }
        }
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Packet");
        arrayList.add("Potion");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Regen Mode", (Module)this, "Packet", arrayList));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Regen Packets", this, 1.0, 1.0, 1000.0, true));
    }

    private void packetRegen(int n) {
        if (Minecraft.thePlayer.onGround) {
            int n2 = 0;
            while (n2 < n) {
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer(true));
                ++n2;
            }
        }
    }
}

