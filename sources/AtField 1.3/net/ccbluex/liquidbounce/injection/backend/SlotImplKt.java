/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.Slot
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.injection.backend.SlotImpl;
import net.minecraft.inventory.Slot;

public final class SlotImplKt {
    public static final ISlot wrap(Slot slot) {
        boolean bl = false;
        return new SlotImpl(slot);
    }

    public static final Slot unwrap(ISlot iSlot) {
        boolean bl = false;
        return ((SlotImpl)iSlot).getWrapped();
    }
}

