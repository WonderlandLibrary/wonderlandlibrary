/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.sctp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SctpMessageCompletionHandler
extends MessageToMessageDecoder<SctpMessage> {
    private final Map<Integer, ByteBuf> fragments = new HashMap<Integer, ByteBuf>();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, SctpMessage sctpMessage, List<Object> list) throws Exception {
        ByteBuf byteBuf = sctpMessage.content();
        int n = sctpMessage.protocolIdentifier();
        int n2 = sctpMessage.streamIdentifier();
        boolean bl = sctpMessage.isComplete();
        ByteBuf byteBuf2 = this.fragments.containsKey(n2) ? this.fragments.remove(n2) : Unpooled.EMPTY_BUFFER;
        if (bl && !byteBuf2.isReadable()) {
            list.add(sctpMessage);
        } else if (!bl && byteBuf2.isReadable()) {
            this.fragments.put(n2, Unpooled.wrappedBuffer(byteBuf2, byteBuf));
        } else if (bl && byteBuf2.isReadable()) {
            this.fragments.remove(n2);
            SctpMessage sctpMessage2 = new SctpMessage(n, n2, Unpooled.wrappedBuffer(byteBuf2, byteBuf));
            list.add(sctpMessage2);
        } else {
            this.fragments.put(n2, byteBuf);
        }
        byteBuf.retain();
    }
}

