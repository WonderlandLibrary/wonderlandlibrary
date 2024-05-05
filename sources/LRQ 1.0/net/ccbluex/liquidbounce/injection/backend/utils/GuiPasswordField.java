/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiTextField
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public final class GuiPasswordField
extends GuiTextField {
    /*
     * WARNING - void declaration
     */
    public void func_146194_f() {
        String realText = this.func_146179_b();
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        int n2 = ((CharSequence)this.func_146179_b()).length();
        while (n < n2) {
            void i;
            stringBuilder.append('*');
            ++i;
        }
        this.func_146180_a(stringBuilder.toString());
        super.func_146194_f();
        this.func_146180_a(realText);
    }

    public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }
}

