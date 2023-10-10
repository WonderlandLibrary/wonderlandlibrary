// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.velocity.handlers;

import com.viaversion.viaversion.exception.CancelCodecException;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelEncoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class VelocityEncodeHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final UserConnection info;
    
    public VelocityEncodeHandler(final UserConnection info) {
        this.info = info;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!this.info.checkOutgoingPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            this.info.transformOutgoing(transformedBuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
            out.add(transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}
