/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.play.INetHandlerPlayClient
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.injection.backend.INetHandlerPlayClientImpl;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayClient;

public final class INetHandlerPlayClientImplKt {
    public static final INetHandlerPlayClient unwrap(IINetHandlerPlayClient $this$unwrap) {
        int $i$f$unwrap = 0;
        return (INetHandlerPlayClient)((INetHandlerPlayClientImpl)$this$unwrap).getWrapped();
    }

    public static final IINetHandlerPlayClient wrap(NetHandlerPlayClient $this$wrap) {
        int $i$f$wrap = 0;
        return new INetHandlerPlayClientImpl($this$wrap);
    }
}

