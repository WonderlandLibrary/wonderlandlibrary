/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import dev.sakura_starring.ui.GuiLogin;
import dev.sakura_starring.util.safe.Safe;
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
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.Nullable;

public final class INetHandlerPlayClientImpl
implements IINetHandlerPlayClient {
    private final NetHandlerPlayClient wrapped;

    public final NetHandlerPlayClient getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof INetHandlerPlayClientImpl && ((INetHandlerPlayClientImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public INetworkPlayerInfo getPlayerInfo(UUID uUID) {
        INetworkPlayerInfo iNetworkPlayerInfo;
        NetworkPlayerInfo networkPlayerInfo = this.wrapped.func_175102_a(uUID);
        if (networkPlayerInfo != null) {
            NetworkPlayerInfo networkPlayerInfo2 = networkPlayerInfo;
            boolean bl = false;
            iNetworkPlayerInfo = new NetworkPlayerInfoImpl(networkPlayerInfo2);
        } else {
            iNetworkPlayerInfo = null;
        }
        return iNetworkPlayerInfo;
    }

    @Override
    public Collection getPlayerInfoMap() {
        return new WrappedCollection(this.wrapped.func_175106_d(), playerInfoMap.1.INSTANCE, playerInfoMap.2.INSTANCE);
    }

    @Override
    public void addToSendQueue(IPacket iPacket) {
        if (!Safe.verification) {
            MinecraftInstance.mc.displayGuiScreen(MinecraftInstance.classProvider.wrapGuiScreen(new GuiLogin()));
        }
        IPacket iPacket2 = iPacket;
        NetHandlerPlayClient netHandlerPlayClient = this.wrapped;
        boolean bl = false;
        Packet packet = ((PacketImpl)iPacket2).getWrapped();
        netHandlerPlayClient.func_147297_a(packet);
    }

    public INetHandlerPlayClientImpl(NetHandlerPlayClient netHandlerPlayClient) {
        this.wrapped = netHandlerPlayClient;
    }

    @Override
    public INetworkManager getNetworkManager() {
        NetworkManager networkManager = this.wrapped.func_147298_b();
        boolean bl = false;
        return new NetworkManagerImpl(networkManager);
    }
}

