/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiSlot
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.injection.backend.GuiSlotImpl;
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public final class GuiSlotWrapper
extends GuiSlot {
    private final WrappedGuiSlot wrapped;

    protected int func_148127_b() {
        return this.wrapped.getSize();
    }

    protected void func_192637_a(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
        this.wrapped.drawSlot(slotIndex, xPos, yPos, heightIn, mouseXIn, mouseYIn);
    }

    protected boolean func_148131_a(int slotIndex) {
        return this.wrapped.isSelected(slotIndex);
    }

    protected void func_148123_a() {
        this.wrapped.drawBackground();
    }

    public void func_148144_a(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.wrapped.elementClicked(slotIndex, isDoubleClick, mouseX, mouseY);
    }

    public final WrappedGuiSlot getWrapped() {
        return this.wrapped;
    }

    /*
     * WARNING - void declaration
     */
    public GuiSlotWrapper(WrappedGuiSlot wrapped, IMinecraft mc, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        void $this$unwrap$iv;
        IMinecraft iMinecraft = mc;
        GuiSlotWrapper guiSlotWrapper = this;
        boolean $i$f$unwrap = false;
        Minecraft minecraft = ((MinecraftImpl)$this$unwrap$iv).getWrapped();
        super(minecraft, width, height, topIn, bottomIn, slotHeightIn);
        this.wrapped = wrapped;
        this.wrapped.setRepresented(new GuiSlotImpl(this));
    }
}

