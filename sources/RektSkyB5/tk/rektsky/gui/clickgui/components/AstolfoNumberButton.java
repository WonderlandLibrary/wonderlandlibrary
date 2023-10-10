/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.module.settings.IntSetting;

public class AstolfoNumberButton
extends AstolfoButton {
    public IntSetting setting;
    public Color color;
    public boolean dragged;

    public AstolfoNumberButton(float x2, float y2, float width, float height, IntSetting set, Color col) {
        super(x2, y2, width, height);
        this.color = col;
        this.setting = set;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        if (this.dragged) {
            int diff = this.setting.getMax() - this.setting.getMin();
            double newVal = ((float)mouseX - (this.x + 6.0f)) * ((float)diff / (this.width - 12.0f));
            this.setting.setValue((int)Math.max(Math.min(newVal, (double)this.setting.getMax()), (double)this.setting.getMin()));
        }
        double percentWidth = 1.0 * (double)(this.setting.getValue() - this.setting.getMin()) / (1.0 * (double)(this.setting.getMax() - this.setting.getMin()));
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -15197673);
        if ((double)(this.x + 3.0f) < (double)this.x + percentWidth * (double)this.width - 3.0) {
            Gui.drawRect((int)(this.x + 3.0f), (int)this.y, (int)((double)this.x + percentWidth * (double)(this.width - 6.0f)) - 3, (int)(this.y + this.height), this.color.getRGB());
        }
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -299621581);
        Gui.drawRect((int)(this.x + 6.0f), (int)this.y + 3, (int)((double)(this.x + 6.0f) + percentWidth * (double)(this.width - 12.0f)), (int)(this.y + this.height - 3.0f), this.color.getRGB());
        String string = this.setting.name + ": " + Double.toString(Math.floor(this.setting.getValue() * 10) / 10.0);
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

