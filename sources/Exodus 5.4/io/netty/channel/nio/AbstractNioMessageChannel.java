/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.util.concurrent.AbstractEventExecutor;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNioMessageChannel
extends AbstractNioChannel {
    protected abstract boolean doWriteMessage(Object var1, ChannelOutboundBuffer var2) throws Exception;

    protected abstract int doReadMessages(List<Object> var1) throws Exception;

    @Override
    protected AbstractNioChannel.AbstractNioUnsafe newUnsafe() {
        return new NioMessageUnsafe();
    }

    @Override
    protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {
        SelectionKey selectionKey = this.selectionKey();
        int n = selectionKey.interestOps();
        while (true) {
            Object object;
            if ((object = channelOutboundBuffer.current()) == null) {
                if ((n & 4) == 0) break;
                selectionKey.interestOps(n & 0xFFFFFFFB);
                break;
            }
            try {
                boolean bl = false;
                for (int i = this.config().getWriteSpinCount() - 1; i >= 0; --i) {
                    if (!this.doWriteMessage(object, channelOutboundBuffer)) continue;
                    bl = true;
                    break;
                }
                if (bl) {
                    channelOutboundBuffer.remove();
                    continue;
                }
                if ((n & 4) != 0) break;
                selectionKey.interestOps(n | 4);
            }
            catch (IOException iOException) {
                if (this.continueOnWriteError()) {
                    channelOutboundBuffer.remove(iOException);
                    continue;
                }
                throw iOException;
            }
            break;
        }
    }

    protected boolean continueOnWriteError() {
        return false;
    }

    protected AbstractNioMessageChannel(Channel channel, SelectableChannel selectableChannel, int n) {
        super(channel, selectableChannel, n);
    }

    private final class NioMessageUnsafe
    extends AbstractNioChannel.AbstractNioUnsafe {
        private final List<Object> readBuf;

        private NioMessageUnsafe() {
            super(AbstractNioMessageChannel.this);
            this.readBuf = new ArrayList<Object>();
        }

        @Override
        public void read() {
            block12: {
                int n;
                assert (((AbstractEventExecutor)((Object)AbstractNioMessageChannel.this.eventLoop())).inEventLoop());
                ChannelConfig channelConfig = AbstractNioMessageChannel.this.config();
                if (!channelConfig.isAutoRead() && !AbstractNioMessageChannel.this.isReadPending()) {
                    this.removeReadOp();
                    return;
                }
                int n2 = channelConfig.getMaxMessagesPerRead();
                ChannelPipeline channelPipeline = AbstractNioMessageChannel.this.pipeline();
                boolean bl = false;
                Throwable throwable = null;
                try {
                    while ((n = AbstractNioMessageChannel.this.doReadMessages(this.readBuf)) != 0) {
                        if (n < 0) {
                            bl = true;
                        } else if (channelConfig.isAutoRead() && this.readBuf.size() < n2) continue;
                        break;
                    }
                }
                catch (Throwable throwable2) {
                    throwable = throwable2;
                }
                AbstractNioMessageChannel.this.setReadPending(false);
                n = this.readBuf.size();
                for (int i = 0; i < n; ++i) {
                    channelPipeline.fireChannelRead(this.readBuf.get(i));
                }
                this.readBuf.clear();
                channelPipeline.fireChannelReadComplete();
                if (throwable != null) {
                    if (throwable instanceof IOException) {
                        bl = !(AbstractNioMessageChannel.this instanceof ServerChannel);
                    }
                    channelPipeline.fireExceptionCaught(throwable);
                }
                if (bl && AbstractNioMessageChannel.this.isOpen()) {
                    this.close(this.voidPromise());
                }
                if (channelConfig.isAutoRead() || AbstractNioMessageChannel.this.isReadPending()) break block12;
                this.removeReadOp();
            }
        }
    }
}

