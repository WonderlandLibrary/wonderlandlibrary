/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.concurrent.Future
 *  io.netty.util.concurrent.GenericFutureListener
 *  kotlin.Metadata
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.NetworkManager
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.crypto.SecretKey;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.NetworkManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u001e\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0013H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/NetworkManagerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "wrapped", "Lnet/minecraft/network/NetworkManager;", "(Lnet/minecraft/network/NetworkManager;)V", "getWrapped", "()Lnet/minecraft/network/NetworkManager;", "enableEncryption", "", "secretKey", "Ljavax/crypto/SecretKey;", "equals", "", "other", "", "sendPacket", "packet", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "listener", "Lkotlin/Function0;", "LiKingSense"})
public final class NetworkManagerImpl
implements INetworkManager {
    @NotNull
    private final NetworkManager wrapped;

    /*
     * WARNING - void declaration
     */
    @Override
    public void sendPacket(@NotNull IPacket packet) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)packet, (String)"packet");
        IPacket iPacket = packet;
        NetworkManager networkManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((PacketImpl)$this$unwrap$iv).getWrapped();
        networkManager.func_179290_a(t2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void sendPacket(@NotNull IPacket packet, @NotNull Function0<Unit> listener) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)packet, (String)"packet");
        Intrinsics.checkParameterIsNotNull(listener, (String)"listener");
        IPacket iPacket = packet;
        NetworkManager networkManager = this.wrapped;
        boolean $i$f$unwrap = false;
        Object t2 = ((PacketImpl)$this$unwrap$iv).getWrapped();
        networkManager.func_179288_a(t2, (GenericFutureListener)new GenericFutureListener<Future<? super Void>>(listener){
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
    public void enableEncryption(@NotNull SecretKey secretKey) {
        Intrinsics.checkParameterIsNotNull((Object)secretKey, (String)"secretKey");
        this.wrapped.func_150727_a(secretKey);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof NetworkManagerImpl && Intrinsics.areEqual((Object)((NetworkManagerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final NetworkManager getWrapped() {
        return this.wrapped;
    }

    public NetworkManagerImpl(@NotNull NetworkManager wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

