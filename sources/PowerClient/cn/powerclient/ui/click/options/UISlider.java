/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click.options;

import java.awt.Color;
import me.AveReborn.Client;
import me.AveReborn.util.Colors;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import net.minecraft.client.gui.Gui;

public class UISlider {
    private int height = 1;
    public int width = 100;
    public boolean drag;
    private Color color;
    private float lastMouseX = -1.0f;
    private int x2;
    private int y2;
    private String name;
    private double min;
    private double max;
    private double step;

    public UISlider(String valueName, double min, double max, double step) {
        this.name = valueName;
        this.min = min;
        this.max = max;
        this.step = step;
        this.lastMouseX = -1.0f;
    }

    public UISlider(String valueName, double min, double max, double step, int width) {
        this.name = valueName;
        this.width = width;
        this.min = min;
        this.max = max;
        this.step = step;
        this.lastMouseX = -1.0f;
    }

    public double draw(float value, int mouseX, int mouseY, int x2, int y2) {
        this.height = 2;
        UnicodeFontRenderer font = Client.instance.fontMgr.verdana12;
        String strValue = String.valueOf(String.valueOf(String.valueOf(this.name)) + " " + value);
        float strWidth = font.getStringWidth(this.name);
        float strHeight = font.getStringHeight(this.name);
        float vWidth = font.getStringWidth(strValue);
        float vheight = font.getStringHeight(strValue);
        Gui.drawRect(x2, y2, x2 + this.width, y2 + this.height, Colors.GREY.c);
        font.drawString(strValue, (float)(x2 + this.width / 2) - vWidth / 2.0f + 0.5f, (float)(y2 + this.height) + 2.5f, Colors.BLACK.c);
        this.x2 = x2;
        this.y2 = y2;
        return this.changeValue(value, mouseX, mouseY, x2, y2);
    }

    public boolean mouseClick(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY)) {
            this.drag = true;
        }
        return this.drag;
    }

    public void mouseRelease() {
        this.drag = false;
    }

    private double changeValue(float value, int mouseX, int mouseY, int x2, int y2) {
        double valAbs = mouseX - x2;
        double perc = valAbs / (double)this.width;
        perc = Math.min(Math.max(0.0, perc), 1.0);
        double valRel = (this.max - this.min) * perc;
        double valuu = this.min + valRel;
        double percSlider = ((double)value - this.min) / (this.max - this.min);
        double val = (double)x2 + (double)this.width * percSlider;
        Gui.drawRect((float)x2, (float)y2, this.lastMouseX == -1.0f ? (float)((int)val) : (float)x2 + (float)this.width * this.lastMouseX, (float)(y2 + this.height), new Color(51, 102, 205).getRGB());
        Gui.circle((float)val, (float)(y2 + 1), 2.0f, new Color(51, 102, 205).getRGB());
        if (this.drag) {
            this.lastMouseX = ((float)Math.min(Math.max(x2, mouseX), x2 + this.width) - (float)x2) / (float)this.width;
            valuu = (double)Math.round(valuu * (1.0 / this.step)) / (1.0 / this.step);
            return valuu;
        }
        return (double)Math.round((double)value * (1.0 / this.step)) / (1.0 / this.step);
    }

    public boolean isHovering(int mouseX, int mouseY) {
        if (mouseX >= this.x2 && mouseY >= this.y2 && mouseX <= this.x2 + this.width && mouseY < this.y2 + this.height) {
            return true;
        }
        return false;
    }
}

