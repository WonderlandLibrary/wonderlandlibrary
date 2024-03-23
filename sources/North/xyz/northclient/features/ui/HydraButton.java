package xyz.northclient.features.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import xyz.northclient.util.font.FontUtil;

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
            FontUtil.SFProMedium.drawString(displayString, (float) xPosition + width / 2 - FontUtil.SFProMedium.getWidth(displayString) / 2, (float) yPosition + height / 2 - FontUtil.SFProMedium.getHeight() / 2, Color.WHITE.getRGB());
        }
    }

    public void updateCoordinates(float x, float y) {
        xPosition = (int) x;
        yPosition = (int) y;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}