/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketEntityAction
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.injection.backend.CPacketEntityActionImpl;
import net.minecraft.network.play.client.CPacketEntityAction;

public final class CPacketEntityActionImplKt {
    public static final CPacketEntityAction unwrap(ICPacketEntityAction $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketEntityAction)((CPacketEntityActionImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketEntityAction wrap(CPacketEntityAction $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketEntityActionImpl<CPacketEntityAction>($this$wrap);
    }
}

