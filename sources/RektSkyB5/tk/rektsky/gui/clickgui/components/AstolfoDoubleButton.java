/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.module.settings.DoubleSetting;

public class AstolfoDoubleButton
extends AstolfoButton {
    public DoubleSetting setting;
    public Color color;
    public boolean dragged;

    public AstolfoDoubleButton(float x2, float y2, float width, float height, DoubleSetting set, Color col) {
        super(x2, y2, width, height);
        this.color = col;
        this.setting = set;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        double diff = (this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin());
        float percentWidth = (float)((this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()));
        if (this.dragged) {
            double newVal = (double)(((float)mouseX - (this.x + 6.0f)) / (this.width - 12.0f)) * (this.setting.getMax() - this.setting.getMin()) + this.setting.getMin();
            this.setting.setValue(Math.max(Math.min(newVal, this.setting.getMax()), this.setting.getMin()));
            percentWidth = (float)((this.setting.getValue() - this.setting.getMin()) / (this.setting.getMax() - this.setting.getMin()));
        }
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -299621581);
        Gui.drawRect((int)(this.x + 6.0f), (int)this.y + 3, (int)(this.x + 6.0f + percentWidth * (this.width - 12.0f)), (int)(this.y + this.height - 3.0f), this.color.getRGB());
        String string = this.setting.name + ": " + Double.toString(Math.floor(this.setting.getValue() * 10.0) / 10.0);
        float f2 = this.y + this.height / 2.0f;
        AstolfoClickGui.settingsFont.getClass();
        AstolfoClickGui.settingsFont.drawString(string, this.x + 8.0f, f2 - 9.0f / 2.0f, -1);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY)) {
            this.dragged = true;
        }
        if (!click) {
            this.dragged = false;
        }
    }
}

