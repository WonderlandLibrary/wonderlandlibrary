/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.barchart.udt.TypeUDT
 *  com.barchart.udt.nio.SocketChannelUDT
 */
package io.netty.channel.udt.nio;

import com.barchart.udt.TypeUDT;
import com.barchart.udt.nio.SocketChannelUDT;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.udt.nio.NioUdtAcceptorChannel;
import io.netty.channel.udt.nio.NioUdtByteConnectorChannel;
import java.util.List;

public class NioUdtByteAcceptorChannel
extends NioUdtAcceptorChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        SocketChannelUDT socketChannelUDT = this.javaChannel().accept();
        if (socketChannelUDT == null) {
            return 0;
        }
        list.add(new NioUdtByteConnectorChannel((Channel)this, socketChannelUDT));
        return 1;
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    public NioUdtByteAcceptorChannel() {
        super(TypeUDT.STREAM);
    }
}

