package viaversion.viaversion.protocols.protocol1_9to1_8;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.remapper.PacketHandler;
import viaversion.viaversion.api.type.Type;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;

public class PlayerMovementMapper implements PacketHandler {
    @Override
    public void handle(PacketWrapper wrapper) throws Exception {
        MovementTracker tracker = wrapper.user().get(MovementTracker.class);
        tracker.incrementIdlePacket();
        // If packet has the ground data
        if (wrapper.is(Type.BOOLEAN, 0)) {
            tracker.setGround(wrapper.get(Type.BOOLEAN, 0));
        }
    }
}
