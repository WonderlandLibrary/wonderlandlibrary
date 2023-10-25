/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui.elements.menu;

import de.Hero.clickgui.elements.Element;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.ColorUtil;
import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;

public class ElementCheckBox
extends Element {
    public ElementCheckBox(ModuleButton moduleButton, Setting setting) {
        this.parent = moduleButton;
        this.set = setting;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        Color color = ColorUtil.getClickGUIColor();
        int n3 = CustomIngameGui.getColorInt((int)(this.y / 8.0));
        Gui.drawRect(this.x + 2.25, this.y, this.x + this.width, this.y + this.height, -15066598);
        Gui.drawRect(this.x + 3.5, this.y + 2.0, this.x + 14.0, this.y + 13.0, this.set.getValBoolean() ? n3 : -16777216);
        FontUtil.normal.drawStringWithShadow(this.setstrg, this.x + 15.0, (float)(this.y + (double)((float)FontUtil.normal.getHeight() / 1.5f)), -1);
    }

    @Override
    public boolean mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isCheckHovered(n, n2)) {
            this.set.setValBoolean(!this.set.getValBoolean());
            return true;
        }
        return super.mouseClicked(n, n2, n3);
    }

    public boolean isCheckHovered(int n, int n2) {
        return (double)n >= this.x + 1.0 && (double)n <= this.x + 12.0 && (double)n2 >= this.y + 2.0 && (double)n2 <= this.y + 13.0;
    }
}

