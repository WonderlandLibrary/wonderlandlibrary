package org.dreamcore.client.ui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.dreamcore.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class GuiAltButton extends GuiButton {

    private int fade = 20;
    private int fadeOutline = 20;

    public GuiAltButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiAltButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float mouseButton) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= (this.yPosition) && mouseX < this.xPosition + this.width && mouseY < (this.yPosition) + this.height);
            Color text = new Color(215, 215, 215, 255);
            Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 120);
            if (this.hovered) {
                if (this.fade < 100) {
                    this.fade += 7;
                }
                text = Color.white;
            } else {
                if (this.fade > 20) {
                    this.fade -= 7;
                }
            }

            Color colorOutline = new Color(this.fade + 60, this.fade, this.fade, 255);
            if (this.hovered) {
                if (this.fadeOutline < 100) {
                    this.fadeOutline += 7;
                }
            } else {
                if (this.fadeOutline > 20) {
                    this.fadeOutline -= 7;
                }
            }

            RectHelper.drawOutlineRect(this.xPosition, this.yPosition, this.width, this.height, color, colorOutline);
            mc.circleregular.drawCenteredString(this.displayString, this.xPosition + this.width / 2F, this.yPosition + (this.height - 7.5f) / 2F, text.getRGB());
        }
    }
}