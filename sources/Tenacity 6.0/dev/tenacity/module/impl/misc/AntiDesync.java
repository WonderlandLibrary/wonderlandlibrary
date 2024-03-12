// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public final class AntiDesync extends Module
{
    private int slot;
    
    public AntiDesync() {
        super("AntiDesync", "Anti Desync", Category.MISC, "pervents desync client side");
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (event.getPacket() instanceof C09PacketHeldItemChange) {
            this.slot = ((C09PacketHeldItemChange)event.getPacket()).getSlotId();
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (this.slot != AntiDesync.mc.thePlayer.inventory.currentItem && this.slot != -1) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(AntiDesync.mc.thePlayer.inventory.currentItem));
        }
    }
}
