/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.util;

import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;

public abstract class WrappedGuiSlot {
    public IGuiSlot represented;

    public final IGuiSlot getRepresented() {
        return this.represented;
    }

    public final void setRepresented(IGuiSlot iGuiSlot) {
        this.represented = iGuiSlot;
    }

    public abstract void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6);

    public abstract void drawBackground();

    public abstract void elementClicked(int var1, boolean var2, int var3, int var4);

    public abstract int getSize();

    public abstract boolean isSelected(int var1);

    public WrappedGuiSlot(IMinecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        WrapperImpl.INSTANCE.getClassProvider().wrapGuiSlot(this, mc, width, height, top, bottom, slotHeight);
    }
}

