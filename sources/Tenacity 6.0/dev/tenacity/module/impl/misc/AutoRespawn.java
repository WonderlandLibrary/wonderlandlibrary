// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.module.Category;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C16PacketClientStatus;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.module.Module;

public final class AutoRespawn extends Module
{
    @Override
    public void onTickEvent(final TickEvent event) {
        if (AutoRespawn.mc.thePlayer != null && AutoRespawn.mc.thePlayer.isDead) {
            PacketUtils.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        }
    }
    
    public AutoRespawn() {
        super("AutoRespawn", "Auto Respawn", Category.MISC, "automatically respawn");
    }
}
