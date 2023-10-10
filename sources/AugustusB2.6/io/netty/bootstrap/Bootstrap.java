// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.bootstrap;

import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import com.google.common.collect.Sets;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.charset.Charset;
import com.google.common.hash.Hashing;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.EmptyArrays;
import java.net.SocketException;
import java.util.Iterator;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import io.netty.channel.ChannelOption;
import java.util.Map;
import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import com.google.common.base.Splitter;
import com.google.common.base.Joiner;
import java.net.SocketAddress;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.Channel;

public final class Bootstrap extends AbstractBootstrap<Bootstrap, Channel>
{
    private static final InternalLogger logger;
    private volatile SocketAddress remoteAddress;
    private static final Joiner DOT_JOINER;
    private static final Splitter DOT_SPLITTER;
    @VisibleForTesting
    static final Set<String> BLOCKED_SERVERS;
    
    public Bootstrap() {
    }
    
    private Bootstrap(final Bootstrap bootstrap) {
        super(bootstrap);
        this.remoteAddress = bootstrap.remoteAddress;
    }
    
    public Bootstrap remoteAddress(final SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }
    
    public Bootstrap remoteAddress(final String inetHost, final int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }
    
    public Bootstrap remoteAddress(final InetAddress inetHost, final int inetPort) {
        this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }
    
    public ChannelFuture connect() {
        this.validate();
        final SocketAddress remoteAddress = this.remoteAddress;
        if (remoteAddress == null) {
            throw new IllegalStateException("remoteAddress not set");
        }
        return this.doConnect(remoteAddress, this.localAddress());
    }
    
    public ChannelFuture connect(final String inetHost, final int inetPort) {
        return this.connect(new InetSocketAddress(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final InetAddress inetHost, final int inetPort) {
        return this.connect(new InetSocketAddress(inetHost, inetPort));
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(remoteAddress, this.localAddress());
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        this.validate();
        return this.doConnect(remoteAddress, localAddress);
    }
    
    private ChannelFuture doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        final ChannelFuture future = this.checkAddress(remoteAddress);
        if (future != null) {
            return future;
        }
        final ChannelFuture regFuture = this.initAndRegister();
        final Channel channel = regFuture.channel();
        if (regFuture.cause() != null) {
            return regFuture;
        }
        final ChannelPromise promise = channel.newPromise();
        if (regFuture.isDone()) {
            doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
        }
        else {
            regFuture.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    doConnect0(regFuture, channel, remoteAddress, localAddress, promise);
                }
            });
        }
        return promise;
    }
    
    private static void doConnect0(final ChannelFuture regFuture, final Channel channel, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                if (regFuture.isSuccess()) {
                    if (localAddress == null) {
                        channel.connect(remoteAddress, promise);
                    }
                    else {
                        channel.connect(remoteAddress, localAddress, promise);
                    }
                    promise.addListener((GenericFutureListener<? extends Future<? super Void>>)ChannelFutureListener.CLOSE_ON_FAILURE);
                }
                else {
                    promise.setFailure(regFuture.cause());
                }
            }
        });
    }
    
    @Override
    void init(final Channel channel) throws Exception {
        final ChannelPipeline p = channel.pipeline();
        p.addLast(this.handler());
        final Map<ChannelOption<?>, Object> options = this.options();
        synchronized (options) {
            for (final Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
                try {
                    if (channel.config().setOption(e.getKey(), e.getValue())) {
                        continue;
                    }
                    Bootstrap.logger.warn("Unknown channel option: " + e);
                }
                catch (Throwable t) {
                    Bootstrap.logger.warn("Failed to set a channel option: " + channel, t);
                }
            }
        }
        final Map<AttributeKey<?>, Object> attrs = this.attrs();
        synchronized (attrs) {
            for (final Map.Entry<AttributeKey<?>, Object> e2 : attrs.entrySet()) {
                channel.attr(e2.getKey()).set(e2.getValue());
            }
        }
    }
    
    @Override
    public Bootstrap validate() {
        super.validate();
        if (this.handler() == null) {
            throw new IllegalStateException("handler not set");
        }
        return this;
    }
    
    @Override
    public Bootstrap clone() {
        return new Bootstrap(this);
    }
    
    @Override
    public String toString() {
        if (this.remoteAddress == null) {
            return super.toString();
        }
        final StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        buf.append(", remoteAddress: ");
        buf.append(this.remoteAddress);
        buf.append(')');
        return buf.toString();
    }
    
    @Nullable
    @VisibleForTesting
    ChannelFuture checkAddress(final SocketAddress remoteAddress) {
        if (remoteAddress instanceof InetSocketAddress) {
            final InetAddress address = ((InetSocketAddress)remoteAddress).getAddress();
            if (this.isBlockedServer(address.getHostAddress()) || this.isBlockedServer(address.getHostName())) {
                final Channel channel = (Channel)this.channelFactory().newChannel();
                channel.unsafe().closeForcibly();
                final SocketException cause = new SocketException("Network is unreachable");
                cause.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
                return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(cause);
            }
        }
        return null;
    }
    
    public boolean isBlockedServer(String server) {
        if (server == null || server.isEmpty()) {
            return false;
        }
        while (server.charAt(server.length() - 1) == '.') {
            server = server.substring(0, server.length() - 1);
        }
        if (this.isBlockedServerHostName(server)) {
            return true;
        }
        final List<String> strings = (List<String>)Lists.newArrayList((Iterable<?>)Bootstrap.DOT_SPLITTER.split(server));
        boolean isIp = strings.size() == 4;
        if (isIp) {
            for (final String string : strings) {
                try {
                    final int part = Integer.parseInt(string);
                    if (part >= 0 && part <= 255) {
                        continue;
                    }
                }
                catch (NumberFormatException ex) {}
                isIp = false;
                break;
            }
        }
        if (!isIp && this.isBlockedServerHostName("*." + server)) {
            return true;
        }
        while (strings.size() > 1) {
            strings.remove(isIp ? (strings.size() - 1) : 0);
            final String starredPart = isIp ? (Bootstrap.DOT_JOINER.join(strings) + ".*") : ("*." + Bootstrap.DOT_JOINER.join(strings));
            if (this.isBlockedServerHostName(starredPart)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isBlockedServerHostName(final String server) {
        return Bootstrap.BLOCKED_SERVERS.contains(Hashing.sha1().hashBytes(server.toLowerCase().getBytes(Charset.forName("ISO-8859-1"))).toString());
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Bootstrap.class);
        DOT_JOINER = Joiner.on('.');
        DOT_SPLITTER = Splitter.on('.');
        BLOCKED_SERVERS = Sets.newHashSet();
        try {
            Bootstrap.BLOCKED_SERVERS.addAll(IOUtils.readLines(new URL("https://sessionserver.mojang.com/blockedservers").openConnection().getInputStream()));
        }
        catch (IOException ex) {}
    }
}
