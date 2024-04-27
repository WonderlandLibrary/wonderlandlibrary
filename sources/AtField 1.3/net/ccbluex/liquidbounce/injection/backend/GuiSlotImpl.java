/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.gui.GuiSlot
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.utils.GuiSlotWrapper;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot;
import net.minecraft.client.gui.GuiSlot;

public final class GuiSlotImpl
implements IGuiSlot {
    private final GuiSlot wrapped;

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.wrapped.func_148128_a(n, n2, f);
    }

    @Override
    public int getWidth() {
        return this.wrapped.field_148155_a;
    }

    @Override
    public void registerScrollButtons(int n, int n2) {
        this.wrapped.func_148134_d(n, n2);
    }

    @Override
    public void handleMouseInput() {
        this.wrapped.func_178039_p();
    }

    @Override
    public void elementClicked(int n, boolean bl, int n2, int n3) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiSlotWrapper");
        }
        ((GuiSlotWrapper)guiSlot).func_148144_a(n, bl, n2, n3);
    }

    public GuiSlotImpl(GuiSlot guiSlot) {
        this.wrapped = guiSlot;
    }

    public final GuiSlot getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setListWidth(int n) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot");
        }
        ((IMixinGuiSlot)guiSlot).setListWidth(n);
    }

    @Override
    public void scrollBy(int n) {
        this.wrapped.func_148145_f(n);
    }

    @Override
    public int getSlotHeight() {
        return this.wrapped.field_148149_f;
    }

    @Override
    public void setEnableScissor(boolean bl) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot");
        }
        ((IMixinGuiSlot)guiSlot).setEnableScissor(bl);
    }
}

