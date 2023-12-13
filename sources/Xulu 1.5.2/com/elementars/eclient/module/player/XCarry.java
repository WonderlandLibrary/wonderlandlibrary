// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import com.elementars.eclient.event.events.CloseInventoryEvent;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.play.client.CPacketCloseWindow;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class XCarry extends Module
{
    public XCarry() {
        super("XCarry", "Holds things in your crafting menu", 0, Category.PLAYER, true);
    }
    
    @EventTarget
    public void onPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketCloseWindow) {
            final CPacketCloseWindow packet = (CPacketCloseWindow)event.getPacket();
            event.setCancelled(packet.windowId == 0);
        }
    }
    
    @EventTarget
    public void onClose(final CloseInventoryEvent event) {
        event.setCancelled(true);
    }
}
