package viaversion.viaversion.protocols.protocol1_9to1_8.providers;

import io.netty.channel.ChannelHandlerContext;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.providers.Provider;
import viaversion.viaversion.protocols.protocol1_9to1_8.storage.MovementTracker;
import viaversion.viaversion.util.PipelineUtil;

public abstract class MovementTransmitterProvider implements Provider {
    public abstract Object getFlyingPacket();

    public abstract Object getGroundPacket();

    public void sendPlayer(UserConnection userConnection) {
        // Old method using packets.
        ChannelHandlerContext context = PipelineUtil.getContextBefore("decoder", userConnection.getChannel().pipeline());
        if (context != null) {
            if (userConnection.get(MovementTracker.class).isGround()) {
                context.fireChannelRead(getGroundPacket());
            } else {
                context.fireChannelRead(getFlyingPacket());
            }
            userConnection.get(MovementTracker.class).incrementIdlePacket();
        }
    }
}
