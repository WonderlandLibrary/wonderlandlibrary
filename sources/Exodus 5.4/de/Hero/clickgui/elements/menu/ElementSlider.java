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
import net.minecraft.util.MathHelper;

public class ElementSlider
extends Element {
    public boolean dragging;

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.dragging = false;
    }

    @Override
    public boolean mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.isSliderHovered(n, n2)) {
            this.dragging = true;
            return true;
        }
        return super.mouseClicked(n, n2, n3);
    }

    public ElementSlider(ModuleButton moduleButton, Setting setting) {
        this.parent = moduleButton;
        this.set = setting;
        this.dragging = false;
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        String string = "" + (double)Math.round(this.set.getValDouble() * 100.0) / 100.0;
        boolean bl = this.isSliderHovered(n, n2) || this.dragging;
        Color color = ColorUtil.getClickGUIColor();
        int n3 = CustomIngameGui.getColorInt((int)(this.y / 8.0));
        int n4 = CustomIngameGui.getColorInt((int)(this.y / 8.0));
        double d = (this.set.getValDouble() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15066598);
        Gui.drawRect(this.x, this.y + 12.5, this.x + this.width, this.y - 0.5, -15724528);
        Gui.drawRect(this.x, this.y + 12.5, this.x + d * this.width, this.y - 0.5, n3);
        if (d > 0.0 && d < 1.0) {
            Gui.drawRect(this.x + d * this.width, this.y + 12.0, this.x + Math.min(d * this.width, this.width), this.y - 2.0, n4);
        }
        if (this.dragging) {
            double d2 = this.set.getMax() - this.set.getMin();
            double d3 = this.set.getMin() + MathHelper.clamp_double(((double)n - this.x) / this.width, 0.0, 1.0) * d2;
            this.set.setValDouble(d3);
        }
        FontUtil.normal.drawString(String.valueOf(this.setstrg) + " " + string, this.x + 3.0, (float)(this.y + 3.0), -1);
    }

    public boolean isSliderHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= this.y - 11.0 && (double)n2 <= this.y + 14.0;
    }
}

