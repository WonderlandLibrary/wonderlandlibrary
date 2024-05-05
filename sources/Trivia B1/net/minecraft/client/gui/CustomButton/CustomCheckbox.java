package net.minecraft.client.gui.CustomButton;


import org.darkstorm.minecraft.gui.util.RenderUtil;

import me.aidanmees.trivia.client.tools.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class CustomCheckbox
extends GuiButton {
    private boolean isChecked;
    private int boxWidth;

    public CustomCheckbox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
        super(id, xPos, yPos, displayString);
        this.isChecked = isChecked;
        this.boxWidth = 11;
        this.height = 11;
        this.width = this.boxWidth + 2 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(displayString);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.boxWidth && mouseY < this.yPosition + this.height;
            RenderUtils.drawVerticalLine(this.xPosition, this.yPosition, this.yPosition + this.height - 1);
            RenderUtils.drawVerticalLine(this.xPosition + this.boxWidth - 1, this.yPosition, this.yPosition + this.height - 1);
            RenderUtils.drawHorizontalLine(this.xPosition, this.xPosition + this.boxWidth - 1, this.yPosition);
            RenderUtils.drawHorizontalLine(this.xPosition, this.xPosition + this.boxWidth - 1, this.yPosition + this.height - 1);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;
            if (!this.enabled) {
                color = 10526880;
            }
            if (this.isChecked) {
                this.drawCenteredString(mc.fontRendererObj, "x", this.xPosition + this.boxWidth / 2 + 1, this.yPosition + 1, 14737632);
            }
            this.drawString(mc.fontRendererObj, this.displayString, this.xPosition + this.boxWidth + 2, this.yPosition + 2, color);
        }
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height) {
            this.isChecked = !this.isChecked;
            return true;
        }
        return false;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}

