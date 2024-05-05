package club.cloverhook.ui.main.components.simple;

import net.minecraft.util.ResourceLocation;

import java.awt.*;

import club.cloverhook.Cloverhook;
import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.base.BaseButton;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.FontRenderer;
import club.cloverhook.utils.font.Fonts;

/**
 * @author antja03
 */
public class SimpleTextButton extends BaseButton {

    private Color buttonColor;
    private Color buttonColorHovered;
    private Color textColor;
    private String text;
    private FontRenderer fontRenderer;

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
                Cloverhook.instance.userInterface.theInterface.getColor(buttonColorHovered.getRed(), buttonColorHovered.getGreen(), buttonColorHovered.getBlue(), buttonColorHovered.getAlpha())
                : Cloverhook.instance.userInterface.theInterface.getColor(buttonColor.getRed(), buttonColor.getGreen(), buttonColor.getBlue(), buttonColor.getAlpha());

        Draw.drawRectangle(x, y, x + maxWidth, y + maxHeight, color);
        fontRenderer.drawCenteredString(text, x + maxWidth / 2, y + (maxHeight / 2) - (fontRenderer.getStringHeight(text) / 2),
                Cloverhook.instance.userInterface.theInterface.getColor(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), textColor.getAlpha()));
    }

}
