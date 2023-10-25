/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.epoll.AbstractEpollChannel;
import io.netty.channel.epoll.EpollEventLoop;
import io.netty.channel.epoll.EpollServerSocketChannelConfig;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.epoll.Native;
import io.netty.channel.socket.ServerSocketChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class EpollServerSocketChannel
extends AbstractEpollChannel
implements ServerSocketChannel {
    private volatile InetSocketAddress local;
    private final EpollServerSocketChannelConfig config = new EpollServerSocketChannelConfig(this);

    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        throw new UnsupportedOperationException();
    }

    public EpollServerSocketChannel() {
        super(Native.socketStreamFd(), 4);
    }

    @Override
    protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
        return new EpollServerSocketUnsafe();
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
        EpollServerSocketChannel.checkResolvable(inetSocketAddress);
        Native.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        this.local = Native.localAddress(this.fd);
        Native.listen(this.fd, this.config.getBacklog());
        this.active = true;
    }

    @Override
    protected Object filterOutboundMessage(Object object) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected InetSocketAddress remoteAddress0() {
        return null;
    }

    @Override
    public EpollServerSocketChannelConfig config() {
        return this.config;
    }

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof EpollEventLoop;
    }

    final class EpollServerSocketUnsafe
    extends AbstractEpollChannel.AbstractEpollUnsafe {
        EpollServerSocketUnsafe() {
            super(EpollServerSocketChannel.this);
        }

        @Override
        public void connect(SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) {
            channelPromise.setFailure(new UnsupportedOperationException());
        }

        @Override
        void epollInReady() {
            block8: {
                assert (EpollServerSocketChannel.this.eventLoop().inEventLoop());
                ChannelPipeline channelPipeline = EpollServerSocketChannel.this.pipeline();
                Throwable throwable = null;
                block4: while (true) {
                    try {
                        int n;
                        while ((n = Native.accept(EpollServerSocketChannel.this.fd)) != -1) {
                            try {
                                this.readPending = false;
                                channelPipeline.fireChannelRead(new EpollSocketChannel(EpollServerSocketChannel.this, n));
                                continue block4;
                            }
                            catch (Throwable throwable2) {
                                channelPipeline.fireChannelReadComplete();
                                channelPipeline.fireExceptionCaught(throwable2);
                            }
                        }
                        break;
                    }
                    catch (Throwable throwable3) {
                        throwable = throwable3;
                        break;
                    }
                }
                channelPipeline.fireChannelReadComplete();
                if (throwable != null) {
                    channelPipeline.fireExceptionCaught(throwable);
                }
                if (EpollServerSocketChannel.this.config.isAutoRead() || this.readPending) break block8;
                this.clearEpollIn0();
            }
        }
    }
}

