/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.injection.backend.NetworkPlayerInfoImpl;
import net.minecraft.client.network.NetworkPlayerInfo;

public final class NetworkPlayerInfoImplKt {
    public static final NetworkPlayerInfo unwrap(INetworkPlayerInfo $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((NetworkPlayerInfoImpl)$this$unwrap).getWrapped();
    }

    public static final INetworkPlayerInfo wrap(NetworkPlayerInfo $this$wrap) {
        int $i$f$wrap = 0;
        return new NetworkPlayerInfoImpl($this$wrap);
    }
}

