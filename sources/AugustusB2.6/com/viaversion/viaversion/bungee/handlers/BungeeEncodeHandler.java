// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.bungee.handlers;

import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.bungee.util.BungeePipelineUtil;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelEncoderException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import io.netty.channel.ChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class BungeeEncodeHandler extends MessageToMessageEncoder<ByteBuf>
{
    private final UserConnection info;
    private boolean handledCompression;
    
    public BungeeEncodeHandler(final UserConnection info) {
        this.info = info;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf bytebuf, final List<Object> out) throws Exception {
        if (!ctx.channel().isActive()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.checkClientboundPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            out.add(bytebuf.retain());
            return;
        }
        final ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            final boolean needsCompress = this.handleCompressionOrder(ctx, transformedBuf);
            this.info.transformClientbound(transformedBuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
            if (needsCompress) {
                this.recompress(ctx, transformedBuf);
            }
            out.add(transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }
    
    private boolean handleCompressionOrder(final ChannelHandlerContext ctx, final ByteBuf buf) {
        boolean needsCompress = false;
        if (!this.handledCompression && ctx.pipeline().names().indexOf("compress") > ctx.pipeline().names().indexOf("via-encoder")) {
            final ByteBuf decompressed = BungeePipelineUtil.decompress(ctx, buf);
            if (buf != decompressed) {
                try {
                    buf.clear().writeBytes(decompressed);
                }
                finally {
                    decompressed.release();
                }
            }
            final ChannelHandler dec = ctx.pipeline().get("via-decoder");
            final ChannelHandler enc = ctx.pipeline().get("via-encoder");
            ctx.pipeline().remove(dec);
            ctx.pipeline().remove(enc);
            ctx.pipeline().addAfter("decompress", "via-decoder", dec);
            ctx.pipeline().addAfter("compress", "via-encoder", enc);
            needsCompress = true;
            this.handledCompression = true;
        }
        return needsCompress;
    }
    
    private void recompress(final ChannelHandlerContext ctx, final ByteBuf buf) {
        final ByteBuf compressed = BungeePipelineUtil.compress(ctx, buf);
        try {
            buf.clear().writeBytes(compressed);
        }
        finally {
            compressed.release();
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
