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

    protected abstract int doReadMessages(List<Object> var1) throws Exception;

    protected AbstractOioMessageChannel(Channel channel) {
        super(channel);
    }

    @Override
    protected void doRead() {
        ChannelConfig channelConfig = this.config();
        ChannelPipeline channelPipeline = this.pipeline();
        boolean bl = false;
        int n = channelConfig.getMaxMessagesPerRead();
        Throwable throwable = null;
        int n2 = 0;
        try {
            while ((n2 = this.doReadMessages(this.readBuf)) != 0) {
                if (n2 < 0) {
                    bl = true;
                } else if (this.readBuf.size() < n && channelConfig.isAutoRead()) continue;
                break;
            }
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
        }
        int n3 = this.readBuf.size();
        for (int i = 0; i < n3; ++i) {
            channelPipeline.fireChannelRead(this.readBuf.get(i));
        }
        this.readBuf.clear();
        channelPipeline.fireChannelReadComplete();
        if (throwable != null) {
            if (throwable instanceof IOException) {
                bl = true;
            }
            this.pipeline().fireExceptionCaught(throwable);
        }
        if (bl) {
            if (this.isOpen()) {
                this.unsafe().close(this.unsafe().voidPromise());
            }
        } else if (n2 == 0 && this.isActive()) {
            this.read();
        }
    }
}

