/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventReceivePacket;
import me.Tengoku.Terror.event.events.EventSendPacket;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class FreeCam
extends Module {
    @EventTarget
    public void onReceivePacket(EventReceivePacket eventReceivePacket) {
        if (eventReceivePacket.getPacket() instanceof S0CPacketSpawnPlayer) {
            eventReceivePacket.setCancelled(true);
            System.out.println("cancelled");
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof S0CPacketSpawnPlayer) {
            eventSendPacket.setCancelled(true);
            System.out.println("cancelled");
        }
    }

    public FreeCam() {
        super("FreeCam", 0, Category.MISC, "");
    }
}

