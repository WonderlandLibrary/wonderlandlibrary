// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.sponge.handlers;

import com.viaversion.viaversion.exception.CancelCodecException;
import java.util.function.Function;
import com.viaversion.viaversion.exception.CancelEncoderException;
import java.lang.reflect.InvocationTargetException;
import com.viaversion.viaversion.util.PipelineUtil;
import com.viaversion.viaversion.handlers.ChannelHandlerContextWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.handlers.ViaCodecHandler;
import io.netty.handler.codec.MessageToByteEncoder;

public class SpongeEncodeHandler extends MessageToByteEncoder<Object> implements ViaCodecHandler
{
    private final UserConnection info;
    private final MessageToByteEncoder<?> minecraftEncoder;
    
    public SpongeEncodeHandler(final UserConnection info, final MessageToByteEncoder<?> minecraftEncoder) {
        this.info = info;
        this.minecraftEncoder = minecraftEncoder;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object o, final ByteBuf bytebuf) throws Exception {
        if (!(o instanceof ByteBuf)) {
            try {
                PipelineUtil.callEncode(this.minecraftEncoder, new ChannelHandlerContextWrapper(ctx, this), o, bytebuf);
            }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception)e.getCause();
                }
                if (e.getCause() instanceof Error) {
                    throw (Error)e.getCause();
                }
            }
        }
        else {
            bytebuf.writeBytes((ByteBuf)o);
        }
        this.transform(bytebuf);
    }
    
    @Override
    public void transform(final ByteBuf bytebuf) throws Exception {
        if (!this.info.checkClientboundPacket()) {
            throw CancelEncoderException.generate(null);
        }
        if (!this.info.shouldTransformPacket()) {
            return;
        }
        this.info.transformClientbound(bytebuf, (Function<Throwable, Exception>)CancelEncoderException::generate);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof CancelCodecException) {
            return;
        }
        super.exceptionCaught(ctx, cause);
    }
}
