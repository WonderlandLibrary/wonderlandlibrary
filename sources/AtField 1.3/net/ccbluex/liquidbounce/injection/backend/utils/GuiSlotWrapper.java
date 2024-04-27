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

    protected boolean func_148131_a(int n) {
        return this.wrapped.isSelected(n);
    }

    protected void func_192637_a(int n, int n2, int n3, int n4, int n5, int n6, float f) {
        this.wrapped.drawSlot(n, n2, n3, n4, n5, n6);
    }

    public GuiSlotWrapper(WrappedGuiSlot wrappedGuiSlot, IMinecraft iMinecraft, int n, int n2, int n3, int n4, int n5) {
        IMinecraft iMinecraft2 = iMinecraft;
        GuiSlotWrapper guiSlotWrapper = this;
        boolean bl = false;
        Minecraft minecraft = ((MinecraftImpl)iMinecraft2).getWrapped();
        super(minecraft, n, n2, n3, n4, n5);
        this.wrapped = wrappedGuiSlot;
        this.wrapped.setRepresented(new GuiSlotImpl(this));
    }

    public final WrappedGuiSlot getWrapped() {
        return this.wrapped;
    }

    public void func_148144_a(int n, boolean bl, int n2, int n3) {
        this.wrapped.elementClicked(n, bl, n2, n3);
    }

    protected int func_148127_b() {
        return this.wrapped.getSize();
    }

    protected void func_148123_a() {
        this.wrapped.drawBackground();
    }
}

