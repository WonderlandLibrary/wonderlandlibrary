// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.Packet;
import java.io.IOException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.EnumPacketDirection;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDeserializer extends ByteToMessageDecoder
{
    private static final Logger logger;
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;
    private static final String __OBFID = "CL_00001252";
    
    public MessageDeserializer(final EnumPacketDirection direction) {
        this.direction = direction;
    }
    
    protected void decode(final ChannelHandlerContext p_decode_1_, final ByteBuf p_decode_2_, final List p_decode_3_) throws IOException, InstantiationException, IllegalAccessException {
        if (p_decode_2_.readableBytes() != 0) {
            final PacketBuffer var4 = new PacketBuffer(p_decode_2_);
            final int var5 = var4.readVarIntFromBuffer();
            final Packet var6 = ((EnumConnectionState)p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getPacket(this.direction, var5);
            if (var6 == null) {
                throw new IOException("Bad packet id " + var5);
            }
            var6.readPacketData(var4);
            if (var4.readableBytes() > 0) {
                throw new IOException("Packet " + ((EnumConnectionState)p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getId() + "/" + var5 + " (" + var6.getClass().getSimpleName() + ") was larger than I expected, found " + var4.readableBytes() + " bytes extra whilst reading packet " + var5);
            }
            p_decode_3_.add(var6);
            if (MessageDeserializer.logger.isDebugEnabled()) {
                MessageDeserializer.logger.debug(MessageDeserializer.RECEIVED_PACKET_MARKER, " IN: [{}:{}] {}", new Object[] { p_decode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), var5, var6.getClass().getName() });
            }
        }
    }
    
    static {
        logger = LogManager.getLogger();
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_RECEIVED", NetworkManager.logMarkerPackets);
    }
}
