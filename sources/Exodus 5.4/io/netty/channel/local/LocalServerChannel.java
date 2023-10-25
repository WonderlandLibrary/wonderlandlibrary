/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.local;

import io.netty.channel.AbstractServerChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalChannelRegistry;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;

public class LocalServerChannel
extends AbstractServerChannel {
    private volatile int state;
    private volatile LocalAddress localAddress;
    private final Runnable shutdownHook;
    private volatile boolean acceptInProgress;
    private final ChannelConfig config = new DefaultChannelConfig(this);
    private final Queue<Object> inboundBuffer = new ArrayDeque<Object>();

    @Override
    protected SocketAddress localAddress0() {
        return this.localAddress;
    }

    @Override
    protected boolean isCompatible(EventLoop eventLoop) {
        return eventLoop instanceof SingleThreadEventLoop;
    }

    @Override
    public ChannelConfig config() {
        return this.config;
    }

    @Override
    protected void doBeginRead() throws Exception {
        Object object;
        if (this.acceptInProgress) {
            return;
        }
        Queue<Object> queue = this.inboundBuffer;
        if (queue.isEmpty()) {
            this.acceptInProgress = true;
            return;
        }
        ChannelPipeline channelPipeline = this.pipeline();
        while ((object = queue.poll()) != null) {
            channelPipeline.fireChannelRead(object);
        }
        channelPipeline.fireChannelReadComplete();
    }

    @Override
    protected void doBind(SocketAddress socketAddress) throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, socketAddress);
        this.state = 1;
    }

    @Override
    public boolean isOpen() {
        return this.state < 2;
    }

    public LocalServerChannel() {
        this.shutdownHook = new Runnable(){

            @Override
            public void run() {
                LocalServerChannel.this.unsafe().close(LocalServerChannel.this.unsafe().voidPromise());
            }
        };
    }

    private void serve0(LocalChannel localChannel) {
        this.inboundBuffer.add(localChannel);
        if (this.acceptInProgress) {
            Object object;
            this.acceptInProgress = false;
            ChannelPipeline channelPipeline = this.pipeline();
            while ((object = this.inboundBuffer.poll()) != null) {
                channelPipeline.fireChannelRead(object);
            }
            channelPipeline.fireChannelReadComplete();
        }
    }

    @Override
    protected void doRegister() throws Exception {
        ((SingleThreadEventExecutor)((Object)this.eventLoop())).addShutdownHook(this.shutdownHook);
    }

    @Override
    public LocalAddress localAddress() {
        return (LocalAddress)super.localAddress();
    }

    @Override
    public boolean isActive() {
        return this.state == 1;
    }

    @Override
    public LocalAddress remoteAddress() {
        return (LocalAddress)super.remoteAddress();
    }

    @Override
    protected void doDeregister() throws Exception {
        ((SingleThreadEventExecutor)((Object)this.eventLoop())).removeShutdownHook(this.shutdownHook);
    }

    @Override
    protected void doClose() throws Exception {
        if (this.state <= 1) {
            if (this.localAddress != null) {
                LocalChannelRegistry.unregister(this.localAddress);
                this.localAddress = null;
            }
            this.state = 2;
        }
    }

    LocalChannel serve(LocalChannel localChannel) {
        final LocalChannel localChannel2 = new LocalChannel(this, localChannel);
        if (this.eventLoop().inEventLoop()) {
            this.serve0(localChannel2);
        } else {
            this.eventLoop().execute(new Runnable(){

                @Override
                public void run() {
                    LocalServerChannel.this.serve0(localChannel2);
                }
            });
        }
        return localChannel2;
    }
}

