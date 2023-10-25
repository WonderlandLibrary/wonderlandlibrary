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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.marshalling.ChannelBufferByteInput;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

public class MarshallingDecoder
extends LengthFieldBasedFrameDecoder {
    private final UnmarshallerProvider provider;

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        ByteBuf byteBuf2 = (ByteBuf)super.decode(channelHandlerContext, byteBuf);
        if (byteBuf2 == null) {
            return null;
        }
        Unmarshaller unmarshaller = this.provider.getUnmarshaller(channelHandlerContext);
        ChannelBufferByteInput channelBufferByteInput = new ChannelBufferByteInput(byteBuf2);
        unmarshaller.start((ByteInput)channelBufferByteInput);
        Object object = unmarshaller.readObject();
        unmarshaller.finish();
        Object object2 = object;
        unmarshaller.close();
        return object2;
    }

    @Override
    protected ByteBuf extractFrame(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, int n, int n2) {
        return byteBuf.slice(n, n2);
    }

    public MarshallingDecoder(UnmarshallerProvider unmarshallerProvider) {
        this(unmarshallerProvider, 0x100000);
    }

    public MarshallingDecoder(UnmarshallerProvider unmarshallerProvider, int n) {
        super(n, 0, 4, 0, 4);
        this.provider = unmarshallerProvider;
    }
}

