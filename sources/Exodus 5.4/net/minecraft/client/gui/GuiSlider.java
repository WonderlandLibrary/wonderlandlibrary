/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class GuiSlider
extends GuiButton {
    private final float max;
    public boolean isMouseDown;
    private FormatHelper formatHelper;
    private String name;
    private final GuiPageButtonList.GuiResponder responder;
    private final float min;
    private float sliderPosition = 1.0f;

    public void func_175219_a(float f) {
        this.sliderPosition = f;
        this.displayString = this.getDisplayString();
        this.responder.onTick(this.id, this.func_175220_c());
    }

    @Override
    public void mouseReleased(int n, int n2) {
        this.isMouseDown = false;
    }

    @Override
    protected void mouseDragged(Minecraft minecraft, int n, int n2) {
        if (this.visible) {
            if (this.isMouseDown) {
                this.sliderPosition = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
                if (this.sliderPosition < 0.0f) {
                    this.sliderPosition = 0.0f;
                }
                if (this.sliderPosition > 1.0f) {
                    this.sliderPosition = 1.0f;
                }
                this.displayString = this.getDisplayString();
                this.responder.onTick(this.id, this.func_175220_c());
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public float func_175220_c() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }

    public void func_175218_a(float f, boolean bl) {
        this.sliderPosition = (f - this.min) / (this.max - this.min);
        this.displayString = this.getDisplayString();
        if (bl) {
            this.responder.onTick(this.id, this.func_175220_c());
        }
    }

    @Override
    protected int getHoverState(boolean bl) {
        return 0;
    }

    public GuiSlider(GuiPageButtonList.GuiResponder guiResponder, int n, int n2, int n3, String string, float f, float f2, float f3, FormatHelper formatHelper) {
        super(n, n2, n3, 150, 20, "");
        this.name = string;
        this.min = f;
        this.max = f2;
        this.sliderPosition = (f3 - f) / (f2 - f);
        this.formatHelper = formatHelper;
        this.responder = guiResponder;
        this.displayString = this.getDisplayString();
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int n, int n2) {
        if (super.mousePressed(minecraft, n, n2)) {
            this.sliderPosition = (float)(n - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderPosition < 0.0f) {
                this.sliderPosition = 0.0f;
            }
            if (this.sliderPosition > 1.0f) {
                this.sliderPosition = 1.0f;
            }
            this.displayString = this.getDisplayString();
            this.responder.onTick(this.id, this.func_175220_c());
            this.isMouseDown = true;
            return true;
        }
        return false;
    }

    private String getDisplayString() {
        return this.formatHelper == null ? String.valueOf(I18n.format(this.name, new Object[0])) + ": " + this.func_175220_c() : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), this.func_175220_c());
    }

    public float func_175217_d() {
        return this.sliderPosition;
    }

    public static interface FormatHelper {
        public String getText(int var1, String var2, float var3);
    }
}

