package me.aquavit.liquidsense.ui.client.gui.elements;

import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiButtonElement extends GuiButton {

    public GuiButtonElement(final int buttonId, final int x, final int y, final int width, final int height, final String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            Color color = new Color(225,225,225,225);
            FontRenderer fontrenderer = Fonts.font20;
            hovered = (mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height);

            RenderUtils.drawButtonRect(xPosition, yPosition, xPosition + width, yPosition + height, 6, new Color(1,1,1,80).getRGB());

            if (hovered) {
                GL11.glPushMatrix();
                RenderUtils.color(color.darker().getRGB());
                GL11.glPopMatrix();
            }

            mouseDragged(mc, mouseX, mouseY);
            int stringColor = new Color(255,255,255,200).getRGB();

            if (hovered)
                stringColor = color.darker().getRGB();

            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 6) / 2, stringColor);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
}
