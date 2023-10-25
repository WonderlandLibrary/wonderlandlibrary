/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.AbstractNioByteChannel;
import java.util.IdentityHashMap;
import java.util.Map;

public class DefaultChannelConfig
implements ChannelConfig {
    private volatile RecvByteBufAllocator rcvBufAllocator;
    private volatile int writeSpinCount = 16;
    protected final Channel channel;
    private volatile int writeBufferHighWaterMark = 65536;
    private volatile ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;
    private static final RecvByteBufAllocator DEFAULT_RCVBUF_ALLOCATOR = AdaptiveRecvByteBufAllocator.DEFAULT;
    private volatile int maxMessagesPerRead;
    private volatile int connectTimeoutMillis = 30000;
    private volatile int writeBufferLowWaterMark = 32768;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    private volatile boolean autoClose = true;
    private volatile MessageSizeEstimator msgSizeEstimator;
    private static final MessageSizeEstimator DEFAULT_MSG_SIZE_ESTIMATOR = DefaultMessageSizeEstimator.DEFAULT;
    private volatile boolean autoRead = true;

    protected Map<ChannelOption<?>, Object> getOptions(Map<ChannelOption<?>, Object> map, ChannelOption<?> ... channelOptionArray) {
        if (map == null) {
            map = new IdentityHashMap();
        }
        for (ChannelOption<?> channelOption : channelOptionArray) {
            map.put(channelOption, this.getOption(channelOption));
        }
        return map;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == null) {
            throw new NullPointerException("option");
        }
        if (channelOption == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            return (T)Integer.valueOf(this.getConnectTimeoutMillis());
        }
        if (channelOption == ChannelOption.MAX_MESSAGES_PER_READ) {
            return (T)Integer.valueOf(this.getMaxMessagesPerRead());
        }
        if (channelOption == ChannelOption.WRITE_SPIN_COUNT) {
            return (T)Integer.valueOf(this.getWriteSpinCount());
        }
        if (channelOption == ChannelOption.ALLOCATOR) {
            return (T)this.getAllocator();
        }
        if (channelOption == ChannelOption.RCVBUF_ALLOCATOR) {
            return (T)this.getRecvByteBufAllocator();
        }
        if (channelOption == ChannelOption.AUTO_READ) {
            return (T)Boolean.valueOf(this.isAutoRead());
        }
        if (channelOption == ChannelOption.AUTO_CLOSE) {
            return (T)Boolean.valueOf(this.isAutoClose());
        }
        if (channelOption == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferHighWaterMark());
        }
        if (channelOption == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            return (T)Integer.valueOf(this.getWriteBufferLowWaterMark());
        }
        if (channelOption == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            return (T)this.getMessageSizeEstimator();
        }
        return null;
    }

    @Override
    public boolean isAutoClose() {
        return this.autoClose;
    }

    @Override
    public ChannelConfig setWriteBufferLowWaterMark(int n) {
        if (n > this.getWriteBufferHighWaterMark()) {
            throw new IllegalArgumentException("writeBufferLowWaterMark cannot be greater than writeBufferHighWaterMark (" + this.getWriteBufferHighWaterMark() + "): " + n);
        }
        if (n < 0) {
            throw new IllegalArgumentException("writeBufferLowWaterMark must be >= 0");
        }
        this.writeBufferLowWaterMark = n;
        return this;
    }

    @Override
    public ChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        if (byteBufAllocator == null) {
            throw new NullPointerException("allocator");
        }
        this.allocator = byteBufAllocator;
        return this;
    }

    protected void autoReadCleared() {
    }

    @Override
    public RecvByteBufAllocator getRecvByteBufAllocator() {
        return this.rcvBufAllocator;
    }

    @Override
    public int getConnectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }

    @Override
    public boolean isAutoRead() {
        return this.autoRead;
    }

    @Override
    public int getWriteBufferHighWaterMark() {
        return this.writeBufferHighWaterMark;
    }

    @Override
    public ChannelConfig setWriteSpinCount(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("writeSpinCount must be a positive integer.");
        }
        this.writeSpinCount = n;
        return this;
    }

    @Override
    public ChannelConfig setConnectTimeoutMillis(int n) {
        if (n < 0) {
            throw new IllegalArgumentException(String.format("connectTimeoutMillis: %d (expected: >= 0)", n));
        }
        this.connectTimeoutMillis = n;
        return this;
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == ChannelOption.CONNECT_TIMEOUT_MILLIS) {
            this.setConnectTimeoutMillis((Integer)t);
        } else if (channelOption == ChannelOption.MAX_MESSAGES_PER_READ) {
            this.setMaxMessagesPerRead((Integer)t);
        } else if (channelOption == ChannelOption.WRITE_SPIN_COUNT) {
            this.setWriteSpinCount((Integer)t);
        } else if (channelOption == ChannelOption.ALLOCATOR) {
            this.setAllocator((ByteBufAllocator)t);
        } else if (channelOption == ChannelOption.RCVBUF_ALLOCATOR) {
            this.setRecvByteBufAllocator((RecvByteBufAllocator)t);
        } else if (channelOption == ChannelOption.AUTO_READ) {
            this.setAutoRead((Boolean)t);
        } else if (channelOption == ChannelOption.AUTO_CLOSE) {
            this.setAutoClose((Boolean)t);
        } else if (channelOption == ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK) {
            this.setWriteBufferHighWaterMark((Integer)t);
        } else if (channelOption == ChannelOption.WRITE_BUFFER_LOW_WATER_MARK) {
            this.setWriteBufferLowWaterMark((Integer)t);
        } else if (channelOption == ChannelOption.MESSAGE_SIZE_ESTIMATOR) {
            this.setMessageSizeEstimator((MessageSizeEstimator)t);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int getMaxMessagesPerRead() {
        return this.maxMessagesPerRead;
    }

    @Override
    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }

    @Override
    public int getWriteBufferLowWaterMark() {
        return this.writeBufferLowWaterMark;
    }

    public DefaultChannelConfig(Channel channel) {
        this.rcvBufAllocator = DEFAULT_RCVBUF_ALLOCATOR;
        this.msgSizeEstimator = DEFAULT_MSG_SIZE_ESTIMATOR;
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        this.maxMessagesPerRead = channel instanceof ServerChannel || channel instanceof AbstractNioByteChannel ? 16 : 1;
    }

    protected <T> void validate(ChannelOption<T> channelOption, T t) {
        if (channelOption == null) {
            throw new NullPointerException("option");
        }
        channelOption.validate(t);
    }

    @Override
    public ChannelConfig setAutoClose(boolean bl) {
        this.autoClose = bl;
        return this;
    }

    @Override
    public boolean setOptions(Map<ChannelOption<?>, ?> map) {
        if (map == null) {
            throw new NullPointerException("options");
        }
        boolean bl = true;
        for (Map.Entry<ChannelOption<?>, ?> entry : map.entrySet()) {
            if (this.setOption(entry.getKey(), entry.getValue())) continue;
            bl = false;
        }
        return bl;
    }

    @Override
    public ChannelConfig setWriteBufferHighWaterMark(int n) {
        if (n < this.getWriteBufferLowWaterMark()) {
            throw new IllegalArgumentException("writeBufferHighWaterMark cannot be less than writeBufferLowWaterMark (" + this.getWriteBufferLowWaterMark() + "): " + n);
        }
        if (n < 0) {
            throw new IllegalArgumentException("writeBufferHighWaterMark must be >= 0");
        }
        this.writeBufferHighWaterMark = n;
        return this;
    }

    @Override
    public ChannelConfig setAutoRead(boolean bl) {
        boolean bl2 = this.autoRead;
        this.autoRead = bl;
        if (bl && !bl2) {
            this.channel.read();
        } else if (!bl && bl2) {
            this.autoReadCleared();
        }
        return this;
    }

    @Override
    public ChannelConfig setMaxMessagesPerRead(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxMessagesPerRead: " + n + " (expected: > 0)");
        }
        this.maxMessagesPerRead = n;
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(null, ChannelOption.CONNECT_TIMEOUT_MILLIS, ChannelOption.MAX_MESSAGES_PER_READ, ChannelOption.WRITE_SPIN_COUNT, ChannelOption.ALLOCATOR, ChannelOption.AUTO_READ, ChannelOption.AUTO_CLOSE, ChannelOption.RCVBUF_ALLOCATOR, ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, ChannelOption.MESSAGE_SIZE_ESTIMATOR);
    }

    @Override
    public int getWriteSpinCount() {
        return this.writeSpinCount;
    }

    @Override
    public MessageSizeEstimator getMessageSizeEstimator() {
        return this.msgSizeEstimator;
    }

    @Override
    public ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        if (recvByteBufAllocator == null) {
            throw new NullPointerException("allocator");
        }
        this.rcvBufAllocator = recvByteBufAllocator;
        return this;
    }

    @Override
    public ChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        if (messageSizeEstimator == null) {
            throw new NullPointerException("estimator");
        }
        this.msgSizeEstimator = messageSizeEstimator;
        return this;
    }
}

