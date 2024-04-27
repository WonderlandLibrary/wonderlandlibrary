/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  kotlin.jvm.functions.Function0
 *  net.minecraft.network.NetworkManager
 *  net.minecraft.network.Packet
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.crypto.SecretKey;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.Nullable;

public final class NetworkManagerImpl
implements INetworkManager {
    private final NetworkManager wrapped;

    public final NetworkManager getWrapped() {
        return this.wrapped;
    }

    @Override
    public void sendPacket(IPacket iPacket, Function0 function0) {
        IPacket iPacket2 = iPacket;
        NetworkManager networkManager = this.wrapped;
        boolean bl = false;
        Packet packet = ((PacketImpl)iPacket2).getWrapped();
        networkManager.func_179288_a(packet, new GenericFutureListener(function0){
            final Function0 $listener;
            {
                this.$listener = function0;
            }

            static {
            }

            public final void operationComplete(Future future) {
                this.$listener.invoke();
            }
        }, new GenericFutureListener[0]);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof NetworkManagerImpl && ((NetworkManagerImpl)object).wrapped.equals(this.wrapped);
    }

    public NetworkManagerImpl(NetworkManager networkManager) {
        this.wrapped = networkManager;
    }

    @Override
    public void enableEncryption(SecretKey secretKey) {
        this.wrapped.func_150727_a(secretKey);
    }

    @Override
    public void sendPacket(IPacket iPacket) {
        IPacket iPacket2 = iPacket;
        NetworkManager networkManager = this.wrapped;
        boolean bl = false;
        Packet packet = ((PacketImpl)iPacket2).getWrapped();
        networkManager.func_179290_a(packet);
    }
}

