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
    public static final INetworkManager wrap(NetworkManager networkManager) {
        boolean bl = false;
        return new NetworkManagerImpl(networkManager);
    }

    public static final NetworkManager unwrap(INetworkManager iNetworkManager) {
        boolean bl = false;
        return ((NetworkManagerImpl)iNetworkManager).getWrapped();
    }
}

