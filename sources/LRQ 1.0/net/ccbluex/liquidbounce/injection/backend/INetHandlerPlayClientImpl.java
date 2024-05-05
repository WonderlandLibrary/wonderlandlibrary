/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.network.NetworkManager
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import java.util.UUID;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import net.ccbluex.liquidbounce.injection.backend.INetHandlerPlayClientImpl;
import net.ccbluex.liquidbounce.injection.backend.NetworkManagerImpl;
import net.ccbluex.liquidbounce.injection.backend.NetworkPlayerInfoImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;
import org.jetbrains.annotations.Nullable;

public final class INetHandlerPlayClientImpl
implements IINetHandlerPlayClient {
    private final NetHandlerPlayClient wrapped;

    @Override
    public INetworkManager getNetworkManager() {
        NetworkManager $this$wrap$iv = this.wrapped.func_147298_b();
        boolean $i$f$wrap = false;
        return new NetworkManagerImpl($this$wrap$iv);
    }

    @Override
    public Collection<INetworkPlayerInfo> getPlayerInfoMap() {
        return new WrappedCollection(this.wrapped.func_175106_d(), playerInfoMap.1.INSTANCE, playerInfoMap.2.INSTANCE);
    }

    @Override
    public INetworkPlayerInfo getPlayerInfo(UUID uuid) {
        INetworkPlayerInfo iNetworkPlayerInfo;
        NetworkPlayerInfo networkPlayerInfo = this.wrapped.func_175102_a(uuid);
        if (networkPlayerInfo != null) {
            NetworkPlayerInfo $this$wrap$iv = networkPlayerInfo;
            boolean $i$f$wrap = false;
            iNetworkPlayerInfo = new NetworkPlayerInfoImpl($this$wrap$iv);
        } else {
            iNetworkPlayerInfo = null;
        }
        return iNetworkPlayerInfo;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void addToSendQueue(IPacket packet) {
        void $this$unwrap$iv;
        IPacket iPacket = packet;
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((PacketImpl)$this$unwrap$iv).getWrapped();
        netHandlerPlayClient.func_147297_a(t);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof INetHandlerPlayClientImpl && ((INetHandlerPlayClientImpl)other).wrapped.equals(this.wrapped);
    }

    public final NetHandlerPlayClient getWrapped() {
        return this.wrapped;
    }

    public INetHandlerPlayClientImpl(NetHandlerPlayClient wrapped) {
        this.wrapped = wrapped;
    }
}

