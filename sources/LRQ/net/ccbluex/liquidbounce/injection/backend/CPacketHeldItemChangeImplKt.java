/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.injection.backend.CPacketHeldItemChangeImpl;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public final class CPacketHeldItemChangeImplKt {
    public static final CPacketHeldItemChange unwrap(ICPacketHeldItemChange $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketHeldItemChange)((CPacketHeldItemChangeImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketHeldItemChange wrap(CPacketHeldItemChange $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketHeldItemChangeImpl<CPacketHeldItemChange>($this$wrap);
    }
}

