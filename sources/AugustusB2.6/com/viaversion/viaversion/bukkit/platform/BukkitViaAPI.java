// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.platform;

import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.util.ProtocolSupportUtil;
import org.bukkit.Bukkit;
import com.viaversion.viaversion.api.Via;
import java.util.UUID;
import com.viaversion.viaversion.ViaVersionPlugin;
import org.bukkit.entity.Player;
import com.viaversion.viaversion.ViaAPIBase;

public class BukkitViaAPI extends ViaAPIBase<Player>
{
    private final ViaVersionPlugin plugin;
    
    public BukkitViaAPI(final ViaVersionPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public int getPlayerVersion(final Player player) {
        return this.getPlayerVersion(player.getUniqueId());
    }
    
    @Override
    public int getPlayerVersion(final UUID uuid) {
        final UserConnection connection = Via.getManager().getConnectionManager().getConnectedClient(uuid);
        if (connection != null) {
            return connection.getProtocolInfo().getProtocolVersion();
        }
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null && this.isProtocolSupport()) {
            return ProtocolSupportUtil.getProtocolVersion(player);
        }
        return -1;
    }
    
    @Override
    public void sendRawPacket(final Player player, final ByteBuf packet) throws IllegalArgumentException {
        this.sendRawPacket(player.getUniqueId(), packet);
    }
    
    public boolean isCompatSpigotBuild() {
        return this.plugin.isCompatSpigotBuild();
    }
    
    public boolean isProtocolSupport() {
        return this.plugin.isProtocolSupport();
    }
}
