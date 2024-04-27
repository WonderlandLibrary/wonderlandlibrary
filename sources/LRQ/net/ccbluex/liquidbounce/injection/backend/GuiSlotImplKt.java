/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiSlot
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.GuiSlotImpl;
import net.minecraft.client.gui.GuiSlot;

public final class GuiSlotImplKt {
    public static final GuiSlot unwrap(IGuiSlot $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((GuiSlotImpl)$this$unwrap).getWrapped();
    }

    public static final IGuiSlot wrap(GuiSlot $this$wrap) {
        int $i$f$wrap = 0;
        return new GuiSlotImpl($this$wrap);
    }
}

