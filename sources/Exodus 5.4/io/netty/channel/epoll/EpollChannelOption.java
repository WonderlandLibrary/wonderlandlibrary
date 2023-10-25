/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.epoll;

import io.netty.channel.ChannelOption;

public final class EpollChannelOption<T>
extends ChannelOption<T> {
    public static final ChannelOption<Integer> TCP_KEEPCNT;
    public static final ChannelOption<Integer> TCP_KEEPIDLE;
    public static final ChannelOption<Integer> TCP_KEEPINTVL;
    public static final ChannelOption<Boolean> TCP_CORK;
    public static final ChannelOption<Boolean> SO_REUSEPORT;

    private EpollChannelOption(String string) {
        super(string);
    }

    static {
        TCP_CORK = EpollChannelOption.valueOf("TCP_CORK");
        TCP_KEEPIDLE = EpollChannelOption.valueOf("TCP_KEEPIDLE");
        TCP_KEEPINTVL = EpollChannelOption.valueOf("TCP_KEEPINTVL");
        TCP_KEEPCNT = EpollChannelOption.valueOf("TCP_KEEPCNT");
        SO_REUSEPORT = EpollChannelOption.valueOf("SO_REUSEPORT");
    }
}

