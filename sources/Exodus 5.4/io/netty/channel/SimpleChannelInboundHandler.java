/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleChannelInboundHandler<I>
extends ChannelInboundHandlerAdapter {
    private final boolean autoRelease;
    private final TypeParameterMatcher matcher;

    protected SimpleChannelInboundHandler(Class<? extends I> clazz) {
        this(clazz, true);
    }

    protected SimpleChannelInboundHandler(Class<? extends I> clazz, boolean bl) {
        this.matcher = TypeParameterMatcher.get(clazz);
        this.autoRelease = bl;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        block2: {
            boolean bl = true;
            if (this.acceptInboundMessage(object)) {
                Object object2 = object;
                this.channelRead0(channelHandlerContext, object2);
            } else {
                bl = false;
                channelHandlerContext.fireChannelRead(object);
            }
            if (!this.autoRelease || !bl) break block2;
            ReferenceCountUtil.release(object);
        }
    }

    public boolean acceptInboundMessage(Object object) throws Exception {
        return this.matcher.match(object);
    }

    protected abstract void channelRead0(ChannelHandlerContext var1, I var2) throws Exception;

    protected SimpleChannelInboundHandler(boolean bl) {
        this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
        this.autoRelease = bl;
    }

    protected SimpleChannelInboundHandler() {
        this(true);
    }
}

