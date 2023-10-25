/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.rxtx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxChannelOption;
import java.util.Map;

final class DefaultRxtxChannelConfig
extends DefaultChannelConfig
implements RxtxChannelConfig {
    private volatile RxtxChannelConfig.Paritybit paritybit;
    private volatile int waitTime;
    private volatile boolean dtr;
    private volatile int readTimeout = 1000;
    private volatile RxtxChannelConfig.Databits databits;
    private volatile int baudrate = 115200;
    private volatile boolean rts;
    private volatile RxtxChannelConfig.Stopbits stopbits = RxtxChannelConfig.Stopbits.STOPBITS_1;

    @Override
    public RxtxChannelConfig.Databits getDatabits() {
        return this.databits;
    }

    @Override
    public RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits stopbits) {
        this.stopbits = stopbits;
        return this;
    }

    @Override
    public RxtxChannelConfig setBaudrate(int n) {
        this.baudrate = n;
        return this;
    }

    @Override
    public boolean isDtr() {
        return this.dtr;
    }

    @Override
    public RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    @Override
    public RxtxChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public RxtxChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public boolean isRts() {
        return this.rts;
    }

    @Override
    public RxtxChannelConfig setDtr(boolean bl) {
        this.dtr = bl;
        return this;
    }

    @Override
    public RxtxChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    @Override
    public RxtxChannelConfig setWaitTimeMillis(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Wait time must be >= 0");
        }
        this.waitTime = n;
        return this;
    }

    @Override
    public RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit paritybit) {
        this.paritybit = paritybit;
        return this;
    }

    @Override
    public RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits databits) {
        this.databits = databits;
        return this;
    }

    @Override
    public RxtxChannelConfig setReadTimeout(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("readTime must be >= 0");
        }
        this.readTimeout = n;
        return this;
    }

    @Override
    public RxtxChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    DefaultRxtxChannelConfig(RxtxChannel rxtxChannel) {
        super(rxtxChannel);
        this.databits = RxtxChannelConfig.Databits.DATABITS_8;
        this.paritybit = RxtxChannelConfig.Paritybit.NONE;
    }

    @Override
    public RxtxChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == RxtxChannelOption.BAUD_RATE) {
            this.setBaudrate((Integer)t);
        } else if (channelOption == RxtxChannelOption.DTR) {
            this.setDtr((Boolean)t);
        } else if (channelOption == RxtxChannelOption.RTS) {
            this.setRts((Boolean)t);
        } else if (channelOption == RxtxChannelOption.STOP_BITS) {
            this.setStopbits((RxtxChannelConfig.Stopbits)((Object)t));
        } else if (channelOption == RxtxChannelOption.DATA_BITS) {
            this.setDatabits((RxtxChannelConfig.Databits)((Object)t));
        } else if (channelOption == RxtxChannelOption.PARITY_BIT) {
            this.setParitybit((RxtxChannelConfig.Paritybit)((Object)t));
        } else if (channelOption == RxtxChannelOption.WAIT_TIME) {
            this.setWaitTimeMillis((Integer)t);
        } else if (channelOption == RxtxChannelOption.READ_TIMEOUT) {
            this.setReadTimeout((Integer)t);
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == RxtxChannelOption.BAUD_RATE) {
            return (T)Integer.valueOf(this.getBaudrate());
        }
        if (channelOption == RxtxChannelOption.DTR) {
            return (T)Boolean.valueOf(this.isDtr());
        }
        if (channelOption == RxtxChannelOption.RTS) {
            return (T)Boolean.valueOf(this.isRts());
        }
        if (channelOption == RxtxChannelOption.STOP_BITS) {
            return (T)((Object)this.getStopbits());
        }
        if (channelOption == RxtxChannelOption.DATA_BITS) {
            return (T)((Object)this.getDatabits());
        }
        if (channelOption == RxtxChannelOption.PARITY_BIT) {
            return (T)((Object)this.getParitybit());
        }
        if (channelOption == RxtxChannelOption.WAIT_TIME) {
            return (T)Integer.valueOf(this.getWaitTimeMillis());
        }
        if (channelOption == RxtxChannelOption.READ_TIMEOUT) {
            return (T)Integer.valueOf(this.getReadTimeout());
        }
        return super.getOption(channelOption);
    }

    @Override
    public RxtxChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    @Override
    public RxtxChannelConfig.Stopbits getStopbits() {
        return this.stopbits;
    }

    @Override
    public RxtxChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public RxtxChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public RxtxChannelConfig.Paritybit getParitybit() {
        return this.paritybit;
    }

    @Override
    public RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    @Override
    public int getWaitTimeMillis() {
        return this.waitTime;
    }

    @Override
    public int getBaudrate() {
        return this.baudrate;
    }

    @Override
    public int getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public RxtxChannelConfig setRts(boolean bl) {
        this.rts = bl;
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME);
    }
}

