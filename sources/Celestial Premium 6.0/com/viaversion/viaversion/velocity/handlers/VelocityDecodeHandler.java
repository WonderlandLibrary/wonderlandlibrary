/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandler$Sharable
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPipeline
 *  io.netty.handler.codec.MessageToMessageDecoder
 */
package com.viaversion.viaversion.velocity.handlers;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.velocity.handlers.VelocityChannelInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

@ChannelHandler.Sharable
public class VelocityDecodeHandler
extends MessageToMessageDecoder<ByteBuf> {
    private final UserConnection info;

    public VelocityDecodeHandler(UserConnection info) {
        this.info = info;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
        if (!this.info.checkIncomingPacket()) {
            throw CancelDecoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            out.add((Object)bytebuf.retain());
            return;
        }
        ByteBuf transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
        try {
            this.info.transformIncoming(transformedBuf, CancelDecoderException::generate);
            out.add((Object)transformedBuf.retain());
        }
        finally {
            transformedBuf.release();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event != VelocityChannelInitializer.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }
        ChannelPipeline pipeline = ctx.pipeline();
        ChannelHandler encoder = pipeline.get("via-encoder");
        pipeline.remove(encoder);
        pipeline.addBefore("minecraft-encoder", "via-encoder", encoder);
        ChannelHandler decoder = pipeline.get("via-decoder");
        pipeline.remove(decoder);
        pipeline.addBefore("minecraft-decoder", "via-decoder", decoder);
        super.userEventTriggered(ctx, event);
    }
}

