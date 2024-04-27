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
    public int getXPosition() {
        return this.wrapped.field_146209_f;
    }

    @Override
    public String getText() {
        return this.wrapped.func_146179_b();
    }

    @Override
    public void setText(String value) {
        this.wrapped.func_146180_a(value);
    }

    @Override
    public boolean isFocused() {
        return this.wrapped.func_146206_l();
    }

    @Override
    public void setFocused(boolean value) {
        this.wrapped.func_146195_b(value);
    }

    @Override
    public int getMaxStringLength() {
        return this.wrapped.func_146208_g();
    }

    @Override
    public void setMaxStringLength(int value) {
        this.wrapped.func_146203_f(value);
    }

    @Override
    public void updateCursorCounter() {
        this.wrapped.func_146178_a();
    }

    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        return this.wrapped.func_146201_a(typedChar, keyCode);
    }

    @Override
    public void drawTextBox() {
        this.wrapped.func_146194_f();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.wrapped.func_146192_a(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        return this.wrapped.func_146201_a(typedChar, keyCode);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof GuiTextFieldImpl && ((GuiTextFieldImpl)other).wrapped.equals(this.wrapped);
    }

    public final GuiTextField getWrapped() {
        return this.wrapped;
    }

    public GuiTextFieldImpl(GuiTextField wrapped) {
        this.wrapped = wrapped;
    }
}

