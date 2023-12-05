package rip.helium.ui.main.components.simple;

import net.minecraft.util.ResourceLocation;
import rip.helium.Helium;
import rip.helium.ui.main.Interface;
import rip.helium.ui.main.components.base.BaseButton;
import rip.helium.utils.Draw;
import rip.helium.utils.font.FontRenderer;
import rip.helium.utils.font.Fonts;

import java.awt.*;

/**
 * @author antja03
 */
public class SimpleTextButton extends BaseButton {

    private final Color buttonColor;
    private final Color buttonColorHovered;
    private final Color textColor;
    private final String text;
    private final FontRenderer fontRenderer;

    public SimpleTextButton(Interface theInterface, Color buttonColor, Color buttonColorHovered, String text, int fontSize, Color textColor, double positionX, double positionY, double maxWidth, double maxHeight, Action action) {
        super(theInterface, positionX, positionY, maxWidth, maxHeight, action);
        this.buttonColor = buttonColor;
        this.buttonColorHovered = buttonColorHovered;
        this.text = text;
        this.textColor = textColor;
        this.fontRenderer = new FontRenderer(Fonts.fontFromTTF(new ResourceLocation("client/verdana.ttf"), fontSize, Font.PLAIN), true, true);
    }

    public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        int color = isMouseOver() ?
                Helium.instance.userInterface.theInterface.getColor(buttonColorHovered.getRed(), buttonColorHovered.getGreen(), buttonColorHovered.getBlue(), buttonColorHovered.getAlpha())
                : Helium.instance.userInterface.theInterface.getColor(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), buttonColor.getAlpha());

        Draw.drawBorderedRectangle(x, y, x + maxWidth, y + maxHeight, 0.5, new Color(32, 31, 32).getRGB(), new Color(54, 56, 56, 175).getRGB(), true);
        fontRenderer.drawCenteredString(text, x + maxWidth / 2, y + (maxHeight / 2) - (fontRenderer.getStringHeight(text) / 2),
                Helium.instance.userInterface.theInterface.getColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textColor.getAlpha()));
    }

}
