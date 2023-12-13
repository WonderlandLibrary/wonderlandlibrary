/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.plugin.Plugin
 */
package com.viaversion.viaversion.bukkit.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class DeathListener
extends ViaBukkitListener {
    public DeathListener(Plugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e2) {
        Player p2 = e2.getEntity();
        if (this.isOnPipe(p2) && Via.getConfig().isShowNewDeathMessages() && this.checkGamerule(p2.getWorld()) && e2.getDeathMessage() != null) {
            this.sendPacket(p2, e2.getDeathMessage());
        }
    }

    public boolean checkGamerule(World w2) {
        try {
            return Boolean.parseBoolean(w2.getGameRuleValue("showDeathMessages"));
        }
        catch (Exception e2) {
            return false;
        }
    }

    private void sendPacket(final Player p2, final String msg) {
        Via.getPlatform().runSync(new Runnable(){

            @Override
            public void run() {
                UserConnection userConnection = DeathListener.this.getUserConnection(p2);
                if (userConnection != null) {
                    PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.COMBAT_EVENT, null, userConnection);
                    try {
                        wrapper.write(Type.VAR_INT, 2);
                        wrapper.write(Type.VAR_INT, p2.getEntityId());
                        wrapper.write(Type.INT, p2.getEntityId());
                        Protocol1_9To1_8.FIX_JSON.write(wrapper, msg);
                        wrapper.scheduleSend(Protocol1_9To1_8.class);
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
    }
}

