/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.module.settings.BooleanSetting;

public class AstolfoBooleanButton
extends AstolfoButton {
    public BooleanSetting setting;
    public Color color;

    public AstolfoBooleanButton(float x2, float y2, float width, float height, BooleanSetting set, Color col) {
        super(x2, y2, width, height);
        this.setting = set;
        this.color = col;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -299621581);
        Gui.drawRect((int)(this.x + this.width - 14.0f - 7.0f), (int)(this.y + this.height / 2.0f - 7.0f), (int)(this.x + this.width - 14.0f + 7.0f), (int)(this.y + this.height / 2.0f + 7.0f), -12303292);
        if (this.setting.getValue().booleanValue()) {
            Gui.drawRect((int)(this.x + this.width - 14.0f - 7.0f) + 2, (int)(this.y + this.height / 2.0f - 7.0f) + 2, (int)(this.x + this.width - 14.0f + 7.0f) - 2, (int)(this.y + this.height / 2.0f + 7.0f) - 2, this.color.getRGB());
        }
        String string = this.setting.name;
        float f2 = this.y + this.height / 2.0f;
        AstolfoClickGui.settingsFont.getClass();
        AstolfoClickGui.settingsFont.drawString(string, this.x + 8.0f, f2 - 9.0f / 2.0f, -1);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY) && click) {
            this.setting.setValue(this.setting.getValue() == false);
        }
    }
}

