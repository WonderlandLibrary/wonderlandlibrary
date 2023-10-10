// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.listeners;

import net.md_5.bungee.event.EventHandler;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Collections;
import com.viaversion.viaversion.api.minecraft.metadata.MetaType;
import com.viaversion.viaversion.api.minecraft.metadata.Metadata;
import com.viaversion.viaversion.api.minecraft.metadata.types.MetaType1_9;
import com.viaversion.viaversion.api.type.types.version.Types1_9;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.EntityTracker1_9;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ElytraPatch implements Listener
{
    @EventHandler(priority = 32)
    public void onServerConnected(final ServerConnectedEvent event) {
        final UserConnection user = Via.getManager().getConnectionManager().getConnectedClient(event.getPlayer().getUniqueId());
        if (user == null) {
            return;
        }
        try {
            if (user.getProtocolInfo().getPipeline().contains(Protocol1_9To1_8.class)) {
                final EntityTracker1_9 tracker = user.getEntityTracker(Protocol1_9To1_8.class);
                final int entityId = tracker.getProvidedEntityId();
                final PacketWrapper wrapper = PacketWrapper.create(ClientboundPackets1_9.ENTITY_METADATA, null, user);
                wrapper.write(Type.VAR_INT, entityId);
                wrapper.write(Types1_9.METADATA_LIST, Collections.singletonList(new Metadata(0, MetaType1_9.Byte, 0)));
                wrapper.scheduleSend(Protocol1_9To1_8.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
