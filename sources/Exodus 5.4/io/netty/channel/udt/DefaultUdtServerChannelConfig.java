/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.barchart.udt.nio.ChannelUDT
 */
package io.netty.channel.udt;

import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.udt.DefaultUdtChannelConfig;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtServerChannelConfig;
import java.io.IOException;
import java.util.Map;

public class DefaultUdtServerChannelConfig
extends DefaultUdtChannelConfig
implements UdtServerChannelConfig {
    private volatile int backlog = 64;

    @Override
    public UdtServerChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    public DefaultUdtServerChannelConfig(UdtChannel udtChannel, ChannelUDT channelUDT, boolean bl) throws IOException {
        super(udtChannel, channelUDT, bl);
        if (bl) {
            this.apply(channelUDT);
        }
    }

    @Override
    protected void apply(ChannelUDT channelUDT) throws IOException {
    }

    @Override
    public UdtServerChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    @Override
    public UdtServerChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption != ChannelOption.SO_BACKLOG) {
            return super.setOption(channelOption, t);
        }
        this.setBacklog((Integer)t);
        return true;
    }

    @Override
    public UdtServerChannelConfig setSystemSendBufferSize(int n) {
        super.setSystemSendBufferSize(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setSystemReceiveBufferSize(int n) {
        super.setSystemReceiveBufferSize(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    @Override
    public UdtServerChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setReceiveBufferSize(int n) {
        super.setReceiveBufferSize(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setProtocolReceiveBufferSize(int n) {
        super.setProtocolReceiveBufferSize(n);
        return this;
    }

    @Override
    public int getBacklog() {
        return this.backlog;
    }

    @Override
    public UdtServerChannelConfig setBacklog(int n) {
        this.backlog = n;
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), ChannelOption.SO_BACKLOG);
    }

    @Override
    public UdtServerChannelConfig setProtocolSendBufferSize(int n) {
        super.setProtocolSendBufferSize(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setReuseAddress(boolean bl) {
        super.setReuseAddress(bl);
        return this;
    }

    @Override
    public UdtServerChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setSoLinger(int n) {
        super.setSoLinger(n);
        return this;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == ChannelOption.SO_BACKLOG) {
            return (T)Integer.valueOf(this.getBacklog());
        }
        return super.getOption(channelOption);
    }

    @Override
    public UdtServerChannelConfig setSendBufferSize(int n) {
        super.setSendBufferSize(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }
}

