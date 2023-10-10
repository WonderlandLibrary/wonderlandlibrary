// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.platform;

import io.netty.buffer.ByteBuf;
import com.velocitypowered.api.proxy.Player;
import com.viaversion.viaversion.ViaAPIBase;

public class VelocityViaAPI extends ViaAPIBase<Player>
{
    @Override
    public int getPlayerVersion(final Player player) {
        return this.getPlayerVersion(player.getUniqueId());
    }
    
    @Override
    public void sendRawPacket(final Player player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
}
