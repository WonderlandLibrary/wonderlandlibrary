// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class ForwardMessageToByteEncoder extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) throws Exception {
        out.writeBytes(msg);
    }
}
