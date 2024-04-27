/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  net.minecraft.network.NetworkManager
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.crypto.SecretKey;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.NetworkManager;
import org.jetbrains.annotations.Nullable;

public final class NetworkManagerImpl
implements INetworkManager {
    private final NetworkManager wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public void sendPacket(IPacket packet) {
        void $this$unwrap$iv;
        IPacket iPacket = packet;
        NetworkManager networkManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((PacketImpl)$this$unwrap$iv).getWrapped();
        networkManager.func_179290_a(t);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void sendPacket(IPacket packet, Function0<Unit> listener) {
        void $this$unwrap$iv;
        IPacket iPacket = packet;
        NetworkManager networkManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t = ((PacketImpl)$this$unwrap$iv).getWrapped();
        networkManager.func_179288_a(t, (GenericFutureListener)new GenericFutureListener<Future<? super Void>>(listener){
            final /* synthetic */ Function0 $listener;

            public final void operationComplete(Future<? super Void> it) {
                this.$listener.invoke();
            }
            {
                this.$listener = function0;
            }
        }, new GenericFutureListener[0]);
    }

    @Override
    public void enableEncryption(SecretKey secretKey) {
        this.wrapped.func_150727_a(secretKey);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof NetworkManagerImpl && ((NetworkManagerImpl)other).wrapped.equals(this.wrapped);
    }

    public final NetworkManager getWrapped() {
        return this.wrapped;
    }

    public NetworkManagerImpl(NetworkManager wrapped) {
        this.wrapped = wrapped;
    }
}

