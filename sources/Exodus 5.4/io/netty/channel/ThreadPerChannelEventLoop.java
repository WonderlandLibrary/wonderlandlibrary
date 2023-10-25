/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.ThreadPerChannelEventLoopGroup;

public class ThreadPerChannelEventLoop
extends SingleThreadEventLoop {
    private final ThreadPerChannelEventLoopGroup parent;
    private Channel ch;

    @Override
    public ChannelFuture register(Channel channel, ChannelPromise channelPromise) {
        return super.register(channel, channelPromise).addListener(new ChannelFutureListener(){

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    ThreadPerChannelEventLoop.this.ch = channelFuture.channel();
                } else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }

    @Override
    protected void run() {
        while (true) {
            Runnable runnable;
            if ((runnable = this.takeTask()) != null) {
                runnable.run();
                this.updateLastExecutionTime();
            }
            Channel channel = this.ch;
            if (this.isShuttingDown()) {
                if (channel != null) {
                    channel.unsafe().close(channel.unsafe().voidPromise());
                }
                if (!this.confirmShutdown()) continue;
                break;
            }
            if (channel == null || channel.isRegistered()) continue;
            this.runAllTasks();
            this.deregister();
        }
    }

    protected void deregister() {
        this.ch = null;
        this.parent.activeChildren.remove(this);
        this.parent.idleChildren.add(this);
    }

    public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup threadPerChannelEventLoopGroup) {
        super(threadPerChannelEventLoopGroup, threadPerChannelEventLoopGroup.threadFactory, true);
        this.parent = threadPerChannelEventLoopGroup;
    }
}

