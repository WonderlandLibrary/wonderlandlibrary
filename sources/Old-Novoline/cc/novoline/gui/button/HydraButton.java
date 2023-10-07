package cc.novoline.gui.button;

import cc.novoline.utils.fonts.impl.Fonts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class HydraButton extends GuiButton {

    private int color;

    public HydraButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }


    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            Gui.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, color);
            Fonts.SF.SF_20.SF_20.drawString(displayString, (float) xPosition + width / 2 - Fonts.SF.SF_20.SF_20.stringWidth(displayString) / 2, (float) yPosition + height / 2 - Fonts.SF.SF_20.SF_20.getHeight() / 2, Color.WHITE.getRGB(), true);
        }
    }

    public void updateCoordinates(float x, float y) {
        xPosition = x;
        yPosition = y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}