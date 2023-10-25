/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonRealmsProxy;
import net.minecraft.util.ResourceLocation;

public class RealmsButton {
    private GuiButtonRealmsProxy proxy;
    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public RealmsButton(int n, int n2, int n3, String string) {
        this.proxy = new GuiButtonRealmsProxy(this, n, n2, n3, string);
    }

    public int getHeight() {
        return this.proxy.func_175232_g();
    }

    public void clicked(int n, int n2) {
    }

    public void msg(String string) {
        this.proxy.setText(string);
    }

    public void blit(int n, int n2, int n3, int n4, int n5, int n6) {
        this.proxy.drawTexturedModalRect(n, n2, n3, n4, n5, n6);
    }

    public int getWidth() {
        return this.proxy.getButtonWidth();
    }

    public void released(int n, int n2) {
    }

    public void renderBg(int n, int n2) {
    }

    public GuiButton getProxy() {
        return this.proxy;
    }

    public int y() {
        return this.proxy.getPositionY();
    }

    public boolean active() {
        return this.proxy.getEnabled();
    }

    public int id() {
        return this.proxy.getId();
    }

    public void render(int n, int n2) {
        this.proxy.drawButton(Minecraft.getMinecraft(), n, n2);
    }

    public int getYImage(boolean bl) {
        return this.proxy.func_154312_c(bl);
    }

    public RealmsButton(int n, int n2, int n3, int n4, int n5, String string) {
        this.proxy = new GuiButtonRealmsProxy(this, n, n2, n3, string, n4, n5);
    }

    public void active(boolean bl) {
        this.proxy.setEnabled(bl);
    }
}

