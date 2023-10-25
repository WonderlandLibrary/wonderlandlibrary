/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui.elements;

import de.Hero.clickgui.ClickGUI;
import de.Hero.clickgui.elements.ModuleButton;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;

public class Element {
    public String setstrg;
    public ClickGUI clickgui;
    public boolean comboextended;
    public double width;
    public double offset;
    public double x;
    public Setting set;
    public double height;
    public double y;
    public ModuleButton parent;

    public void mouseReleased(int n, int n2, int n3) {
    }

    public void setup() {
        this.clickgui = this.parent.parent.clickgui;
    }

    public void drawScreen(int n, int n2, float f) {
    }

    public boolean mouseClicked(int n, int n2, int n3) {
        return this.isHovered(n, n2);
    }

    public void update() {
        this.x = this.parent.x + this.parent.width;
        this.y = this.parent.y + this.offset;
        this.width = this.parent.width + 6.0;
        this.height = 15.0;
        String string = this.set.getName();
        if (this.set.isCheck()) {
            this.setstrg = String.valueOf(string.substring(0, 1).toUpperCase()) + string.substring(1, string.length());
            double d = this.x + this.width - (double)FontUtil.getStringWidth(this.setstrg);
            if (d < this.x + 13.0) {
                this.width += this.x + 13.0 - d + 1.0;
            }
        } else if (this.set.isCombo()) {
            this.height = this.comboextended ? this.set.getOptions().size() * (FontUtil.getFontHeight() + 2) + 15 : 15;
            this.setstrg = String.valueOf(string.substring(0, 1).toUpperCase()) + string.substring(1, string.length());
            int n = FontUtil.getStringWidth(this.setstrg);
            for (String string2 : this.set.getOptions()) {
                int n2 = FontUtil.getStringWidth(string2);
                if (n2 <= n) continue;
                n = n2;
            }
            double d = this.x + this.width - (double)n;
            if (d < this.x) {
                this.width += this.x - d + 1.0;
            }
        } else if (this.set.isSlider()) {
            this.setstrg = String.valueOf(string.substring(0, 1).toUpperCase()) + string.substring(1, string.length());
            String string3 = "" + (double)Math.round(this.set.getValDouble() * 100.0) / 100.0;
            String string4 = "" + (double)Math.round(this.set.getMax() * 100.0) / 100.0;
            double d = this.x + this.width - (double)FontUtil.getStringWidth(this.setstrg) - (double)FontUtil.getStringWidth(string4) + 90.0;
            if (d < this.x) {
                this.width += this.x - d + 1.0;
            }
        }
    }

    public boolean isHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.width && (double)n2 >= this.y && (double)n2 <= this.y + this.height;
    }
}

