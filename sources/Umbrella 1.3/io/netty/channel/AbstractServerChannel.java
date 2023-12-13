/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel;

import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ServerChannel;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;

public abstract class AbstractServerChannel
extends AbstractChannel
implements ServerChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);

    protected AbstractServerChannel() {
        super(null);
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    public SocketAddress remoteAddress() {
        return null;
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }

    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new DefaultServerUnsafe();
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }

    private final class DefaultServerUnsafe
    extends AbstractChannel.AbstractUnsafe {
        private DefaultServerUnsafe() {
        }

        @Override
        public void write(Object msg, ChannelPromise promise) {
            ReferenceCountUtil.release(msg);
            this.reject(promise);
        }

        @Override
        public void flush() {
        }

        @Override
        public void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
            this.reject(promise);
        }

        private void reject(ChannelPromise promise) {
            promise.setFailure(new UnsupportedOperationException());
        }
    }
}

