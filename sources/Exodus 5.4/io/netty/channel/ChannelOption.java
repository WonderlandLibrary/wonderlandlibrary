/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.UniqueName;
import io.netty.util.internal.PlatformDependent;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ConcurrentMap;

public class ChannelOption<T>
extends UniqueName {
    public static final ChannelOption<Boolean> SO_REUSEADDR;
    public static final ChannelOption<Integer> WRITE_BUFFER_LOW_WATER_MARK;
    @Deprecated
    public static final ChannelOption<Boolean> AUTO_CLOSE;
    @Deprecated
    public static final ChannelOption<Long> AIO_WRITE_TIMEOUT;
    public static final ChannelOption<Integer> SO_RCVBUF;
    public static final ChannelOption<Integer> WRITE_SPIN_COUNT;
    public static final ChannelOption<Integer> WRITE_BUFFER_HIGH_WATER_MARK;
    public static final ChannelOption<Integer> SO_SNDBUF;
    public static final ChannelOption<MessageSizeEstimator> MESSAGE_SIZE_ESTIMATOR;
    public static final ChannelOption<Integer> MAX_MESSAGES_PER_READ;
    public static final ChannelOption<Boolean> ALLOW_HALF_CLOSURE;
    public static final ChannelOption<Boolean> AUTO_READ;
    @Deprecated
    public static final ChannelOption<Long> AIO_READ_TIMEOUT;
    public static final ChannelOption<Integer> IP_TOS;
    private static final ConcurrentMap<String, Boolean> names;
    public static final ChannelOption<InetAddress> IP_MULTICAST_ADDR;
    public static final ChannelOption<Integer> SO_LINGER;
    public static final ChannelOption<Boolean> SO_KEEPALIVE;
    public static final ChannelOption<ByteBufAllocator> ALLOCATOR;
    public static final ChannelOption<Integer> IP_MULTICAST_TTL;
    public static final ChannelOption<Boolean> TCP_NODELAY;
    public static final ChannelOption<Integer> CONNECT_TIMEOUT_MILLIS;
    public static final ChannelOption<Integer> SO_TIMEOUT;
    @Deprecated
    public static final ChannelOption<Boolean> DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION;
    public static final ChannelOption<NetworkInterface> IP_MULTICAST_IF;
    public static final ChannelOption<Boolean> SO_BROADCAST;
    public static final ChannelOption<RecvByteBufAllocator> RCVBUF_ALLOCATOR;
    public static final ChannelOption<Integer> SO_BACKLOG;
    public static final ChannelOption<Boolean> IP_MULTICAST_LOOP_DISABLED;

    static {
        names = PlatformDependent.newConcurrentHashMap();
        ALLOCATOR = ChannelOption.valueOf("ALLOCATOR");
        RCVBUF_ALLOCATOR = ChannelOption.valueOf("RCVBUF_ALLOCATOR");
        MESSAGE_SIZE_ESTIMATOR = ChannelOption.valueOf("MESSAGE_SIZE_ESTIMATOR");
        CONNECT_TIMEOUT_MILLIS = ChannelOption.valueOf("CONNECT_TIMEOUT_MILLIS");
        MAX_MESSAGES_PER_READ = ChannelOption.valueOf("MAX_MESSAGES_PER_READ");
        WRITE_SPIN_COUNT = ChannelOption.valueOf("WRITE_SPIN_COUNT");
        WRITE_BUFFER_HIGH_WATER_MARK = ChannelOption.valueOf("WRITE_BUFFER_HIGH_WATER_MARK");
        WRITE_BUFFER_LOW_WATER_MARK = ChannelOption.valueOf("WRITE_BUFFER_LOW_WATER_MARK");
        ALLOW_HALF_CLOSURE = ChannelOption.valueOf("ALLOW_HALF_CLOSURE");
        AUTO_READ = ChannelOption.valueOf("AUTO_READ");
        AUTO_CLOSE = ChannelOption.valueOf("AUTO_CLOSE");
        SO_BROADCAST = ChannelOption.valueOf("SO_BROADCAST");
        SO_KEEPALIVE = ChannelOption.valueOf("SO_KEEPALIVE");
        SO_SNDBUF = ChannelOption.valueOf("SO_SNDBUF");
        SO_RCVBUF = ChannelOption.valueOf("SO_RCVBUF");
        SO_REUSEADDR = ChannelOption.valueOf("SO_REUSEADDR");
        SO_LINGER = ChannelOption.valueOf("SO_LINGER");
        SO_BACKLOG = ChannelOption.valueOf("SO_BACKLOG");
        SO_TIMEOUT = ChannelOption.valueOf("SO_TIMEOUT");
        IP_TOS = ChannelOption.valueOf("IP_TOS");
        IP_MULTICAST_ADDR = ChannelOption.valueOf("IP_MULTICAST_ADDR");
        IP_MULTICAST_IF = ChannelOption.valueOf("IP_MULTICAST_IF");
        IP_MULTICAST_TTL = ChannelOption.valueOf("IP_MULTICAST_TTL");
        IP_MULTICAST_LOOP_DISABLED = ChannelOption.valueOf("IP_MULTICAST_LOOP_DISABLED");
        TCP_NODELAY = ChannelOption.valueOf("TCP_NODELAY");
        AIO_READ_TIMEOUT = ChannelOption.valueOf("AIO_READ_TIMEOUT");
        AIO_WRITE_TIMEOUT = ChannelOption.valueOf("AIO_WRITE_TIMEOUT");
        DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION = ChannelOption.valueOf("DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION");
    }

    public void validate(T t) {
        if (t == null) {
            throw new NullPointerException("value");
        }
    }

    @Deprecated
    protected ChannelOption(String string) {
        super(names, string, new Object[0]);
    }

    public static <T> ChannelOption<T> valueOf(String string) {
        return new ChannelOption<T>(string);
    }
}

