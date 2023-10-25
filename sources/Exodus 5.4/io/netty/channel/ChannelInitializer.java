/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public abstract class ChannelInitializer<C extends Channel>
extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);

    @Override
    public final void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        block4: {
            boolean bl;
            block3: {
                ChannelPipeline channelPipeline = channelHandlerContext.pipeline();
                bl = false;
                try {
                    this.initChannel(channelHandlerContext.channel());
                    channelPipeline.remove(this);
                    channelHandlerContext.fireChannelRegistered();
                    bl = true;
                    if (channelPipeline.context(this) == null) break block3;
                    channelPipeline.remove(this);
                }
                catch (Throwable throwable) {
                    logger.warn("Failed to initialize a channel. Closing: " + channelHandlerContext.channel(), throwable);
                    if (channelPipeline.context(this) != null) {
                        channelPipeline.remove(this);
                    }
                    if (bl) break block4;
                    channelHandlerContext.close();
                }
            }
            if (bl) break block4;
            channelHandlerContext.close();
        }
    }

    protected abstract void initChannel(C var1) throws Exception;
}

