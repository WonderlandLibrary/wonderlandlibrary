/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.socket.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.util.Enumeration;

class NioDatagramChannelConfig
extends DefaultDatagramChannelConfig {
    private static final Object IP_MULTICAST_IF;
    private static final Method SET_OPTION;
    private static final Method GET_OPTION;
    private final DatagramChannel javaChannel;
    private static final Object IP_MULTICAST_TTL;
    private static final Object IP_MULTICAST_LOOP;

    @Override
    protected void autoReadCleared() {
        ((NioDatagramChannel)this.channel).setReadPending(false);
    }

    @Override
    public DatagramChannelConfig setAutoRead(boolean bl) {
        super.setAutoRead(bl);
        return this;
    }

    @Override
    public boolean isLoopbackModeDisabled() {
        return (Boolean)this.getOption0(IP_MULTICAST_LOOP);
    }

    @Override
    public NetworkInterface getNetworkInterface() {
        return (NetworkInterface)this.getOption0(IP_MULTICAST_IF);
    }

    @Override
    public DatagramChannelConfig setLoopbackModeDisabled(boolean bl) {
        this.setOption0(IP_MULTICAST_LOOP, bl);
        return this;
    }

    @Override
    public DatagramChannelConfig setTimeToLive(int n) {
        this.setOption0(IP_MULTICAST_TTL, n);
        return this;
    }

    @Override
    public DatagramChannelConfig setInterface(InetAddress inetAddress) {
        try {
            this.setNetworkInterface(NetworkInterface.getByInetAddress(inetAddress));
        }
        catch (SocketException socketException) {
            throw new ChannelException(socketException);
        }
        return this;
    }

    @Override
    public int getTimeToLive() {
        return (Integer)this.getOption0(IP_MULTICAST_TTL);
    }

    static {
        ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
        Class<?> clazz = null;
        try {
            clazz = Class.forName("java.net.SocketOption", true, classLoader);
        }
        catch (Exception exception) {
            // empty catch block
        }
        Class<?> clazz2 = null;
        try {
            clazz2 = Class.forName("java.net.StandardSocketOptions", true, classLoader);
        }
        catch (Exception exception) {
            // empty catch block
        }
        Object object = null;
        Object object2 = null;
        Object object3 = null;
        Method method = null;
        Method method2 = null;
        if (clazz != null) {
            try {
                object = clazz2.getDeclaredField("IP_MULTICAST_TTL").get(null);
            }
            catch (Exception exception) {
                throw new Error("cannot locate the IP_MULTICAST_TTL field", exception);
            }
            try {
                object2 = clazz2.getDeclaredField("IP_MULTICAST_IF").get(null);
            }
            catch (Exception exception) {
                throw new Error("cannot locate the IP_MULTICAST_IF field", exception);
            }
            try {
                object3 = clazz2.getDeclaredField("IP_MULTICAST_LOOP").get(null);
            }
            catch (Exception exception) {
                throw new Error("cannot locate the IP_MULTICAST_LOOP field", exception);
            }
            try {
                method = NetworkChannel.class.getDeclaredMethod("getOption", clazz);
            }
            catch (Exception exception) {
                throw new Error("cannot locate the getOption() method", exception);
            }
            try {
                method2 = NetworkChannel.class.getDeclaredMethod("setOption", clazz, Object.class);
            }
            catch (Exception exception) {
                throw new Error("cannot locate the setOption() method", exception);
            }
        }
        IP_MULTICAST_TTL = object;
        IP_MULTICAST_IF = object2;
        IP_MULTICAST_LOOP = object3;
        GET_OPTION = method;
        SET_OPTION = method2;
    }

    private Object getOption0(Object object) {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        }
        try {
            return GET_OPTION.invoke(this.javaChannel, object);
        }
        catch (Exception exception) {
            throw new ChannelException(exception);
        }
    }

    NioDatagramChannelConfig(NioDatagramChannel nioDatagramChannel, DatagramChannel datagramChannel) {
        super(nioDatagramChannel, datagramChannel.socket());
        this.javaChannel = datagramChannel;
    }

    private void setOption0(Object object, Object object2) {
        if (PlatformDependent.javaVersion() < 7) {
            throw new UnsupportedOperationException();
        }
        try {
            SET_OPTION.invoke(this.javaChannel, object, object2);
        }
        catch (Exception exception) {
            throw new ChannelException(exception);
        }
    }

    @Override
    public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface) {
        this.setOption0(IP_MULTICAST_IF, networkInterface);
        return this;
    }

    @Override
    public InetAddress getInterface() {
        NetworkInterface networkInterface = this.getNetworkInterface();
        if (networkInterface == null) {
            return null;
        }
        Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses();
        if (enumeration.hasMoreElements()) {
            return enumeration.nextElement();
        }
        return null;
    }
}

