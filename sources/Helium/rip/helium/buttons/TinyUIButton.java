package rip.helium.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class TinyUIButton extends GuiButton {
    int y = 0;

    private int fade;


    public TinyUIButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);

    }

    public TinyUIButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);

            if (!hovered) {
                if (this.fade != 100) {
                    this.fade += 5;
                }
            } else {
                if (this.fade <= 40) {
                    return;
                }
                if (this.fade != 70) {
                    this.fade -= 5;
                }
            }
            final Color a = new Color(255, 255, 255, this.fade);
            int i = 0;
            final FontRenderer var4 = mc.fontRendererObj;
            Gui.drawRect(this.xPosition + 70, this.yPosition, this.xPosition - 70 + this.width, this.yPosition + this.height, a.getRGB());
            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, new Color(255, 255, 255).getRGB());

            i += 12;
        }
    }
}