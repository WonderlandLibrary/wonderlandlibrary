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
    public static final IIInventory wrap(IInventory iInventory) {
        boolean bl = false;
        return new IInventoryImpl(iInventory);
    }

    public static final IInventory unwrap(IIInventory iIInventory) {
        boolean bl = false;
        return ((IInventoryImpl)iIInventory).getWrapped();
    }
}

