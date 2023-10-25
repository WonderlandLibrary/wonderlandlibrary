/*
 * Decompiled with CFR 0.152.
 */
package io.netty.bootstrap;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import io.netty.util.UniqueName;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public final class Bootstrap
extends AbstractBootstrap<Bootstrap, Channel> {
    private volatile SocketAddress remoteAddress;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);

    public Bootstrap remoteAddress(InetAddress inetAddress, int n) {
        this.remoteAddress = new InetSocketAddress(inetAddress, n);
        return this;
    }

    private static void doConnect0(final ChannelFuture channelFuture, final Channel channel, final SocketAddress socketAddress, final SocketAddress socketAddress2, final ChannelPromise channelPromise) {
        channel.eventLoop().execute(new Runnable(){

            @Override
            public void run() {
                if (channelFuture.isSuccess()) {
                    if (socketAddress2 == null) {
                        channel.connect(socketAddress, channelPromise);
                    } else {
                        channel.connect(socketAddress, socketAddress2, channelPromise);
                    }
                    channelPromise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                } else {
                    channelPromise.setFailure(channelFuture.cause());
                }
            }
        });
    }

    public ChannelFuture connect(InetAddress inetAddress, int n) {
        return this.connect(new InetSocketAddress(inetAddress, n));
    }

    public Bootstrap remoteAddress(SocketAddress socketAddress) {
        this.remoteAddress = socketAddress;
        return this;
    }

    public Bootstrap() {
    }

    @Override
    public Bootstrap clone() {
        return new Bootstrap(this);
    }

    @Override
    void init(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast(this.handler());
        Map<ChannelOption<?>, Object> map = this.options();
        Map<UniqueName, Object> map2 = map;
        synchronized (map2) {
            for (Map.Entry<ChannelOption<?>, Object> object : map.entrySet()) {
                try {
                    if (channel.config().setOption(object.getKey(), object.getValue())) continue;
                    logger.warn("Unknown channel option: " + object);
                }
                catch (Throwable entry) {
                    logger.warn("Failed to set a channel option: " + channel, entry);
                }
            }
        }
        map2 = this.attrs();
        Map<UniqueName, Object> map3 = map2;
        synchronized (map3) {
            for (Map.Entry<UniqueName, Object> entry : map2.entrySet()) {
                channel.attr((AttributeKey)entry.getKey()).set(entry.getValue());
            }
        }
    }

    public ChannelFuture connect(String string, int n) {
        return this.connect(new InetSocketAddress(string, n));
    }

    private Bootstrap(Bootstrap bootstrap) {
        super(bootstrap);
        this.remoteAddress = bootstrap.remoteAddress;
    }

    public ChannelFuture connect(SocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(socketAddress, this.localAddress());
    }

    @Override
    public String toString() {
        if (this.remoteAddress == null) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append(", remoteAddress: ");
        stringBuilder.append(this.remoteAddress);
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    public ChannelFuture connect() {
        this.validate();
        SocketAddress socketAddress = this.remoteAddress;
        if (socketAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return this.doConnect(socketAddress, this.localAddress());
    }

    @Override
    public Bootstrap validate() {
        super.validate();
        if (this.handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }

    public Bootstrap remoteAddress(String string, int n) {
        this.remoteAddress = new InetSocketAddress(string, n);
        return this;
    }

    public ChannelFuture connect(SocketAddress socketAddress, SocketAddress socketAddress2) {
        if (socketAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(socketAddress, socketAddress2);
    }

    private ChannelFuture doConnect(final SocketAddress socketAddress, final SocketAddress socketAddress2) {
        final ChannelFuture channelFuture = this.initAndRegister();
        final Channel channel = channelFuture.channel();
        if (channelFuture.cause() != null) {
            return channelFuture;
        }
        final ChannelPromise channelPromise = channel.newPromise();
        if (channelFuture.isDone()) {
            Bootstrap.doConnect0(channelFuture, channel, socketAddress, socketAddress2, channelPromise);
        } else {
            channelFuture.addListener(new ChannelFutureListener(){

                @Override
                public void operationComplete(ChannelFuture channelFuture2) throws Exception {
                    Bootstrap.doConnect0(channelFuture, channel, socketAddress, socketAddress2, channelPromise);
                }
            });
        }
        return channelPromise;
    }
}

