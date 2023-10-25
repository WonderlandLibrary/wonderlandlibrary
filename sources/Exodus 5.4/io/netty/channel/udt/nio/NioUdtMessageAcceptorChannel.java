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
import io.netty.channel.ChannelMetadata;
import io.netty.channel.udt.nio.NioUdtAcceptorChannel;
import io.netty.channel.udt.nio.NioUdtMessageConnectorChannel;
import java.util.List;

public class NioUdtMessageAcceptorChannel
extends NioUdtAcceptorChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);

    public NioUdtMessageAcceptorChannel() {
        super(TypeUDT.DATAGRAM);
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        SocketChannelUDT socketChannelUDT = this.javaChannel().accept();
        if (socketChannelUDT == null) {
            return 0;
        }
        list.add(new NioUdtMessageConnectorChannel(this, socketChannelUDT));
        return 1;
    }
}

