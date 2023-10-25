/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.skyblock;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AutoScam
extends Module {
    private boolean hasSwitched = false;
    Timer timer = new Timer();

    public AutoScam() {
        super("AutoScam", 0, Category.SKYBLOCK, "Allows you to automatically scam on skyblock.");
    }

    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("ItemSlot", this, 1.0, 1.0, 10.0, true));
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof S08PacketPlayerPosLook) {
            eventPacket.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.hasSwitched = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        double d = Exodus.INSTANCE.settingsManager.getSettingByModule("ItemSlot", this).getValDouble();
        if (Minecraft.thePlayer != null) {
            if (Minecraft.thePlayer.openContainer != null) {
                if (Minecraft.thePlayer.openContainer instanceof ContainerChest) {
                    ContainerChest containerChest = (ContainerChest)Minecraft.thePlayer.openContainer;
                    Container container = Minecraft.thePlayer.inventoryContainer;
                    if (!this.hasSwitched) {
                        this.hasSwitched = true;
                        Minecraft.playerController.windowClick(containerChest.windowId, 0, 0, 0, Minecraft.thePlayer);
                    } else {
                        Minecraft.playerController.windowClick(containerChest.windowId, 47, 0, 0, Minecraft.thePlayer);
                    }
                }
            }
        }
    }
}

