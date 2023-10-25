/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.AbstractChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelHandlerContext
extends AbstractChannelHandlerContext {
    private final ChannelHandler handler;

    private static boolean isInbound(ChannelHandler channelHandler) {
        return channelHandler instanceof ChannelInboundHandler;
    }

    @Override
    public ChannelHandler handler() {
        return this.handler;
    }

    private static boolean isOutbound(ChannelHandler channelHandler) {
        return channelHandler instanceof ChannelOutboundHandler;
    }

    DefaultChannelHandlerContext(DefaultChannelPipeline defaultChannelPipeline, EventExecutorGroup eventExecutorGroup, String string, ChannelHandler channelHandler) {
        super(defaultChannelPipeline, eventExecutorGroup, string, DefaultChannelHandlerContext.isInbound(channelHandler), DefaultChannelHandlerContext.isOutbound(channelHandler));
        if (channelHandler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = channelHandler;
    }
}

