/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click.options;

import java.awt.Color;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.util.Colors;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;

public class UIToggleButton {
    private Value value;
    private MouseInputHandler handler;
    public int width;
    private int height;
    private int lastX;
    private float animationX = 2.14748365E9f;

    public UIToggleButton(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x2, int y2) {
        UnicodeFontRenderer font = Client.instance.fontMgr.simpleton13;
        int radius = 4;
        String strValue = this.value.getValueName().split("_")[1];
        boolean enabled = (Boolean)this.value.getValueState();
        int color = enabled ? new Color(51, 102, 205).getRGB() : Colors.GREY.c;
        this.animate(x2, mouseY, radius, enabled);
        this.toggle(mouseX, mouseY, x2, y2, radius);
        this.drawToggleButton(x2, y2, radius, color, enabled);
        font.drawString(strValue, (float)(x2 + 5) + 0.5f, (float)y2 + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c);
        this.lastX = x2;
    }

    private void drawToggleButton(int x2, int y2, int radius, int color, boolean enabled) {
        float xMid = x2 + this.width - radius * 2 - 3;
        float yMid = (float)y2 + (float)(this.height - radius) / 2.0f + 2.0f;
        Gui.drawRect(xMid - (float)radius - 0.5f, yMid - (float)radius + 3.5f, xMid + (float)radius + 0.5f, yMid + (float)radius - 2.5f, color);
        Gui.circle(this.animationX, yMid + 0.5f, (float)radius - 1.0f, new Color(51, 102, 205).getRGB());
    }

    private void animate(int x2, int y2, int radius, boolean enabled) {
        float xMid = x2 + this.width - radius * 2 - 3;
        float yMid = (float)y2 + (float)(this.height - radius) / 2.0f - 3.0f;
        float xEnabled = !enabled ? xMid - (float)radius + 0.25f : xMid + (float)radius - 0.25f;
        float f2 = xEnabled;
        if (this.lastX != x2) {
            this.animationX = xEnabled;
        }
        this.animationX = this.animationX == 2.14748365E9f ? xEnabled : (float)RenderUtil.getAnimationState(this.animationX, xEnabled, 50.0);
    }

    private void toggle(int mouseX, int mouseY, int x2, int y2, int radius) {
        if (this.isHovering(mouseX, mouseY, x2, y2, radius) && this.handler.canExcecute()) {
            this.value.setValueState((Boolean)this.value.getValueState() == false);
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x2, int y2, int radius) {
        float xMid = (float)x2 + (float)(this.width - radius) / 2.0f;
        float yMid = (float)y2 + (float)(this.height - radius) / 2.0f;
        if (mouseX >= x2 && mouseY >= y2 && mouseX <= x2 + this.width && mouseY < y2 + this.height) {
            return true;
        }
        return false;
    }
}

