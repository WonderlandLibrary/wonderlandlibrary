/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class RealmsEditBox {
    private final GuiTextField editBox;

    public void mouseClicked(int n, int n2, int n3) {
        this.editBox.mouseClicked(n, n2, n3);
    }

    public String getValue() {
        return this.editBox.getText();
    }

    public RealmsEditBox(int n, int n2, int n3, int n4, int n5) {
        Minecraft.getMinecraft();
        this.editBox = new GuiTextField(n, Minecraft.fontRendererObj, n2, n3, n4, n5);
    }

    public void setIsEditable(boolean bl) {
        this.editBox.setEnabled(bl);
    }

    public void setMaxLength(int n) {
        this.editBox.setMaxStringLength(n);
    }

    public void tick() {
        this.editBox.updateCursorCounter();
    }

    public boolean isFocused() {
        return this.editBox.isFocused();
    }

    public void keyPressed(char c, int n) {
        this.editBox.textboxKeyTyped(c, n);
    }

    public void render() {
        this.editBox.drawTextBox();
    }

    public void setFocus(boolean bl) {
        this.editBox.setFocused(bl);
    }

    public void setValue(String string) {
        this.editBox.setText(string);
    }
}

