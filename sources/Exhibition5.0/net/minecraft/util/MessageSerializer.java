// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.PacketBuffer;
import java.io.IOException;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.EnumConnectionState;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.EnumPacketDirection;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageSerializer extends MessageToByteEncoder
{
    private static final Logger logger;
    private static final Marker RECEIVED_PACKET_MARKER;
    private final EnumPacketDirection direction;
    private static final String __OBFID = "CL_00001253";
    
    public MessageSerializer(final EnumPacketDirection direction) {
        this.direction = direction;
    }
    
    protected void encode(final ChannelHandlerContext p_encode_1_, final Packet p_encode_2_, final ByteBuf p_encode_3_) throws IOException {
        final Integer var4 = ((EnumConnectionState)p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get()).getPacketId(this.direction, p_encode_2_);
        if (MessageSerializer.logger.isDebugEnabled()) {
            MessageSerializer.logger.debug(MessageSerializer.RECEIVED_PACKET_MARKER, "OUT: [{}:{}] {}", new Object[] { p_encode_1_.channel().attr(NetworkManager.attrKeyConnectionState).get(), var4, p_encode_2_.getClass().getName() });
        }
        if (var4 == null) {
            throw new IOException("Can't serialize unregistered packet");
        }
        final PacketBuffer var5 = new PacketBuffer(p_encode_3_);
        var5.writeVarIntToBuffer(var4);
        try {
            p_encode_2_.writePacketData(var5);
        }
        catch (Throwable var6) {
            MessageSerializer.logger.error((Object)var6);
        }
    }
    
    protected void encode(final ChannelHandlerContext p_encode_1_, final Object p_encode_2_, final ByteBuf p_encode_3_) throws IOException {
        this.encode(p_encode_1_, (Packet)p_encode_2_, p_encode_3_);
    }
    
    static {
        logger = LogManager.getLogger();
        RECEIVED_PACKET_MARKER = MarkerManager.getMarker("PACKET_SENT", NetworkManager.logMarkerPackets);
    }
}
