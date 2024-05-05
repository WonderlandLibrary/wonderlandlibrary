/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.IInventory
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import net.ccbluex.liquidbounce.injection.backend.IInventoryImpl;
import net.minecraft.inventory.IInventory;

public final class IInventoryImplKt {
    public static final IInventory unwrap(IIInventory $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((IInventoryImpl)$this$unwrap).getWrapped();
    }

    public static final IIInventory wrap(IInventory $this$wrap) {
        int $i$f$wrap = 0;
        return new IInventoryImpl($this$wrap);
    }
}

