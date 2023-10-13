/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public abstract class ChannelHandlerAdapter
implements ChannelHandler {
    boolean added;

    public boolean isSharable() {
        return this.getClass().isAnnotationPresent(ChannelHandler.Sharable.class);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    @Deprecated
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}

