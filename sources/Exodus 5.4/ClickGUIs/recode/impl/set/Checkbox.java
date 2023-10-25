/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl.set;

import ClickGUIs.recode.impl.Button;
import ClickGUIs.recode.impl.set.SetComp;
import de.Hero.settings.Setting;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Checkbox
extends SetComp {
    private double x;
    private float lastX2 = -0.5f;
    private boolean hovered;
    private double y;
    private boolean dragging = false;
    private float blue = 0.7058824f;
    private float lastX1 = 1.5f;
    private static double height = 13.0;
    private float red = 0.7058824f;
    private Setting set;
    private float green = 0.7058824f;

    public Checkbox(Setting setting, Button button) {
        super(setting, button, height);
        this.set = setting;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (n3 == 0 && this.hovered) {
            this.set.setValBoolean(!this.set.getValBoolean());
        }
    }

    @Override
    public double drawScreen(int n, int n2, double d, double d2) {
        this.hovered = this.isHovered(n, n2);
        this.x = d;
        this.y = d2;
        Gui.drawRect(d, d2, d + this.parent.getWidth(), d2 + height + 1.0, CustomIngameGui.color);
        Gui.drawRect(this.x + 1.0, this.y, this.x + this.parent.getWidth() - 1.0, this.y + height, CustomIngameGui.color);
        String string = this.set.getName();
        FontUtil.normal.drawString(string, (float)(this.x + 3.0), (float)(d2 + (double)(FontUtil.normal.getHeight() / 2) + 0.0), -1);
        Gui.drawRect(this.x + this.parent.getWidth() - 12.0, this.y + 4.0, this.x + this.parent.getWidth() - 3.0, this.y + 12.0, this.set.getValBoolean() ? -1 : CustomIngameGui.color);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        float f = this.set.getValBoolean() ? 5.0f : 1.5f;
        float f2 = this.set.getValBoolean() ? 3.0f : -0.5f;
        float f3 = f - this.lastX1;
        float f4 = f2 - this.lastX2;
        this.lastX1 += f3 / 4.0f;
        this.lastX2 += f4 / 4.0f;
        return height;
    }

    private boolean isHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.parent.getWidth() && (double)n2 > this.y && (double)n2 < this.y + height;
    }
}

