/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketWindowItems
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketWindowItems;
import net.ccbluex.liquidbounce.injection.backend.SPacketWindowItemsImpl;
import net.minecraft.network.play.server.SPacketWindowItems;

public final class SPacketWindowItemsImplKt {
    public static final SPacketWindowItems unwrap(ISPacketWindowItems $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketWindowItems)((SPacketWindowItemsImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketWindowItems wrap(SPacketWindowItems $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketWindowItemsImpl<SPacketWindowItems>($this$wrap);
    }
}

