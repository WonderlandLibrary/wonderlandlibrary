/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AntiDesync
extends Module {
    private int lastSlot = -1;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (eventUpdate.isPre() && this.lastSlot != -1) {
            if (this.lastSlot != Minecraft.thePlayer.inventory.currentItem) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
            }
        }
    }

    public AntiDesync() {
        super("AntiDesync", 0, Category.PLAYER, "");
    }

    @EventTarget
    public void onPacket(EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof C09PacketHeldItemChange) {
            C09PacketHeldItemChange c09PacketHeldItemChange = (C09PacketHeldItemChange)eventPacket.getPacket();
            this.lastSlot = c09PacketHeldItemChange.getSlotId();
        }
    }
}

