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
    public static final IGuiSlot wrap(GuiSlot guiSlot) {
        boolean bl = false;
        return new GuiSlotImpl(guiSlot);
    }

    public static final GuiSlot unwrap(IGuiSlot iGuiSlot) {
        boolean bl = false;
        return ((GuiSlotImpl)iGuiSlot).getWrapped();
    }
}

