package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.minecraft.network.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MessageSerializer extends MessageToByteEncoder<Packet> {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);

    private final EnumPacketDirection direction;

    @Contract(pure = true)
	public MessageSerializer(@NotNull EnumPacketDirection direction) {
		this.direction = direction;
	}

    @Override
    protected void encode(@NotNull ChannelHandlerContext ctx,
                          @NotNull Packet packet,
                          @NotNull ByteBuf out) throws Exception {
        EnumConnectionState protocol = ctx.channel().attr(NetworkManager.attrKeyConnectionState).get();
        Integer packetId = protocol.getPacketId(direction, packet);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(RECEIVED_PACKET_MARKER,
                    "OUT: [{}:{}] {}", protocol,
                    packetId,
                    packet.getClass().getName());
        }

        if(packetId == null) {
            throw new IOException("Can't serialize unregistered packet");
        } else {
            PacketBuffer packetBuffer = new PacketBuffer(out);
            packetBuffer.writeVarIntToBuffer(packetId);

            try {
                packet.writePacketData(packetBuffer);
            } catch(Throwable t) {
                LOGGER.error(t);
            }
        }
    }
}
