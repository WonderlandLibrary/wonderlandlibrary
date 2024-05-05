package net.minecraft.client.network;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

/*
 * Exception performing whole class analysis ignored.
 */
class OldServerPinger.1
extends SimpleChannelInboundHandler<ByteBuf> {
    OldServerPinger.1() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        ByteBuf bytebuf = Unpooled.buffer();
        try {
            bytebuf.writeByte(254);
            bytebuf.writeByte(1);
            bytebuf.writeByte(250);
            char[] achar = "MC|PingHost".toCharArray();
            bytebuf.writeShort(achar.length);
            for (char c0 : achar) {
                bytebuf.writeChar((int)c0);
            }
            bytebuf.writeShort(7 + 2 * val$serveraddress.getIP().length());
            bytebuf.writeByte(127);
            achar = val$serveraddress.getIP().toCharArray();
            bytebuf.writeShort(achar.length);
            for (char c1 : achar) {
                bytebuf.writeChar((int)c1);
            }
            bytebuf.writeInt(val$serveraddress.getPort());
            p_channelActive_1_.channel().writeAndFlush((Object)bytebuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
        }
        finally {
            bytebuf.release();
        }
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, ByteBuf p_channelRead0_2_) throws Exception {
        short short1 = p_channelRead0_2_.readUnsignedByte();
        if (short1 == 255) {
            String s = new String(p_channelRead0_2_.readBytes(p_channelRead0_2_.readShort() * 2).array(), Charsets.UTF_16BE);
            String[] astring = (String[])Iterables.toArray((Iterable)OldServerPinger.access$200().split((CharSequence)s), String.class);
            if ("\u00a71".equals((Object)astring[0])) {
                int i = MathHelper.parseIntWithDefault((String)astring[1], (int)0);
                String s1 = astring[2];
                String s2 = astring[3];
                int j = MathHelper.parseIntWithDefault((String)astring[4], (int)-1);
                int k = MathHelper.parseIntWithDefault((String)astring[5], (int)-1);
                val$server.version = -1;
                val$server.gameVersion = s1;
                val$server.serverMOTD = s2;
                val$server.populationInfo = EnumChatFormatting.GRAY + "" + j + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + k;
            }
        }
        p_channelRead0_1_.close();
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
        p_exceptionCaught_1_.close();
    }
}
