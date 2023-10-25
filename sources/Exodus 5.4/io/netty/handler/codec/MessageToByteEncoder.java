/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class MessageToByteEncoder<I>
extends ChannelOutboundHandlerAdapter {
    private final TypeParameterMatcher matcher;
    private final boolean preferDirect;

    protected abstract void encode(ChannelHandlerContext var1, I var2, ByteBuf var3) throws Exception;

    protected MessageToByteEncoder() {
        this(true);
    }

    protected MessageToByteEncoder(Class<? extends I> clazz) {
        this(clazz, true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        ByteBuf byteBuf = null;
        try {
            if (this.acceptOutboundMessage(object)) {
                Object object2 = object;
                byteBuf = this.allocateBuffer(channelHandlerContext, object2, this.preferDirect);
                this.encode(channelHandlerContext, object2, byteBuf);
                ReferenceCountUtil.release(object2);
                if (byteBuf.isReadable()) {
                    channelHandlerContext.write(byteBuf, channelPromise);
                    return;
                } else {
                    byteBuf.release();
                    channelHandlerContext.write(Unpooled.EMPTY_BUFFER, channelPromise);
                }
                return;
            }
            channelHandlerContext.write(object, channelPromise);
            if (byteBuf == null) return;
        }
        catch (EncoderException encoderException) {
            throw encoderException;
        }
        catch (Throwable throwable) {
            throw new EncoderException(throwable);
        }
        byteBuf.release();
    }

    protected ByteBuf allocateBuffer(ChannelHandlerContext channelHandlerContext, I i, boolean bl) throws Exception {
        if (bl) {
            return channelHandlerContext.alloc().ioBuffer();
        }
        return channelHandlerContext.alloc().heapBuffer();
    }

    public boolean acceptOutboundMessage(Object object) throws Exception {
        return this.matcher.match(object);
    }

    protected MessageToByteEncoder(boolean bl) {
        this.matcher = TypeParameterMatcher.find(this, MessageToByteEncoder.class, "I");
        this.preferDirect = bl;
    }

    protected MessageToByteEncoder(Class<? extends I> clazz, boolean bl) {
        this.matcher = TypeParameterMatcher.get(clazz);
        this.preferDirect = bl;
    }
}

