/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.resources.I18n;

public class GuiListButton
extends GuiButton {
    private String localizationStr;
    private final GuiPageButtonList.GuiResponder guiResponder;
    private boolean field_175216_o;

    @Override
    public boolean mousePressed(Minecraft minecraft, int n, int n2) {
        if (super.mousePressed(minecraft, n, n2)) {
            this.field_175216_o = !this.field_175216_o;
            this.displayString = this.buildDisplayString();
            this.guiResponder.func_175321_a(this.id, this.field_175216_o);
            return true;
        }
        return false;
    }

    public GuiListButton(GuiPageButtonList.GuiResponder guiResponder, int n, int n2, int n3, String string, boolean bl) {
        super(n, n2, n3, 150, 20, "");
        this.localizationStr = string;
        this.field_175216_o = bl;
        this.displayString = this.buildDisplayString();
        this.guiResponder = guiResponder;
    }

    private String buildDisplayString() {
        return String.valueOf(I18n.format(this.localizationStr, new Object[0])) + ": " + (this.field_175216_o ? I18n.format("gui.yes", new Object[0]) : I18n.format("gui.no", new Object[0]));
    }

    public void func_175212_b(boolean bl) {
        this.field_175216_o = bl;
        this.displayString = this.buildDisplayString();
        this.guiResponder.func_175321_a(this.id, bl);
    }
}

