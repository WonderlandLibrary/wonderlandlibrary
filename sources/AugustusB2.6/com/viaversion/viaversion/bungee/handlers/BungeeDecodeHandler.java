// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.handlers;

import com.viaversion.viaversion.exception.CancelCodecException;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelDecoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
public class BungeeDecodeHandler extends MessageToMessageDecoder<ByteBuf>
{
    private final UserConnection info;
    
    public BungeeDecodeHandler(final UserConnection info) {
        this.info = info;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!ctx.channel().isActive()) {
            throw CancelDecoderException.generate(null);
        }
        if (!this.info.checkServerboundPacket()) {
            throw CancelDecoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            this.info.transformServerbound(transformedBuf, (Function<Throwable, Exception>)CancelDecoderException::generate);
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
