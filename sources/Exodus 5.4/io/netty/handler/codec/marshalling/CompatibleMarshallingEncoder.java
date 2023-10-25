/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jboss.marshalling.ByteOutput
 *  org.jboss.marshalling.Marshaller
 */
package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.marshalling.ChannelBufferByteOutput;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;

@ChannelHandler.Sharable
public class CompatibleMarshallingEncoder
extends MessageToByteEncoder<Object> {
    private final MarshallerProvider provider;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, ByteBuf byteBuf) throws Exception {
        Marshaller marshaller = this.provider.getMarshaller(channelHandlerContext);
        marshaller.start((ByteOutput)new ChannelBufferByteOutput(byteBuf));
        marshaller.writeObject(object);
        marshaller.finish();
        marshaller.close();
    }

    public CompatibleMarshallingEncoder(MarshallerProvider marshallerProvider) {
        this.provider = marshallerProvider;
    }
}

