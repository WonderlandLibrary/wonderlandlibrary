/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.gui.clickgui.components;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.gui.clickgui.components.AstolfoButton;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.settings.ListSetting;

public class AstolfoModeButton
extends AstolfoButton {
    public ListSetting setting;
    public Color color;

    public AstolfoModeButton(float x2, float y2, float width, float height, ListSetting set, Color col) {
        super(x2, y2, width, height);
        this.setting = set;
        this.color = col;
    }

    @Override
    public void drawPanel(int mouseX, int mouseY) {
        Gui.drawRect((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), -299621581);
        String string = this.setting.name + ": " + this.setting.getValue();
        float f2 = this.y + this.height / 2.0f;
        AstolfoClickGui.settingsFont.getClass();
        AstolfoClickGui.settingsFont.drawString(string, this.x + 8.0f, f2 - 9.0f / 2.0f, -1);
    }

    @Override
    public void mouseAction(int mouseX, int mouseY, boolean click, int button) {
        if (this.isHovered(mouseX, mouseY) && click) {
            for (Module m2 : ModulesManager.getModules()) {
                m2.unregisterListeners();
            }
            try {
                this.setting.setValue(this.setting.getValues()[this.setting.getCounter() + 1]);
            }
            catch (Exception ex) {
                this.setting.setValue(this.setting.getValues()[0]);
            }
        }
    }
}

