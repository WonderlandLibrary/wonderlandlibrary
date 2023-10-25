/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jboss.marshalling.ByteInput
 *  org.jboss.marshalling.Unmarshaller
 */
package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.marshalling.ChannelBufferByteInput;
import io.netty.handler.codec.marshalling.LimitingByteInput;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import java.util.List;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

public class CompatibleMarshallingDecoder
extends ReplayingDecoder<Void> {
    private boolean discardingTooLongFrame;
    protected final int maxObjectSize;
    protected final UnmarshallerProvider provider;

    public CompatibleMarshallingDecoder(UnmarshallerProvider unmarshallerProvider, int n) {
        this.provider = unmarshallerProvider;
        this.maxObjectSize = n;
    }

    @Override
    protected void decodeLast(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        switch (byteBuf.readableBytes()) {
            case 0: {
                return;
            }
            case 1: {
                if (byteBuf.getByte(byteBuf.readerIndex()) != 121) break;
                byteBuf.skipBytes(1);
                return;
            }
        }
        this.decode(channelHandlerContext, byteBuf, list);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        if (throwable instanceof TooLongFrameException) {
            channelHandlerContext.close();
        } else {
            super.exceptionCaught(channelHandlerContext, throwable);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (this.discardingTooLongFrame) {
            byteBuf.skipBytes(this.actualReadableBytes());
            this.checkpoint();
            return;
        }
        Unmarshaller unmarshaller = this.provider.getUnmarshaller(channelHandlerContext);
        Object object = new ChannelBufferByteInput(byteBuf);
        if (this.maxObjectSize != Integer.MAX_VALUE) {
            object = new LimitingByteInput((ByteInput)object, this.maxObjectSize);
        }
        try {
            unmarshaller.start((ByteInput)object);
            Object object2 = unmarshaller.readObject();
            unmarshaller.finish();
            list.add(object2);
        }
        catch (LimitingByteInput.TooBigObjectException tooBigObjectException) {
            this.discardingTooLongFrame = true;
            throw new TooLongFrameException();
        }
        unmarshaller.close();
    }
}

