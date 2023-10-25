/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.Map;

public abstract class ChannelHandlerAdapter
implements ChannelHandler {
    boolean added;

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    public boolean isSharable() {
        Class<?> clazz = this.getClass();
        Map<Class<?>, Boolean> map = InternalThreadLocalMap.get().handlerSharableCache();
        Boolean bl = map.get(clazz);
        if (bl == null) {
            bl = clazz.isAnnotationPresent(ChannelHandler.Sharable.class);
            map.put(clazz, bl);
        }
        return bl;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        channelHandlerContext.fireExceptionCaught(throwable);
    }
}

