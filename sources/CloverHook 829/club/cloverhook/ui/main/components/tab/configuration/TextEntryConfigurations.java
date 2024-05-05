package club.cloverhook.ui.main.components.tab.configuration;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.ui.main.components.base.BaseTextEntry;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.FontRenderer;
import club.cloverhook.utils.font.Fonts;

/**
 * @author antja03
 */
public class TextEntryConfigurations extends BaseTextEntry {

    public TextEntryConfigurations(Interface theInterface, FontRenderer fontRenderer, double x, double y, double width, double height) {
        super(theInterface, fontRenderer, x, y, width, height);
    }

    @Override
    public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        Draw.drawRectangle(x, y, x + maxWidth, y + maxHeight, theInterface.getColor(35, 35, 35));
        Fonts.f12.drawString(getContent(), x + 2, y + 3, theInterface.getColor(200, 200, 200));
        if (focused)
            Draw.drawRectangle(x + 2 + Fonts.f12.getStringWidth(getContent()), y + 7, x + 2 + Fonts.f12.getStringWidth(getContent() + 2), y + 7.5, theInterface.getColor(230, 230, 230));
    }

    @Override
    public void execute() {
        this.focused = false;
    }

}
