// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import net.minecraft.network.play.server.S3APacketTabComplete;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.network.play.client.C14PacketTabComplete;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public final class AntiTabComplete extends Module
{
    public AntiTabComplete() {
        super("AntiTabComplete", "Anti Tab Complee", Category.MISC, "prevents you from tab completing");
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (event.getPacket() instanceof C14PacketTabComplete) {
            event.cancel();
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S3APacketTabComplete) {
            event.cancel();
        }
    }
}
