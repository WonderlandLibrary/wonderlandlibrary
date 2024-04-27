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
    public int getWidth() {
        return this.wrapped.field_148155_a;
    }

    @Override
    public int getSlotHeight() {
        return this.wrapped.field_148149_f;
    }

    @Override
    public void scrollBy(int value) {
        this.wrapped.func_148145_f(value);
    }

    @Override
    public void registerScrollButtons(int down, int up) {
        this.wrapped.func_148134_d(down, up);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.wrapped.func_148128_a(mouseX, mouseY, partialTicks);
    }

    @Override
    public void elementClicked(int index, boolean doubleClick, int var3, int var4) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiSlotWrapper");
        }
        ((GuiSlotWrapper)guiSlot).func_148144_a(index, doubleClick, var3, var4);
    }

    @Override
    public void handleMouseInput() {
        this.wrapped.func_178039_p();
    }

    @Override
    public void setListWidth(int width) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot");
        }
        ((IMixinGuiSlot)guiSlot).setListWidth(width);
    }

    @Override
    public void setEnableScissor(boolean flag) {
        GuiSlot guiSlot = this.wrapped;
        if (guiSlot == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot");
        }
        ((IMixinGuiSlot)guiSlot).setEnableScissor(flag);
    }

    public final GuiSlot getWrapped() {
        return this.wrapped;
    }

    public GuiSlotImpl(GuiSlot wrapped) {
        this.wrapped = wrapped;
    }
}

