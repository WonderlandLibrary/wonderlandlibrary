/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl.set;

import ClickGUIs.recode.impl.Button;
import ClickGUIs.recode.impl.set.SetComp;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.text.DecimalFormat;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Slider
extends SetComp {
    private double y;
    private boolean dragging = false;
    private Setting set;
    private boolean hovered;
    private double x;
    private static double height = 13.0;

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.hovered) {
            this.dragging = !this.dragging;
        }
    }

    public Slider(Setting setting, Button button) {
        super(setting, button, height);
        this.set = setting;
    }

    @Override
    public double drawScreen(int n, int n2, double d, double d2) {
        float f;
        this.hovered = this.isHovered(n, n2);
        this.x = d;
        this.y = d2;
        height = 12.425;
        Gui.drawRect(d, d2, d + this.parent.getWidth(), d2 + height + 1.0, CustomIngameGui.color);
        if (this.dragging) {
            f = ((float)n - (float)(this.x - 2.0)) / (float)(this.parent.getWidth() - 1.0);
            if (f > 1.0f) {
                f = 1.0f;
            }
            if (f < 0.0f) {
                f = 0.0f;
            }
            this.set.setValDouble((this.set.getMax() - this.set.getMin()) * (double)f + this.set.getMin());
        }
        f = (float)((this.set.getValDouble() - this.set.getMin()) / (this.set.getMax() - this.set.getMin()));
        Gui.drawRect(this.x, this.y, this.x + this.parent.getWidth(), this.y + height, new Color(21, 21, 21).getRGB());
        Gui.drawRect(this.x + 1.0, this.y + (double)FontUtil.normal.getHeight() - 8.0, (int)(this.x - 1.0 + this.parent.getWidth() * 1.0), this.y + height - 1.0, -1);
        String string = String.valueOf(this.set.getName()) + " " + new DecimalFormat("#.##").format(Math.floor(this.set.getValDouble()));
        int n3 = CustomIngameGui.color;
        Gui.drawRect(this.x + 1.0, this.y + (double)FontUtil.normal.getHeight() - 8.0, (int)(this.x - 1.0 + this.parent.getWidth() * (double)f), this.y + height - 1.0, this.hovered ? new Color(n3).darker().getRGB() : n3);
        Gui.drawRect(this.x + 1.0, this.y - 1.0, (int)(this.x - 1.0 + this.parent.getWidth() * 1.0), this.y, CustomIngameGui.color);
        GlStateManager.pushMatrix();
        float f2 = 1.0f;
        GlStateManager.scale(f2, f2, f2);
        FontUtil.normal.drawString(string, (float)((this.x + 2.0) / (double)f2 + 2.0), (float)((d2 + 2.0) / (double)f2), -1);
        GlStateManager.popMatrix();
        return height;
    }

    private boolean isHovered(int n, int n2) {
        return (double)n >= this.x + 1.0 && (double)n <= this.x + 10.0 + this.parent.getWidth() && (double)n2 >= this.y && (double)n2 <= this.y + height;
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        if (n3 == 0) {
            this.dragging = false;
        }
    }
}

