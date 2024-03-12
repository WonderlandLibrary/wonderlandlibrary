// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.player;

import dev.tenacity.module.Category;
import net.minecraft.network.play.client.C03PacketPlayer;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.player.PushOutOfBlockEvent;
import dev.tenacity.event.impl.player.BoundingBoxEvent;
import dev.tenacity.module.Module;

public final class Freecam extends Module
{
    @Override
    public void onBoundingBoxEvent(final BoundingBoxEvent event) {
        if (Freecam.mc.thePlayer != null) {
            event.cancel();
        }
    }
    
    @Override
    public void onPushOutOfBlockEvent(final PushOutOfBlockEvent event) {
        if (Freecam.mc.thePlayer != null) {
            event.cancel();
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            event.cancel();
        }
    }
    
    @Override
    public void onEnable() {
        if (Freecam.mc.thePlayer != null) {
            Freecam.mc.thePlayer.capabilities.allowFlying = true;
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (Freecam.mc.thePlayer != null) {
            Freecam.mc.thePlayer.capabilities.allowFlying = false;
            Freecam.mc.thePlayer.capabilities.isFlying = false;
        }
        super.onDisable();
    }
    
    public Freecam() {
        super("Freecam", "Freecam", Category.PLAYER, "allows you to look around freely");
    }
}
