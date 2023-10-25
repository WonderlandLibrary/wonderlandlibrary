/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.sctp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.sctp.SctpMessageCompletionHandler;
import java.util.List;

public class SctpInboundByteStreamHandler
extends MessageToMessageDecoder<SctpMessage> {
    private final int streamIdentifier;
    private final int protocolIdentifier;

    @Override
    public final boolean acceptInboundMessage(Object object) throws Exception {
        if (super.acceptInboundMessage(object)) {
            return this.acceptInboundMessage((SctpMessage)object);
        }
        return false;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, SctpMessage sctpMessage, List<Object> list) throws Exception {
        if (!sctpMessage.isComplete()) {
            throw new CodecException(String.format("Received SctpMessage is not complete, please add %s in the pipeline before this handler", SctpMessageCompletionHandler.class.getSimpleName()));
        }
        list.add(sctpMessage.content().retain());
    }

    protected boolean acceptInboundMessage(SctpMessage sctpMessage) {
        return sctpMessage.protocolIdentifier() == this.protocolIdentifier && sctpMessage.streamIdentifier() == this.streamIdentifier;
    }

    public SctpInboundByteStreamHandler(int n, int n2) {
        this.protocolIdentifier = n;
        this.streamIdentifier = n2;
    }
}

