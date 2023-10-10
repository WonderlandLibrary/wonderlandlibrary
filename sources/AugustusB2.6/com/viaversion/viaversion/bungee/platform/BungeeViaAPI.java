// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.platform;

import com.viaversion.viaversion.bungee.service.ProtocolDetectorService;
import net.md_5.bungee.api.config.ServerInfo;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import com.viaversion.viaversion.ViaAPIBase;

public class BungeeViaAPI extends ViaAPIBase<ProxiedPlayer>
{
    @Override
    public int getPlayerVersion(final ProxiedPlayer player) {
        return this.getPlayerVersion(player.getUniqueId());
    }
    
    @Override
    public void sendRawPacket(final ProxiedPlayer player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
    
    public void probeServer(final ServerInfo serverInfo) {
        ProtocolDetectorService.probeServer(serverInfo);
    }
}
