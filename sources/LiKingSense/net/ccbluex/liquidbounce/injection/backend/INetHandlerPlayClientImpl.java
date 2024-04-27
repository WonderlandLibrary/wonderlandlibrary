/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.network.NetworkManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Collection;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0096\u0002J\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/INetHandlerPlayClientImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "wrapped", "Lnet/minecraft/client/network/NetHandlerPlayClient;", "(Lnet/minecraft/client/network/NetHandlerPlayClient;)V", "networkManager", "Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "getNetworkManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "playerInfoMap", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/INetworkPlayerInfo;", "getPlayerInfoMap", "()Ljava/util/Collection;", "getWrapped", "()Lnet/minecraft/client/network/NetHandlerPlayClient;", "addToSendQueue", "", "packet", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "equals", "", "other", "", "getPlayerInfo", "uuid", "Ljava/util/UUID;", "LiKingSense"})
public final class INetHandlerPlayClientImpl
implements IINetHandlerPlayClient {
    @NotNull
    private final NetHandlerPlayClient wrapped;

    @Override
    @NotNull
    public INetworkManager getNetworkManager() {
        NetworkManager networkManager = this.wrapped.func_147298_b();
        Intrinsics.checkExpressionValueIsNotNull((Object)networkManager, (String)"wrapped.networkManager");
        NetworkManager $this$wrap$iv = networkManager;
        boolean $i$f$wrap = false;
        return new NetworkManagerImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public Collection<INetworkPlayerInfo> getPlayerInfoMap() {
        return new WrappedCollection(this.wrapped.func_175106_d(), playerInfoMap.1.INSTANCE, playerInfoMap.2.INSTANCE);
    }

    @Override
    @Nullable
    public INetworkPlayerInfo getPlayerInfo(@NotNull UUID uuid) {
        INetworkPlayerInfo iNetworkPlayerInfo;
        Intrinsics.checkParameterIsNotNull((Object)uuid, (String)"uuid");
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
    public void addToSendQueue(@NotNull IPacket packet) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)packet, (String)"packet");
        IPacket iPacket = packet;
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((PacketImpl)$this$unwrap$iv).getWrapped();
        netHandlerPlayClient.func_147297_a(t2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof INetHandlerPlayClientImpl && Intrinsics.areEqual((Object)((INetHandlerPlayClientImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final NetHandlerPlayClient getWrapped() {
        return this.wrapped;
    }

    public INetHandlerPlayClientImpl(@NotNull NetHandlerPlayClient wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

