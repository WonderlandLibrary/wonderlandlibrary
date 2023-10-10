// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import org.bukkit.World;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import com.viaversion.viaversion.api.Via;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class DeathListener extends ViaBukkitListener
{
    public DeathListener(final Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (this.isOnPipe(p) && Via.getConfig().isShowNewDeathMessages() && this.checkGamerule(p.getWorld()) && e.getDeathMessage() != null) {
            this.sendPacket(p, e.getDeathMessage());
        }
    }
    
    public boolean checkGamerule(final World w) {
        try {
            return Boolean.parseBoolean(w.getGameRuleValue("showDeathMessages"));
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private void sendPacket(final Player p, final String msg) {
        Via.getPlatform().runSync(new Runnable() {
            @Override
            public void run() {
                final UserConnection userConnection = ViaBukkitListener.this.getUserConnection(p);
                if (userConnection != null) {
                    final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.COMBAT_EVENT, null, userConnection);
                    try {
                        wrapper.write(Type.VAR_INT, 2);
                        wrapper.write(Type.VAR_INT, p.getEntityId());
                        wrapper.write(Type.INT, p.getEntityId());
                        Protocol1_9To1_8.FIX_JSON.write(wrapper, msg);
                        wrapper.scheduleSend(Protocol1_9To1_8.class);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
