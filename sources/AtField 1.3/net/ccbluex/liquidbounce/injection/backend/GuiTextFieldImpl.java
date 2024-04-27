/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiTextField
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.Nullable;

public final class GuiTextFieldImpl
implements IGuiTextField {
    private final GuiTextField wrapped;

    @Override
    public int getMaxStringLength() {
        return this.wrapped.func_146208_g();
    }

    @Override
    public boolean textboxKeyTyped(char c, int n) {
        return this.wrapped.func_146201_a(c, n);
    }

    @Override
    public String getText() {
        return this.wrapped.func_146179_b();
    }

    @Override
    public void updateCursorCounter() {
        this.wrapped.func_146178_a();
    }

    public GuiTextFieldImpl(GuiTextField guiTextField) {
        this.wrapped = guiTextField;
    }

    @Override
    public void setMaxStringLength(int n) {
        this.wrapped.func_146203_f(n);
    }

    @Override
    public void drawTextBox() {
        this.wrapped.func_146194_f();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof GuiTextFieldImpl && ((GuiTextFieldImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void setFocused(boolean bl) {
        this.wrapped.func_146195_b(bl);
    }

    @Override
    public void setText(String string) {
        this.wrapped.func_146180_a(string);
    }

    @Override
    public boolean isFocused() {
        return this.wrapped.func_146206_l();
    }

    public final GuiTextField getWrapped() {
        return this.wrapped;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        this.wrapped.func_146192_a(n, n2, n3);
    }

    @Override
    public boolean keyTyped(char c, int n) {
        return this.wrapped.func_146201_a(c, n);
    }

    @Override
    public int getXPosition() {
        return this.wrapped.field_146209_f;
    }
}

