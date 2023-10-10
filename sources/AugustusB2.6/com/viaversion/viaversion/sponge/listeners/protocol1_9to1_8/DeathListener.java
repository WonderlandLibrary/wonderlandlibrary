// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.listeners.protocol1_9to1_8;

import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import java.util.Optional;
import org.spongepowered.api.world.World;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.Listener;
import com.viaversion.viaversion.api.Via;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.SpongePlugin;
import com.viaversion.viaversion.sponge.listeners.ViaSpongeListener;

public class DeathListener extends ViaSpongeListener
{
    public DeathListener(final SpongePlugin plugin) {
        super(plugin, Protocol1_9To1_8.class);
    }
    
    @Listener(order = Order.LAST)
    public void onDeath(final DestructEntityEvent.Death e) {
        if (!(e.getTargetEntity() instanceof Player)) {
            return;
        }
        final Player p = (Player)e.getTargetEntity();
        if (this.isOnPipe(p.getUniqueId()) && Via.getConfig().isShowNewDeathMessages() && this.checkGamerule(p.getWorld())) {
            this.sendPacket(p, e.getMessage().toPlain());
        }
    }
    
    public boolean checkGamerule(final World w) {
        final Optional<String> gamerule = (Optional<String>)w.getGameRule("showDeathMessages");
        if (gamerule.isPresent()) {
            try {
                return Boolean.parseBoolean(gamerule.get());
            }
            catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    private void sendPacket(final Player p, final String msg) {
        Via.getPlatform().runSync(new Runnable() {
            @Override
            public void run() {
                final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.COMBAT_EVENT, null, ViaListener.this.getUserConnection(p.getUniqueId()));
                try {
                    final int entityId = ViaSpongeListener.this.getEntityId(p);
                    wrapper.write(Type.VAR_INT, 2);
                    wrapper.write(Type.VAR_INT, entityId);
                    wrapper.write(Type.INT, entityId);
                    Protocol1_9To1_8.FIX_JSON.write(wrapper, msg);
                    wrapper.scheduleSend(Protocol1_9To1_8.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
