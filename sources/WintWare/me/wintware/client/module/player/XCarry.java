/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.network.play.client.CPacketCloseWindow;

public class XCarry
extends Module {
    public XCarry() {
        super("XCarry", Category.Player);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            CPacketCloseWindow cPacketCloseWindow = (CPacketCloseWindow)event.getPacket();
            if (cPacketCloseWindow.windowId == 0) {
                event.setCancelled(true);
            }
        }
    }
}

