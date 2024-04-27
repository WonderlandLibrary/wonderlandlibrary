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
    public GuiPasswordField(int n, FontRenderer fontRenderer, int n2, int n3, int n4, int n5) {
        super(n, fontRenderer, n2, n3, n4, n5);
    }

    public void func_146194_f() {
        String string = this.func_146179_b();
        StringBuilder stringBuilder = new StringBuilder();
        int n = ((CharSequence)this.func_146179_b()).length();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append('*');
        }
        this.func_146180_a(stringBuilder.toString());
        super.func_146194_f();
        this.func_146180_a(string);
    }
}

