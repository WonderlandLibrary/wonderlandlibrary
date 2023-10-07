package viaversion.viaversion.protocols.protocol1_9to1_8;

import viaversion.viaversion.api.Via;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.protocols.base.ProtocolInfo;
import viaversion.viaversion.protocols.protocol1_9to1_8.providers.MovementTransmitterProvider;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;

public class ViaIdleThread implements Runnable {

    @Override
    public void run() {
        for (UserConnection info : Via.getManager().getConnections()) {
            ProtocolInfo protocolInfo = info.getProtocolInfo();
            if (protocolInfo == null || !protocolInfo.getPipeline().contains(Protocol1_9To1_8.class)) continue;

            MovementTracker movementTracker = info.get(MovementTracker.class);
            if (movementTracker == null) continue;

            long nextIdleUpdate = movementTracker.getNextIdlePacket();
            if (nextIdleUpdate <= System.currentTimeMillis() && info.getChannel().isOpen()) {
                Via.getManager().getProviders().get(MovementTransmitterProvider.class).sendPlayer(info);
            }
        }
    }
}
