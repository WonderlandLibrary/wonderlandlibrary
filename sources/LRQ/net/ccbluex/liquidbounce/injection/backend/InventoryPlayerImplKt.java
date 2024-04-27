/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.InventoryPlayer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.player.IInventoryPlayer;
import net.ccbluex.liquidbounce.injection.backend.InventoryPlayerImpl;
import net.minecraft.entity.player.InventoryPlayer;

public final class InventoryPlayerImplKt {
    public static final InventoryPlayer unwrap(IInventoryPlayer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((InventoryPlayerImpl)$this$unwrap).getWrapped();
    }

    public static final IInventoryPlayer wrap(InventoryPlayer $this$wrap) {
        int $i$f$wrap = 0;
        return new InventoryPlayerImpl($this$wrap);
    }
}

