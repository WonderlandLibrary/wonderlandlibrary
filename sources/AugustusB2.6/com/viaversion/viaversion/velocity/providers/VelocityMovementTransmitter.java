// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.providers;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;

public class VelocityMovementTransmitter extends MovementTransmitterProvider
{
    @Override
    public Object getFlyingPacket() {
        return null;
    }
    
    @Override
    public Object getGroundPacket() {
        return null;
    }
    
    @Override
    public void sendPlayer(final UserConnection userConnection) {
        if (userConnection.getProtocolInfo().getState() == State.PLAY) {
            final PacketWrapper wrapper = PacketWrapper.create(ServerboundPackets1_8.PLAYER_MOVEMENT, null, userConnection);
            final MovementTracker tracker = userConnection.get(MovementTracker.class);
            wrapper.write(Type.BOOLEAN, tracker.isGround());
            try {
                wrapper.scheduleSendToServer(Protocol1_9To1_8.class);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            tracker.incrementIdlePacket();
        }
    }
}
