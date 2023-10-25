/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.recode.impl.set;

import ClickGUIs.recode.impl.Button;
import ClickGUIs.recode.impl.set.SetComp;
import de.Hero.settings.Setting;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.gui.Gui;

public class Mode
extends SetComp {
    private static int height = 13;
    private boolean dragging = false;
    private boolean isOpened = false;
    private double y;
    private double x;
    private Setting set;
    private boolean hovered;

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if ((n3 == 0 || n3 == 1) && this.hovered) {
            List<String> list = Arrays.asList(this.set.getValString());
            int n4 = list.indexOf(this.set.getValString());
            if (n3 == 0) {
                ++n4;
            } else if (n3 == 1) {
                --n4;
            }
            if (n4 >= list.size()) {
                n4 = 0;
            } else if (n4 < 0) {
                n4 = list.size() - 1;
            }
            this.set.setValString(this.set.getValString());
        }
    }

    public Mode(Setting setting, Button button) {
        super(setting, button, height);
        this.set = setting;
    }

    @Override
    public double drawScreen(int n, int n2, double d, double d2) {
        this.hovered = this.isHovered(n, n2);
        this.x = d;
        this.y = d2;
        String string = String.valueOf(this.set.getName());
        Gui.drawRect(d, d2, d + this.parent.getWidth(), d2 + (double)height + 1.0, -1);
        Gui.drawRect(this.x + 1.0, this.y, this.x - 1.0 + this.parent.getWidth(), this.y + (double)height, CustomIngameGui.color);
        FontUtil.normal.drawString(string, (int)(this.x + 3.0), (int)(d2 + (double)(FontUtil.normal.getHeight() / 2) + 0.0), new Color(255, 255, 255).darker().getRGB());
        FontUtil.normal.drawString(this.set.getValString(), (int)(this.x + 100.0 - FontUtil.normal.getStringWidth(this.set.getValString()) - 3.0), (int)(d2 + (double)(FontUtil.normal.getHeight() / 2) + 0.0), -1);
        return height;
    }

    private boolean isHovered(int n, int n2) {
        return (double)n >= this.x && (double)n <= this.x + this.parent.getWidth() && (double)n2 >= this.y && (double)n2 <= this.y + (double)height;
    }
}

