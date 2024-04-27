/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.NetworkManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.injection.backend.NetworkManagerImpl;
import net.minecraft.network.NetworkManager;

public final class NetworkManagerImplKt {
    public static final NetworkManager unwrap(INetworkManager $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((NetworkManagerImpl)$this$unwrap).getWrapped();
    }

    public static final INetworkManager wrap(NetworkManager $this$wrap) {
        int $i$f$wrap = 0;
        return new NetworkManagerImpl($this$wrap);
    }
}

