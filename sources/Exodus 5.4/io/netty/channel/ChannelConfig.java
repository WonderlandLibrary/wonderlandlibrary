/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import java.util.Map;

public interface ChannelConfig {
    public MessageSizeEstimator getMessageSizeEstimator();

    public RecvByteBufAllocator getRecvByteBufAllocator();

    public Map<ChannelOption<?>, Object> getOptions();

    @Deprecated
    public ChannelConfig setAutoClose(boolean var1);

    public <T> boolean setOption(ChannelOption<T> var1, T var2);

    public boolean isAutoRead();

    public ChannelConfig setWriteSpinCount(int var1);

    public boolean setOptions(Map<ChannelOption<?>, ?> var1);

    public int getConnectTimeoutMillis();

    public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1);

    public ChannelConfig setAllocator(ByteBufAllocator var1);

    public ByteBufAllocator getAllocator();

    public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1);

    @Deprecated
    public boolean isAutoClose();

    public ChannelConfig setWriteBufferLowWaterMark(int var1);

    public ChannelConfig setWriteBufferHighWaterMark(int var1);

    public int getWriteBufferHighWaterMark();

    public int getWriteSpinCount();

    public int getMaxMessagesPerRead();

    public int getWriteBufferLowWaterMark();

    public <T> T getOption(ChannelOption<T> var1);

    public ChannelConfig setMaxMessagesPerRead(int var1);

    public ChannelConfig setConnectTimeoutMillis(int var1);

    public ChannelConfig setAutoRead(boolean var1);
}

