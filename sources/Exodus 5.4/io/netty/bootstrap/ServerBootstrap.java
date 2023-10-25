/*
 * Decompiled with CFR 0.152.
 */
package io.netty.bootstrap;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.util.AttributeKey;
import io.netty.util.UniqueName;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class ServerBootstrap
extends AbstractBootstrap<ServerBootstrap, ServerChannel> {
    private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap();
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
    private volatile ChannelHandler childHandler;
    private volatile EventLoopGroup childGroup;
    private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap();

    public EventLoopGroup childGroup() {
        return this.childGroup;
    }

    private static Map.Entry<ChannelOption<?>, Object>[] newOptionArray(int n) {
        return new Map.Entry[n];
    }

    @Override
    public ServerBootstrap validate() {
        super.validate();
        if (this.childHandler == null) {
            throw new IllegalStateException("childHandler not set");
        }
        if (this.childGroup == null) {
            logger.warn("childGroup is not set. Using parentGroup instead.");
            this.childGroup = this.group();
        }
        return this;
    }

    public <T> ServerBootstrap childAttr(AttributeKey<T> attributeKey, T t) {
        if (attributeKey == null) {
            throw new NullPointerException("childKey");
        }
        if (t == null) {
            this.childAttrs.remove(attributeKey);
        } else {
            this.childAttrs.put(attributeKey, t);
        }
        return this;
    }

    @Override
    public ServerBootstrap clone() {
        return new ServerBootstrap(this);
    }

    @Override
    void init(Channel channel) throws Exception {
        Map.Entry[] entryArray;
        Map.Entry[] entryArray2;
        Map<ChannelOption<?>, Object> map = this.options();
        Map<UniqueName, Object> map2 = map;
        synchronized (map2) {
            channel.config().setOptions(map);
        }
        map2 = this.attrs();
        Object object2 = map2;
        synchronized (object2) {
            for (Map.Entry<UniqueName, Object> object3 : map2.entrySet()) {
                entryArray2 = (Map.Entry[])object3.getKey();
                channel.attr(entryArray2).set(object3.getValue());
            }
        }
        object2 = channel.pipeline();
        if (this.handler() != null) {
            object2.addLast(this.handler());
        }
        final EventLoopGroup eventLoopGroup = this.childGroup;
        final ChannelHandler channelHandler = this.childHandler;
        Map<UniqueName, Object> map3 = this.childOptions;
        synchronized (map3) {
            entryArray2 = this.childOptions.entrySet().toArray(ServerBootstrap.newOptionArray(this.childOptions.size()));
        }
        map3 = this.childAttrs;
        synchronized (map3) {
            entryArray = this.childAttrs.entrySet().toArray(ServerBootstrap.newAttrArray(this.childAttrs.size()));
        }
        object2.addLast(new ChannelInitializer<Channel>(){

            @Override
            public void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new ServerBootstrapAcceptor(eventLoopGroup, channelHandler, entryArray2, entryArray));
            }
        });
    }

    private static Map.Entry<AttributeKey<?>, Object>[] newAttrArray(int n) {
        return new Map.Entry[n];
    }

    private ServerBootstrap(ServerBootstrap serverBootstrap) {
        super(serverBootstrap);
        this.childGroup = serverBootstrap.childGroup;
        this.childHandler = serverBootstrap.childHandler;
        Map<UniqueName, Object> map = serverBootstrap.childOptions;
        synchronized (map) {
            this.childOptions.putAll(serverBootstrap.childOptions);
        }
        map = serverBootstrap.childAttrs;
        synchronized (map) {
            this.childAttrs.putAll(serverBootstrap.childAttrs);
        }
    }

    public ServerBootstrap() {
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        stringBuilder.setLength(stringBuilder.length() - 1);
        stringBuilder.append(", ");
        if (this.childGroup != null) {
            stringBuilder.append("childGroup: ");
            stringBuilder.append(StringUtil.simpleClassName(this.childGroup));
            stringBuilder.append(", ");
        }
        Map<ChannelOption<?>, Object> map = this.childOptions;
        // MONITORENTER : map
        if (!this.childOptions.isEmpty()) {
            stringBuilder.append("childOptions: ");
            stringBuilder.append(this.childOptions);
            stringBuilder.append(", ");
        }
        // MONITOREXIT : map
        Map<AttributeKey<?>, Object> map2 = this.childAttrs;
        // MONITORENTER : map2
        if (!this.childAttrs.isEmpty()) {
            stringBuilder.append("childAttrs: ");
            stringBuilder.append(this.childAttrs);
            stringBuilder.append(", ");
        }
        // MONITOREXIT : map2
        if (this.childHandler != null) {
            stringBuilder.append("childHandler: ");
            stringBuilder.append(this.childHandler);
            stringBuilder.append(", ");
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == '(') {
            stringBuilder.append(')');
            return stringBuilder.toString();
        }
        stringBuilder.setCharAt(stringBuilder.length() - 2, ')');
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public ServerBootstrap group(EventLoopGroup eventLoopGroup, EventLoopGroup eventLoopGroup2) {
        super.group(eventLoopGroup);
        if (eventLoopGroup2 == null) {
            throw new NullPointerException("childGroup");
        }
        if (this.childGroup != null) {
            throw new IllegalStateException("childGroup set already");
        }
        this.childGroup = eventLoopGroup2;
        return this;
    }

    public ServerBootstrap childHandler(ChannelHandler channelHandler) {
        if (channelHandler == null) {
            throw new NullPointerException("childHandler");
        }
        this.childHandler = channelHandler;
        return this;
    }

    @Override
    public ServerBootstrap group(EventLoopGroup eventLoopGroup) {
        return this.group(eventLoopGroup, eventLoopGroup);
    }

    /*
     * Enabled aggressive block sorting
     */
    public <T> ServerBootstrap childOption(ChannelOption<T> channelOption, T t) {
        if (channelOption == null) {
            throw new NullPointerException("childOption");
        }
        if (t == null) {
            Map<ChannelOption<?>, Object> map = this.childOptions;
            synchronized (map) {
                this.childOptions.remove(channelOption);
                return this;
            }
        }
        Map<ChannelOption<?>, Object> map = this.childOptions;
        synchronized (map) {
            this.childOptions.put(channelOption, t);
            return this;
        }
    }

    private static class ServerBootstrapAcceptor
    extends ChannelInboundHandlerAdapter {
        private final ChannelHandler childHandler;
        private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;
        private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
        private final EventLoopGroup childGroup;

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
            final ChannelConfig channelConfig = channelHandlerContext.channel().config();
            if (channelConfig.isAutoRead()) {
                channelConfig.setAutoRead(false);
                channelHandlerContext.channel().eventLoop().schedule(new Runnable(){

                    @Override
                    public void run() {
                        channelConfig.setAutoRead(true);
                    }
                }, 1L, TimeUnit.SECONDS);
            }
            channelHandlerContext.fireExceptionCaught(throwable);
        }

        ServerBootstrapAcceptor(EventLoopGroup eventLoopGroup, ChannelHandler channelHandler, Map.Entry<ChannelOption<?>, Object>[] entryArray, Map.Entry<AttributeKey<?>, Object>[] entryArray2) {
            this.childGroup = eventLoopGroup;
            this.childHandler = channelHandler;
            this.childOptions = entryArray;
            this.childAttrs = entryArray2;
        }

        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
            final Channel channel = (Channel)object;
            channel.pipeline().addLast(this.childHandler);
            for (Map.Entry<ChannelOption<?>, Object> entry : this.childOptions) {
                try {
                    if (channel.config().setOption(entry.getKey(), entry.getValue())) continue;
                    logger.warn("Unknown channel option: " + entry);
                }
                catch (Throwable throwable) {
                    logger.warn("Failed to set a channel option: " + channel, throwable);
                }
            }
            for (Map.Entry<UniqueName, Object> entry : this.childAttrs) {
                channel.attr((AttributeKey)entry.getKey()).set(entry.getValue());
            }
            try {
                this.childGroup.register(channel).addListener(new ChannelFutureListener(){

                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (!channelFuture.isSuccess()) {
                            ServerBootstrapAcceptor.forceClose(channel, channelFuture.cause());
                        }
                    }
                });
            }
            catch (Throwable throwable) {
                ServerBootstrapAcceptor.forceClose(channel, throwable);
            }
        }

        private static void forceClose(Channel channel, Throwable throwable) {
            channel.unsafe().closeForcibly();
            logger.warn("Failed to register an accepted channel: " + channel, throwable);
        }
    }
}

