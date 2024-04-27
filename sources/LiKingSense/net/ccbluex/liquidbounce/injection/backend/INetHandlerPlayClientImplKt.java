/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.injection.backend.INetHandlerPlayClientImpl;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayClient;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=2, d1={"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0086\b\u001a\r\u0010\u0003\u001a\u00020\u0002*\u00020\u0004H\u0086\b\u00a8\u0006\u0005"}, d2={"unwrap", "Lnet/minecraft/network/play/INetHandlerPlayClient;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "wrap", "Lnet/minecraft/client/network/NetHandlerPlayClient;", "LiKingSense"})
public final class INetHandlerPlayClientImplKt {
    @NotNull
    public static final INetHandlerPlayClient unwrap(@NotNull IINetHandlerPlayClient $this$unwrap) {
        int $i$f$unwrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$unwrap, (String)"$this$unwrap");
        return (INetHandlerPlayClient)((INetHandlerPlayClientImpl)$this$unwrap).getWrapped();
    }

    @NotNull
    public static final IINetHandlerPlayClient wrap(@NotNull NetHandlerPlayClient $this$wrap) {
        int $i$f$wrap = 0;
        Intrinsics.checkParameterIsNotNull((Object)$this$wrap, (String)"$this$wrap");
        return new INetHandlerPlayClientImpl($this$wrap);
    }
}

