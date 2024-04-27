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

    @Override
    public int getWindowId() {
        return this.wrapped.field_75152_c;
    }

    @Override
    public ISlot getSlot(int id) {
        Slot $this$wrap$iv = this.wrapped.func_75139_a(id);
        boolean $i$f$wrap = false;
        return new SlotImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ContainerImpl && ((ContainerImpl)other).wrapped.equals(this.wrapped);
    }

    public final Container getWrapped() {
        return this.wrapped;
    }

    public ContainerImpl(Container wrapped) {
        this.wrapped = wrapped;
    }
}

