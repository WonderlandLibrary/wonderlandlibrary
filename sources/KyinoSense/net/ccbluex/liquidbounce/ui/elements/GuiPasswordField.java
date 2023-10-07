/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiTextField
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.elements;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/ui/elements/GuiPasswordField;", "Lnet/minecraft/client/gui/GuiTextField;", "componentId", "", "fontrendererObj", "Lnet/minecraft/client/gui/FontRenderer;", "x", "y", "par5Width", "par6Height", "(ILnet/minecraft/client/gui/FontRenderer;IIII)V", "drawTextBox", "", "KyinoClient"})
public final class GuiPasswordField
extends GuiTextField {
    /*
     * WARNING - void declaration
     */
    public void func_146194_f() {
        String realText = this.func_146179_b();
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        String string = this.func_146179_b();
        Intrinsics.checkExpressionValueIsNotNull(string, "text");
        int n2 = ((CharSequence)string).length();
        while (n < n2) {
            void i;
            stringBuilder.append('*');
            ++i;
        }
        this.func_146180_a(stringBuilder.toString());
        super.func_146194_f();
        this.func_146180_a(realText);
    }

    public GuiPasswordField(int componentId, @NotNull FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        Intrinsics.checkParameterIsNotNull(fontrendererObj, "fontrendererObj");
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }
}

