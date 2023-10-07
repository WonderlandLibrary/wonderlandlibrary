// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.bukkit.tasks.protocol1_19to1_18_2;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.storage.SequenceStorage;
import com.viaversion.viaversion.api.connection.UserConnection;

public final class AckSequenceTask implements Runnable
{
    private final UserConnection connection;
    private final SequenceStorage sequenceStorage;
    
    public AckSequenceTask(final UserConnection connection, final SequenceStorage sequenceStorage) {
        this.connection = connection;
        this.sequenceStorage = sequenceStorage;
    }
    
    @Override
    public void run() {
        final int sequence = this.sequenceStorage.setSequenceId(-1);
        try {
            final PacketWrapper ackPacket = PacketWrapper.create(ClientboundPackets1_19.BLOCK_CHANGED_ACK, this.connection);
            ackPacket.write(Type.VAR_INT, sequence);
            ackPacket.scheduleSend(Protocol1_19To1_18_2.class);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
