/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.barchart.udt.OptionUDT
 *  com.barchart.udt.SocketUDT
 *  com.barchart.udt.nio.ChannelUDT
 */
package io.netty.channel.udt;

import com.barchart.udt.OptionUDT;
import com.barchart.udt.SocketUDT;
import com.barchart.udt.nio.ChannelUDT;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.UdtChannelConfig;
import io.netty.channel.udt.UdtChannelOption;
import java.io.IOException;
import java.util.Map;

public class DefaultUdtChannelConfig
extends DefaultChannelConfig
implements UdtChannelConfig {
    private volatile int allocatorReceiveBufferSize = 131072;
    private static final int K = 1024;
    private volatile int protocolSendBuferSize = 0xA00000;
    private volatile int protocolReceiveBuferSize = 0xA00000;
    private volatile int systemSendBuferSize = 0x100000;
    private volatile int allocatorSendBufferSize = 131072;
    private volatile boolean reuseAddress = true;
    private static final int M = 0x100000;
    private volatile int soLinger;
    private volatile int systemReceiveBufferSize = 0x100000;

    @Override
    public int getSendBufferSize() {
        return this.allocatorSendBufferSize;
    }

    @Override
    public UdtChannelConfig setSystemSendBufferSize(int n) {
        this.systemReceiveBufferSize = n;
        return this;
    }

    @Override
    public UdtChannelConfig setSoLinger(int n) {
        this.soLinger = n;
        return this;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return this.getOptions(super.getOptions(), UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, UdtChannelOption.SO_RCVBUF, UdtChannelOption.SO_SNDBUF, UdtChannelOption.SO_REUSEADDR, UdtChannelOption.SO_LINGER);
    }

    @Override
    public UdtChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public int getReceiveBufferSize() {
        return this.allocatorReceiveBufferSize;
    }

    @Override
    public <T> T getOption(ChannelOption<T> channelOption) {
        if (channelOption == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getProtocolReceiveBufferSize());
        }
        if (channelOption == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getProtocolSendBufferSize());
        }
        if (channelOption == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getSystemReceiveBufferSize());
        }
        if (channelOption == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            return (T)Integer.valueOf(this.getSystemSendBufferSize());
        }
        if (channelOption == UdtChannelOption.SO_RCVBUF) {
            return (T)Integer.valueOf(this.getReceiveBufferSize());
        }
        if (channelOption == UdtChannelOption.SO_SNDBUF) {
            return (T)Integer.valueOf(this.getSendBufferSize());
        }
        if (channelOption == UdtChannelOption.SO_REUSEADDR) {
            return (T)Boolean.valueOf(this.isReuseAddress());
        }
        if (channelOption == UdtChannelOption.SO_LINGER) {
            return (T)Integer.valueOf(this.getSoLinger());
        }
        return super.getOption(channelOption);
    }

    @Override
    public UdtChannelConfig setReuseAddress(boolean bl) {
        this.reuseAddress = bl;
        return this;
    }

    @Override
    public UdtChannelConfig setAllocator(ByteBufAllocator byteBufAllocator) {
        super.setAllocator(byteBufAllocator);
        return this;
    }

    @Override
    public boolean isReuseAddress() {
        return this.reuseAddress;
    }

    @Override
    public int getProtocolReceiveBufferSize() {
        return this.protocolReceiveBuferSize;
    }

    @Override
    public int getSystemReceiveBufferSize() {
        return this.systemReceiveBufferSize;
    }

    @Override
    public int getSystemSendBufferSize() {
        return this.systemSendBuferSize;
    }

    @Override
    public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator recvByteBufAllocator) {
        super.setRecvByteBufAllocator(recvByteBufAllocator);
        return this;
    }

    @Override
    public UdtChannelConfig setAutoClose(boolean bl) {
        super.setAutoClose(bl);
        return this;
    }

    @Override
    public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator messageSizeEstimator) {
        super.setMessageSizeEstimator(messageSizeEstimator);
        return this;
    }

    protected void apply(ChannelUDT channelUDT) throws IOException {
        SocketUDT socketUDT = channelUDT.socketUDT();
        socketUDT.setReuseAddress(this.isReuseAddress());
        socketUDT.setSendBufferSize(this.getSendBufferSize());
        if (this.getSoLinger() <= 0) {
            socketUDT.setSoLinger(false, 0);
        } else {
            socketUDT.setSoLinger(true, this.getSoLinger());
        }
        socketUDT.setOption(OptionUDT.Protocol_Receive_Buffer_Size, (Object)this.getProtocolReceiveBufferSize());
        socketUDT.setOption(OptionUDT.Protocol_Send_Buffer_Size, (Object)this.getProtocolSendBufferSize());
        socketUDT.setOption(OptionUDT.System_Receive_Buffer_Size, (Object)this.getSystemReceiveBufferSize());
        socketUDT.setOption(OptionUDT.System_Send_Buffer_Size, (Object)this.getSystemSendBufferSize());
    }

    @Override
    public UdtChannelConfig setSystemReceiveBufferSize(int n) {
        this.systemSendBuferSize = n;
        return this;
    }

    @Override
    public UdtChannelConfig setReceiveBufferSize(int n) {
        this.allocatorReceiveBufferSize = n;
        return this;
    }

    @Override
    public UdtChannelConfig setWriteBufferHighWaterMark(int n) {
        super.setWriteBufferHighWaterMark(n);
        return this;
    }

    @Override
    public int getProtocolSendBufferSize() {
        return this.protocolSendBuferSize;
    }

    @Override
    public UdtChannelConfig setMaxMessagesPerRead(int n) {
        super.setMaxMessagesPerRead(n);
        return this;
    }

    @Override
    public UdtChannelConfig setConnectTimeoutMillis(int n) {
        super.setConnectTimeoutMillis(n);
        return this;
    }

    @Override
    public UdtChannelConfig setWriteBufferLowWaterMark(int n) {
        super.setWriteBufferLowWaterMark(n);
        return this;
    }

    public DefaultUdtChannelConfig(UdtChannel udtChannel, ChannelUDT channelUDT, boolean bl) throws IOException {
        super(udtChannel);
        if (bl) {
            this.apply(channelUDT);
        }
    }

    @Override
    public UdtChannelConfig setProtocolSendBufferSize(int n) {
        this.protocolSendBuferSize = n;
        return this;
    }

    @Override
    public int getSoLinger() {
        return this.soLinger;
    }

    @Override
    public UdtChannelConfig setSendBufferSize(int n) {
        this.allocatorSendBufferSize = n;
        return this;
    }

    @Override
    public UdtChannelConfig setWriteSpinCount(int n) {
        super.setWriteSpinCount(n);
        return this;
    }

    @Override
    public UdtChannelConfig setProtocolReceiveBufferSize(int n) {
        this.protocolReceiveBuferSize = n;
        return this;
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> channelOption, T t) {
        this.validate(channelOption, t);
        if (channelOption == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
            this.setProtocolReceiveBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
            this.setProtocolSendBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
            this.setSystemReceiveBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
            this.setSystemSendBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.SO_RCVBUF) {
            this.setReceiveBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.SO_SNDBUF) {
            this.setSendBufferSize((Integer)t);
        } else if (channelOption == UdtChannelOption.SO_REUSEADDR) {
            this.setReuseAddress((Boolean)t);
        } else if (channelOption == UdtChannelOption.SO_LINGER) {
            this.setSoLinger((Integer)t);
        } else {
            return super.setOption(channelOption, t);
        }
        return true;
    }
}

