/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.GuiTextField
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0013\u0010\u001d\u001a\u00020\u00062\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\u0018\u0010 \u001a\u00020\u00062\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u000bH\u0016J \u0010$\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020\u000b2\u0006\u0010&\u001a\u00020\u000b2\u0006\u0010'\u001a\u00020\u000bH\u0016J\u0018\u0010(\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u000bH\u0016J\b\u0010)\u001a\u00020\u001cH\u0016R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR$\u0010\f\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u000b8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R$\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0005\u001a\u00020\u00118V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0019\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u000e\u00a8\u0006*"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GuiTextFieldImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiTextField;", "wrapped", "Lnet/minecraft/client/gui/GuiTextField;", "(Lnet/minecraft/client/gui/GuiTextField;)V", "value", "", "isFocused", "()Z", "setFocused", "(Z)V", "", "maxStringLength", "getMaxStringLength", "()I", "setMaxStringLength", "(I)V", "", "text", "getText", "()Ljava/lang/String;", "setText", "(Ljava/lang/String;)V", "getWrapped", "()Lnet/minecraft/client/gui/GuiTextField;", "xPosition", "getXPosition", "drawTextBox", "", "equals", "other", "", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseX", "mouseY", "mouseButton", "textboxKeyTyped", "updateCursorCounter", "LiKingSense"})
public final class GuiTextFieldImpl
implements IGuiTextField {
    @NotNull
    private final GuiTextField wrapped;

    @Override
    public int getXPosition() {
        return this.wrapped.field_146209_f;
    }

    @Override
    @NotNull
    public String getText() {
        String string = this.wrapped.func_146179_b();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.text");
        return string;
    }

    @Override
    public void setText(@NotNull String value) {
        Intrinsics.checkParameterIsNotNull((Object)value, (String)"value");
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
        return other instanceof GuiTextFieldImpl && Intrinsics.areEqual((Object)((GuiTextFieldImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final GuiTextField getWrapped() {
        return this.wrapped;
    }

    public GuiTextFieldImpl(@NotNull GuiTextField wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

