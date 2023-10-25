/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.oio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ThreadPerChannelEventLoop;
import java.net.ConnectException;
import java.net.SocketAddress;

public abstract class AbstractOioChannel
extends AbstractChannel {
    private volatile boolean readPending;
    protected static final int SO_TIMEOUT = 1000;
    private final Runnable readTask = new Runnable(){

        @Override
        public void run() {
            if (!AbstractOioChannel.this.isReadPending() && !AbstractOioChannel.this.config().isAutoRead()) {
                return;
            }
            AbstractOioChannel.this.setReadPending(false);
            AbstractOioChannel.this.doRead();
        }
    };

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof ThreadPerChannelEventLoop;
    }

    protected void setReadPending(boolean bl) {
        this.readPending = bl;
    }

    protected abstract void doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

    protected abstract void doRead();

    @Override
    protected void doBeginRead() throws Exception {
        if (this.isReadPending()) {
            return;
        }
        this.setReadPending(true);
        this.eventLoop().execute(this.readTask);
    }

    @Override
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new DefaultOioUnsafe();
    }

    protected boolean isReadPending() {
        return this.readPending;
    }

    protected AbstractOioChannel(Channel channel) {
        super(channel);
    }

    private final class DefaultOioUnsafe
    extends AbstractChannel.AbstractUnsafe {
        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            if (!channelPromise.setUncancellable() || !this.ensureOpen(channelPromise)) {
                return;
            }
            try {
                boolean bl = AbstractOioChannel.this.isActive();
                AbstractOioChannel.this.doConnect(socketAddress, socketAddress2);
                this.safeSetSuccess(channelPromise);
                if (!bl && AbstractOioChannel.this.isActive()) {
                    AbstractOioChannel.this.pipeline().fireChannelActive();
                }
            }
            catch (Throwable throwable) {
                ConnectException connectException;
                if (throwable instanceof ConnectException) {
                    ConnectException connectException2 = new ConnectException(throwable.getMessage() + ": " + socketAddress);
                    connectException2.setStackTrace(throwable.getStackTrace());
                    connectException = connectException2;
                }
                this.safeSetFailure(channelPromise, connectException);
                this.closeIfClosed();
            }
        }

        private DefaultOioUnsafe() {
            super(AbstractOioChannel.this);
        }
    }
}

