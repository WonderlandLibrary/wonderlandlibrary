/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.nio.AbstractNioMessageChannel;
import io.netty.channel.socket.DefaultServerSocketChannelConfig;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;

public class NioServerSocketChannel
extends AbstractNioMessageChannel
implements ServerSocketChannel {
    private static final InternalLogger logger;
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER;
    private static final ChannelMetadata METADATA;
    private final ServerSocketChannelConfig config = new NioServerSocketChannelConfig(this, this.javaChannel().socket());

    @Override
    protected int doReadMessages(List<Object> list) throws Exception {
        SocketChannel socketChannel = this.javaChannel().accept();
        try {
            if (socketChannel != null) {
                list.add(new NioSocketChannel((Channel)this, socketChannel));
                return 1;
            }
        }
        catch (Throwable throwable) {
            logger.warn("Failed to create a new channel from an accepted socket.", throwable);
            try {
                socketChannel.close();
            }
            catch (Throwable throwable2) {
                logger.warn("Failed to close a socket.", throwable2);
            }
        }
        return 0;
    }

    @Override
    protected void doFinishConnect() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doDisconnect() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SocketAddress remoteAddress0() {
        return null;
    }

    private static java.nio.channels.ServerSocketChannel newSocket(SelectorProvider selectorProvider) {
        try {
            return selectorProvider.openServerSocketChannel();
        }
        catch (IOException iOException) {
            throw new ChannelException("Failed to open a server socket.", iOException);
        }
    }

    public NioServerSocketChannel() {
        this(NioServerSocketChannel.newSocket(DEFAULT_SELECTOR_PROVIDER));
    }

    @Override
    protected java.nio.channels.ServerSocketChannel javaChannel() {
        return (java.nio.channels.ServerSocketChannel)super.javaChannel();
    }

    @Override
    public ChannelMetadata metadata() {
        return METADATA;
    }

    @Override
    protected final Object filterOutboundMessage(Object object) throws Exception {
        throw new UnsupportedOperationException();
    }

    static {
        METADATA = new ChannelMetadata(false);
        DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
        logger = InternalLoggerFactory.getInstance(NioServerSocketChannel.class);
    }

    public NioServerSocketChannel(java.nio.channels.ServerSocketChannel serverSocketChannel) {
        super(null, serverSocketChannel, 16);
    }

    @Override
    public ServerSocketChannelConfig config() {
        return this.config;
    }

    @Override
    protected boolean doConnect(SocketAddress socketAddress, SocketAddress socketAddress2) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }

    @Override
    protected void doClose() throws Exception {
        this.javaChannel().close();
    }

    @Override
    protected boolean doWriteMessage(Object object, ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.javaChannel().socket().bind(socketAddress, this.config.getBacklog());
    }

    @Override
    public boolean isActive() {
        return this.javaChannel().socket().isBound();
    }

    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }

    public NioServerSocketChannel(SelectorProvider selectorProvider) {
        this(NioServerSocketChannel.newSocket(selectorProvider));
    }

    @Override
    public InetSocketAddress remoteAddress() {
        return null;
    }

    private final class NioServerSocketChannelConfig
    extends DefaultServerSocketChannelConfig {
        private NioServerSocketChannelConfig(NioServerSocketChannel nioServerSocketChannel2, ServerSocket serverSocket) {
            super(nioServerSocketChannel2, serverSocket);
        }

        @Override
        protected void autoReadCleared() {
            NioServerSocketChannel.this.setReadPending(false);
        }
    }
}

