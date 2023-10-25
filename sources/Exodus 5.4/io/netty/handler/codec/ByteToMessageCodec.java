/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

public abstract class ByteToMessageCodec<I>
extends ChannelDuplexHandler {
    private final ByteToMessageDecoder decoder = new ByteToMessageDecoder(){

        @Override
        protected void decodeLast(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            ByteToMessageCodec.this.decodeLast(channelHandlerContext, byteBuf, list);
        }

        @Override
        public void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            ByteToMessageCodec.this.decode(channelHandlerContext, byteBuf, list);
        }
    };
    private final TypeParameterMatcher outboundMsgMatcher;
    private final MessageToByteEncoder<I> encoder;

    public boolean acceptOutboundMessage(Object object) throws Exception {
        return this.outboundMsgMatcher.match(object);
    }

    protected ByteToMessageCodec() {
        this(true);
    }

    protected ByteToMessageCodec(Class<? extends I> clazz) {
        this(clazz, true);
    }

    protected ByteToMessageCodec(Class<? extends I> clazz, boolean bl) {
        this.checkForSharableAnnotation();
        this.outboundMsgMatcher = TypeParameterMatcher.get(clazz);
        this.encoder = new Encoder(bl);
    }

    protected abstract void encode(ChannelHandlerContext var1, I var2, ByteBuf var3) throws Exception;

    protected abstract void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception;

    protected ByteToMessageCodec(boolean bl) {
        this.outboundMsgMatcher = TypeParameterMatcher.find(this, ByteToMessageCodec.class, "I");
        this.encoder = new Encoder(bl);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.encoder.write(channelHandlerContext, object, channelPromise);
    }

    protected void decodeLast(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        this.decode(channelHandlerContext, byteBuf, list);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.decoder.channelRead(channelHandlerContext, object);
    }

    private void checkForSharableAnnotation() {
        if (this.isSharable()) {
            throw new IllegalStateException("@Sharable annotation is not allowed");
        }
    }

    private final class Encoder
    extends MessageToByteEncoder<I> {
        Encoder(boolean bl) {
            super(bl);
        }

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, I i, ByteBuf byteBuf) throws Exception {
            ByteToMessageCodec.this.encode(channelHandlerContext, i, byteBuf);
        }

        @Override
        public boolean acceptOutboundMessage(Object object) throws Exception {
            return ByteToMessageCodec.this.acceptOutboundMessage(object);
        }
    }
}

