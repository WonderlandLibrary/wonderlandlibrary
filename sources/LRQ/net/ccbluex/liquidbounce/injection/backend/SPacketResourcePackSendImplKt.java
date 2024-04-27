/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.injection.backend.SPacketResourcePackSendImpl;
import net.minecraft.network.play.server.SPacketResourcePackSend;

public final class SPacketResourcePackSendImplKt {
    public static final SPacketResourcePackSend unwrap(ISPacketResourcePackSend $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketResourcePackSend)((SPacketResourcePackSendImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketResourcePackSend wrap(SPacketResourcePackSend $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketResourcePackSendImpl<SPacketResourcePackSend>($this$wrap);
    }
}

