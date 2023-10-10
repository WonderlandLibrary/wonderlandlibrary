// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8;

import com.viaversion.viaversion.api.connection.ProtocolInfo;
import java.util.Iterator;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.Via;

public class ViaIdleThread implements Runnable
{
    @Override
    public void run() {
        for (final UserConnection info : Via.getManager().getConnectionManager().getConnections()) {
            final ProtocolInfo protocolInfo = info.getProtocolInfo();
            if (protocolInfo != null) {
                if (!protocolInfo.getPipeline().contains(Protocol1_9To1_8.class)) {
                    continue;
                }
                final MovementTracker movementTracker = info.get(MovementTracker.class);
                if (movementTracker == null) {
                    continue;
                }
                final long nextIdleUpdate = movementTracker.getNextIdlePacket();
                if (nextIdleUpdate > System.currentTimeMillis() || !info.getChannel().isOpen()) {
                    continue;
                }
                Via.getManager().getProviders().get(MovementTransmitterProvider.class).sendPlayer(info);
            }
        }
    }
}
