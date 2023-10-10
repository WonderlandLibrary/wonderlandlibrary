// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bukkit.listeners.protocol1_15to1_14_4;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Arrays;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.type.types.version.Types1_14;
import org.bukkit.potion.PotionEffectType;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.entity.Player;
import com.viaversion.viaversion.api.protocol.Protocol;
import org.bukkit.plugin.Plugin;
import com.viaversion.viaversion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;
import com.viaversion.viaversion.ViaVersionPlugin;
import com.viaversion.viaversion.bukkit.listeners.ViaBukkitListener;

public class EntityToggleGlideListener extends ViaBukkitListener
{
    private boolean swimmingMethodExists;
    
    public EntityToggleGlideListener(final ViaVersionPlugin plugin) {
        super((Plugin)plugin, Protocol1_15To1_14_4.class);
        try {
            Player.class.getMethod("isSwimming", (Class<?>[])new Class[0]);
            this.swimmingMethodExists = true;
        }
        catch (NoSuchMethodException ex) {}
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void entityToggleGlide(final EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (!this.isOnPipe(player)) {
            return;
        }
        if (event.isGliding() && event.isCancelled()) {
            final PacketWrapper packet = PacketWrapper.create(ClientboundPackets1_15.ENTITY_METADATA, null, this.getUserConnection(player));
            try {
                packet.write(Type.VAR_INT, player.getEntityId());
                byte bitmask = 0;
                if (player.getFireTicks() > 0) {
                    bitmask |= 0x1;
                }
                if (player.isSneaking()) {
                    bitmask |= 0x2;
                }
                if (player.isSprinting()) {
                    bitmask |= 0x8;
                }
                if (this.swimmingMethodExists && player.isSwimming()) {
                    bitmask |= 0x10;
                }
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    bitmask |= 0x20;
                }
                if (player.isGlowing()) {
                    bitmask |= 0x40;
                }
                packet.write(Types1_14.METADATA_LIST, Arrays.asList(new Metadata(0, Types1_14.META_TYPES.byteType, bitmask)));
                packet.scheduleSend(Protocol1_15To1_14_4.class);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
