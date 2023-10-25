/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.local;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.local.LocalAddress;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentMap;

final class LocalChannelRegistry {
    private static final ConcurrentMap<LocalAddress, Channel> boundChannels = PlatformDependent.newConcurrentHashMap();

    static void unregister(LocalAddress localAddress) {
        boundChannels.remove(localAddress);
    }

    static LocalAddress register(Channel channel, LocalAddress localAddress, SocketAddress socketAddress) {
        Channel channel2;
        if (localAddress != null) {
            throw new ChannelException("already bound");
        }
        if (!(socketAddress instanceof LocalAddress)) {
            throw new ChannelException("unsupported address type: " + StringUtil.simpleClassName(socketAddress));
        }
        LocalAddress localAddress2 = (LocalAddress)socketAddress;
        if (LocalAddress.ANY.equals(localAddress2)) {
            localAddress2 = new LocalAddress(channel);
        }
        if ((channel2 = boundChannels.putIfAbsent(localAddress2, channel)) != null) {
            throw new ChannelException("address already in use by: " + channel2);
        }
        return localAddress2;
    }

    private LocalChannelRegistry() {
    }

    static Channel get(SocketAddress socketAddress) {
        return (Channel)boundChannels.get(socketAddress);
    }
}

