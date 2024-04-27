/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketCustomPayload
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.injection.backend.CPacketCustomPayloadImpl;
import net.minecraft.network.play.client.CPacketCustomPayload;

public final class CPacketCustomPayloadImplKt {
    public static final CPacketCustomPayload unwrap(ICPacketCustomPayload $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketCustomPayload)((CPacketCustomPayloadImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketCustomPayload wrap(CPacketCustomPayload $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketCustomPayloadImpl<CPacketCustomPayload>($this$wrap);
    }
}

