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
    public static final NetworkPlayerInfo unwrap(INetworkPlayerInfo iNetworkPlayerInfo) {
        boolean bl = false;
        return ((NetworkPlayerInfoImpl)iNetworkPlayerInfo).getWrapped();
    }

    public static final INetworkPlayerInfo wrap(NetworkPlayerInfo networkPlayerInfo) {
        boolean bl = false;
        return new NetworkPlayerInfoImpl(networkPlayerInfo);
    }
}

