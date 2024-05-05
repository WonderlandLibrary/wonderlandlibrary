package net.minecraft.network;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.local.LocalEventLoopGroup;
import net.minecraft.util.LazyLoadBase;

static final class NetworkSystem.3
extends LazyLoadBase<LocalEventLoopGroup> {
    NetworkSystem.3() {
    }

    protected LocalEventLoopGroup load() {
        return new LocalEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Local Server IO #%d").setDaemon(true).build());
    }
}
