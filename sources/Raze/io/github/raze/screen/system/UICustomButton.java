package io.github.raze.screen.system;

import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.visual.RoundUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class UICustomButton extends GuiButton {

    private final Color backgroundColor;
    private final Color highlightedColor;
    private final Color textColor;
    public int xPosition;
    public int yPosition;
    public int width;
    public int height;
    private boolean pressed;

    public UICustomButton(int buttonId, int x, int y, int width, int height, String text, Color backgroundColor, Color highlightedColor, Color textColor) {
        super(buttonId, x, y, width, height, text);
        this.backgroundColor = backgroundColor;
        this.highlightedColor = highlightedColor;
        this.textColor = textColor;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.pressed = false;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawButton(int mouseX, int mouseY) {
        if (this.visible) {
            boolean isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            int backgroundColorRGB;
            if (this.pressed && isHovered) {
                backgroundColorRGB = this.highlightedColor != null ? this.highlightedColor.getRGB() : this.backgroundColor.getRGB();
            } else if (isHovered) {
                backgroundColorRGB = this.highlightedColor != null ? this.highlightedColor.brighter().getRGB() : this.backgroundColor.brighter().getRGB();
            } else {
                backgroundColorRGB = this.backgroundColor.getRGB();
            }

            RoundUtil.drawSmoothRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, 8, backgroundColorRGB);

            Color color = this.enabled ? this.textColor : Color.GRAY;
            CFontUtil.Jello_Light_16.getRenderer().drawString(this.displayString, this.xPosition + (double) (this.width - CFontUtil.Jello_Light_16.getRenderer().getStringWidth(this.displayString)) / 2, this.yPosition + (double) (this.height - CFontUtil.Jello_Light_16.getRenderer().getHeight()) / 2, color);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean result = super.mousePressed(mc, mouseX, mouseY);
        if (result) {
            this.pressed = true;
            return true;
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY) {
        super.mouseReleased(mouseX, mouseY);
        this.pressed = false;
    }
}
