/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.sctp;

import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface SctpChannelConfig
extends ChannelConfig {
    @Override
    public SctpChannelConfig setMaxMessagesPerRead(int var1);

    public SctpChannelConfig setReceiveBufferSize(int var1);

    @Override
    public SctpChannelConfig setWriteBufferLowWaterMark(int var1);

    @Override
    public SctpChannelConfig setConnectTimeoutMillis(int var1);

    @Override
    public SctpChannelConfig setWriteBufferHighWaterMark(int var1);

    public SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();

    @Override
    public SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1);

    public boolean isSctpNoDelay();

    public int getSendBufferSize();

    public int getReceiveBufferSize();

    @Override
    public SctpChannelConfig setAutoClose(boolean var1);

    @Override
    public SctpChannelConfig setWriteSpinCount(int var1);

    public SctpChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams var1);

    @Override
    public SctpChannelConfig setAutoRead(boolean var1);

    public SctpChannelConfig setSctpNoDelay(boolean var1);

    @Override
    public SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1);

    @Override
    public SctpChannelConfig setAllocator(ByteBufAllocator var1);

    public SctpChannelConfig setSendBufferSize(int var1);
}

