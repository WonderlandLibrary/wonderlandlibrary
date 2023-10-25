/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFutureListener
extends GenericFutureListener<ChannelFuture> {
    public static final ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE;
    public static final ChannelFutureListener CLOSE;
    public static final ChannelFutureListener CLOSE_ON_FAILURE;

    static {
        CLOSE = new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                channelFuture.channel().close();
            }
        };
        CLOSE_ON_FAILURE = new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (!channelFuture.isSuccess()) {
                    channelFuture.channel().close();
                }
            }
        };
        FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (!channelFuture.isSuccess()) {
                    channelFuture.channel().pipeline().fireExceptionCaught(channelFuture.cause());
                }
            }
        };
    }
}

