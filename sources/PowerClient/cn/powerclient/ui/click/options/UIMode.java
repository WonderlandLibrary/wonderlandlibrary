/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click.options;

import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.Value;
import me.AveReborn.util.ClientUtil;
import me.AveReborn.util.Colors;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;

public class UIMode {
    private int height;
    public int width;
    private Value value;
    private MouseInputHandler handler;

    public UIMode(Value value, MouseInputHandler handler, int width, int height) {
        this.value = value;
        this.handler = handler;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY, int x2, int y2) {
        this.setNextMode(mouseX, mouseY, x2, y2);
        UnicodeFontRenderer font = Client.instance.fontMgr.verdana12;
        String displayText = String.valueOf(String.valueOf(this.value.getModeTitle())) + " " + this.value.getModeAt(this.value.getCurrentMode());
        String modeCountText = String.valueOf(String.valueOf(this.value.getCurrentMode() + 1)) + "/" + this.value.mode.size();
        if (this.isHovering(mouseX, mouseY, x2, y2)) {
            Gui.drawRect(x2, y2, x2 + this.width, y2 + this.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.35f));
        }
        font.drawString(displayText, (float)x2 + (float)(this.width - font.getStringWidth(displayText)) / 2.0f + 0.5f, (float)y2 + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c);
        font.drawString(modeCountText, (float)(x2 + this.width - font.getStringWidth(modeCountText) - 4) + 0.5f, (float)y2 + (float)(this.height - font.FONT_HEIGHT) / 2.0f + 0.5f, Colors.BLACK.c);
    }

    private void setNextMode(int mouseX, int mouseY, int x2, int y2) {
        if (this.isHovering(mouseX, mouseY, x2, y2) && this.handler.canExcecute()) {
            if (this.value.getCurrentMode() < this.value.mode.size() - 1) {
                this.value.setCurrentMode(this.value.getCurrentMode() + 1);
            } else {
                this.value.setCurrentMode(0);
            }
        }
    }

    public boolean isHovering(int mouseX, int mouseY, int x2, int y2) {
        if (mouseX >= x2 && mouseY >= y2 && mouseX <= x2 + this.width && mouseY < y2 + this.height) {
            return true;
        }
        return false;
    }
}

