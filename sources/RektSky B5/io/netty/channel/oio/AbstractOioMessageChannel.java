/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.oio.AbstractOioChannel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOioMessageChannel
extends AbstractOioChannel {
    private final List<Object> readBuf = new ArrayList<Object>();

    protected AbstractOioMessageChannel(Channel parent) {
        super(parent);
    }

    @Override
    protected void doRead() {
        ChannelConfig config = this.config();
        ChannelPipeline pipeline = this.pipeline();
        boolean closed = false;
        int maxMessagesPerRead = config.getMaxMessagesPerRead();
        Throwable exception = null;
        int localRead = 0;
        try {
            while ((localRead = this.doReadMessages(this.readBuf)) != 0) {
                if (localRead < 0) {
                    closed = true;
                } else if (this.readBuf.size() < maxMessagesPerRead && config.isAutoRead()) continue;
                break;
            }
        }
        catch (Throwable t2) {
            exception = t2;
        }
        int size = this.readBuf.size();
        for (int i2 = 0; i2 < size; ++i2) {
            pipeline.fireChannelRead(this.readBuf.get(i2));
        }
        this.readBuf.clear();
        pipeline.fireChannelReadComplete();
        if (exception != null) {
            if (exception instanceof IOException) {
                closed = true;
            }
            this.pipeline().fireExceptionCaught(exception);
        }
        if (closed) {
            if (this.isOpen()) {
                this.unsafe().close(this.unsafe().voidPromise());
            }
        } else if (localRead == 0 && this.isActive()) {
            this.read();
        }
    }

    protected abstract int doReadMessages(List<Object> var1) throws Exception;
}

