package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;

public class MessageDeserializer extends ByteToMessageDecoder {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);

	private final EnumPacketDirection direction;

	public MessageDeserializer(EnumPacketDirection direction) {
		this.direction = direction;
	}

	@Override
	protected void decode(@NotNull ChannelHandlerContext ctx,
						  @NotNull ByteBuf byteBuf,
						  @NotNull List<Object> out) throws Exception {
		if (byteBuf.readableBytes() != 0) {
			PacketBuffer packetbuffer = new PacketBuffer(byteBuf);
			int i = packetbuffer.readVarIntFromBuffer();
			Packet<?> packet = ctx.channel().attr(NetworkManager.attrKeyConnectionState).get().getPacket(direction, i);

			if (packet == null) {
				throw new IOException("Bad packet id " + i);
			} else {
				packet.readPacketData(packetbuffer);

				if(packetbuffer.readableBytes() > 0) {
					throw new IOException(
							"Packet " + ctx.channel().attr(NetworkManager.attrKeyConnectionState).get().getId() + "/" + i + " ("
									+ packet.getClass().getCanonicalName() + ") was larger than I expected, found "
									+ packetbuffer.readableBytes() + " bytes extra whilst reading packet " + i);
				} else {
					out.add(packet);

					if(LOGGER.isDebugEnabled()) {
						LOGGER.debug(RECEIVED_PACKET_MARKER,
								" IN: [{}:{}] {}",
								ctx.channel().attr(NetworkManager.attrKeyConnectionState).get(),
								i,
								packet.getClass().getName());
					}
				}
			}
		}
	}
}
