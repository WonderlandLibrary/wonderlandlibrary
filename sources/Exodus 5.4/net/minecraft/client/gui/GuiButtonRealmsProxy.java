/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.realms.RealmsButton;

public class GuiButtonRealmsProxy
extends GuiButton {
    private RealmsButton realmsButton;

    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    public int getPositionY() {
        return this.yPosition;
    }

    @Override
    public void mouseReleased(int n, int n2) {
        this.realmsButton.released(n, n2);
    }

    @Override
    public int getButtonWidth() {
        return super.getButtonWidth();
    }

    public void setText(String string) {
        this.displayString = string;
    }

    public GuiButtonRealmsProxy(RealmsButton realmsButton, int n, int n2, int n3, String string, int n4, int n5) {
        super(n, n2, n3, n4, n5, string);
        this.realmsButton = realmsButton;
    }

    public RealmsButton getRealmsButton() {
        return this.realmsButton;
    }

    public int func_175232_g() {
        return this.height;
    }

    @Override
    public void mouseDragged(Minecraft minecraft, int n, int n2) {
        this.realmsButton.renderBg(n, n2);
    }

    @Override
    public int getHoverState(boolean bl) {
        return this.realmsButton.getYImage(bl);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int n, int n2) {
        if (super.mousePressed(minecraft, n, n2)) {
            this.realmsButton.clicked(n, n2);
        }
        return super.mousePressed(minecraft, n, n2);
    }

    public int getId() {
        return this.id;
    }

    public GuiButtonRealmsProxy(RealmsButton realmsButton, int n, int n2, int n3, String string) {
        super(n, n2, n3, string);
        this.realmsButton = realmsButton;
    }

    public int func_154312_c(boolean bl) {
        return super.getHoverState(bl);
    }
}

