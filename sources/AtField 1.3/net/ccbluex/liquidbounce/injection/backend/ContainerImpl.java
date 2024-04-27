/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.injection.backend.SlotImpl;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public final class ContainerImpl
implements IContainer {
    private final Container wrapped;

    public final Container getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ContainerImpl && ((ContainerImpl)object).wrapped.equals(this.wrapped);
    }

    public ContainerImpl(Container container) {
        this.wrapped = container;
    }

    @Override
    public int getWindowId() {
        return this.wrapped.field_75152_c;
    }

    @Override
    public ISlot getSlot(int n) {
        Slot slot = this.wrapped.func_75139_a(n);
        boolean bl = false;
        return new SlotImpl(slot);
    }
}

