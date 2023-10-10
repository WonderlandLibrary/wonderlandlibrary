// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.platform;

import io.netty.buffer.ByteBuf;
import org.spongepowered.api.entity.living.player.Player;
import com.viaversion.viaversion.ViaAPIBase;

public class SpongeViaAPI extends ViaAPIBase<Player>
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
