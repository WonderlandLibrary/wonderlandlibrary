/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.udt;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface UdtChannelConfig
extends ChannelConfig {
    public int getSendBufferSize();

    @Override
    public UdtChannelConfig setMaxMessagesPerRead(int var1);

    public UdtChannelConfig setSendBufferSize(int var1);

    public UdtChannelConfig setReceiveBufferSize(int var1);

    public UdtChannelConfig setProtocolReceiveBufferSize(int var1);

    @Override
    public UdtChannelConfig setWriteBufferLowWaterMark(int var1);

    public int getSystemSendBufferSize();

    @Override
    public UdtChannelConfig setAutoRead(boolean var1);

    public int getSystemReceiveBufferSize();

    @Override
    public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1);

    public int getSoLinger();

    public UdtChannelConfig setSystemReceiveBufferSize(int var1);

    @Override
    public UdtChannelConfig setConnectTimeoutMillis(int var1);

    public int getReceiveBufferSize();

    public UdtChannelConfig setSystemSendBufferSize(int var1);

    @Override
    public UdtChannelConfig setWriteSpinCount(int var1);

    @Override
    public UdtChannelConfig setWriteBufferHighWaterMark(int var1);

    public int getProtocolReceiveBufferSize();

    public int getProtocolSendBufferSize();

    public UdtChannelConfig setSoLinger(int var1);

    @Override
    public UdtChannelConfig setAllocator(ByteBufAllocator var1);

    public UdtChannelConfig setReuseAddress(boolean var1);

    public boolean isReuseAddress();

    @Override
    public UdtChannelConfig setAutoClose(boolean var1);

    @Override
    public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1);

    public UdtChannelConfig setProtocolSendBufferSize(int var1);
}

