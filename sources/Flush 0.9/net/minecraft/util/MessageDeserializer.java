package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageDeserializer extends ByteToMessageDecoder {
    private static final Logger logger = LogManager.getLogger();
    private static final Marker RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);
    private final EnumPacketDirection direction;

    public MessageDeserializer(EnumPacketDirection direction) {
        this.direction = direction;
    }

    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 2) {
            PacketBuffer packetbuffer = new PacketBuffer(byteBuf);
            int i = packetbuffer.readVarIntFromBuffer();

            Packet<?> packet = context.channel().attr(NetworkManager.attrKeyConnectionState).get().getPacket(direction, i);

            /*
            byte[] bytes = new byte[byteBuf.readableBytes()];
            int readerIndex = byteBuf.readerIndex();
            byteBuf.getBytes(readerIndex, bytes);

            StringBuilder hexBuilder = new StringBuilder();
            for (byte b : bytes) {
                String s = Integer.toHexString(b);
                hexBuilder.append(s.length() == 1 ? "0" + s : s.substring(s.length() - 2).toUpperCase()).append(' ');
            }

            System.out.println("Received packet " + Integer.toHexString(i) + (packet == null ? "" : " (" + packet.getClass().getName() + ")")
                    + ": " + hexBuilder + "\nutf-8: " + new String(bytes, StandardCharsets.UTF_8));
             */

            if (packet == null) {
                throw new IOException("Bad packet id " + i);
            } else {
                packet.readPacketData(packetbuffer);

                if (packetbuffer.readableBytes() > 0)
                    throw new IOException("Packet " + context.channel().attr(NetworkManager.attrKeyConnectionState).get().getId() + "/" + i + " (" + packet.getClass().getSimpleName() + ") was larger than I expected, found " + packetbuffer.readableBytes() + " bytes extra whilst reading packet " + i);
                else {
                    list.add(packet);

                    if (logger.isDebugEnabled())
                        logger.debug(RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", context.channel().attr(NetworkManager.attrKeyConnectionState).get(), i, packet.getClass().getName());
                }
            }
        }
    }
}
