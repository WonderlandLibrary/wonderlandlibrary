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
    public static final Slot unwrap(ISlot $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((SlotImpl)$this$unwrap).getWrapped();
    }

    public static final ISlot wrap(Slot $this$wrap) {
        int $i$f$wrap = 0;
        return new SlotImpl($this$wrap);
    }
}

