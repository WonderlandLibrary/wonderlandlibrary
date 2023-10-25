/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel.oio;

import io.netty.channel.ThreadPerChannelEventLoopGroup;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class OioEventLoopGroup
extends ThreadPerChannelEventLoopGroup {
    public OioEventLoopGroup() {
        this(0);
    }

    public OioEventLoopGroup(int n, ThreadFactory threadFactory) {
        super(n, threadFactory, new Object[0]);
    }

    public OioEventLoopGroup(int n) {
        this(n, Executors.defaultThreadFactory());
    }
}

