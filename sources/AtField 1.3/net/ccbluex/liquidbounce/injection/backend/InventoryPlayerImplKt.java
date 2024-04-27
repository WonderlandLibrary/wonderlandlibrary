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
    public static final InventoryPlayer unwrap(IInventoryPlayer iInventoryPlayer) {
        boolean bl = false;
        return ((InventoryPlayerImpl)iInventoryPlayer).getWrapped();
    }

    public static final IInventoryPlayer wrap(InventoryPlayer inventoryPlayer) {
        boolean bl = false;
        return new InventoryPlayerImpl(inventoryPlayer);
    }
}

