/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ServerChannel;
import java.net.SocketAddress;

public abstract class AbstractServerChannel
extends AbstractChannel
implements ServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }

    @Override
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new DefaultServerUnsafe();
    }

    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }

    protected AbstractServerChannel() {
        super(null);
    }

    @Override
    public SocketAddress remoteAddress() {
        return null;
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected final Object filterOutboundMessage(Object object) {
        throw new UnsupportedOperationException();
    }

    private final class DefaultServerUnsafe
    extends AbstractChannel.AbstractUnsafe {
        private DefaultServerUnsafe() {
            super(AbstractServerChannel.this);
        }

        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            this.safeSetFailure(channelPromise, new UnsupportedOperationException());
        }
    }
}

