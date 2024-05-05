/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.IInventory
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import net.minecraft.inventory.IInventory;
import org.jetbrains.annotations.Nullable;

public final class IInventoryImpl
implements IIInventory {
    private final IInventory wrapped;

    @Override
    public String getName() {
        return this.wrapped.func_70005_c_();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IInventoryImpl && ((IInventoryImpl)other).wrapped.equals(this.wrapped);
    }

    public final IInventory getWrapped() {
        return this.wrapped;
    }

    public IInventoryImpl(IInventory wrapped) {
        this.wrapped = wrapped;
    }
}

