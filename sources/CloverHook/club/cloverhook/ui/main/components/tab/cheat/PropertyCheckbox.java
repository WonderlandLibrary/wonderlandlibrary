package club.cloverhook.ui.main.components.tab.cheat;

import java.util.ArrayList;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.Fonts;
import club.cloverhook.utils.property.abs.Property;
import club.cloverhook.utils.property.impl.BooleanProperty;

/**
 * @author antja03
 */
public class PropertyCheckbox extends PropertyComponent {

    public PropertyCheckbox(Interface theInterface, Property value, double x, double y, double width, double height) {
        super(theInterface, value, x, y, width, height);
    }

    @Override public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        Draw.drawRectangle(x + maxWidth - 18, y + 3, x + maxWidth - 10, y + 11, theInterface.getColor(50, 50, 50));
        if (((BooleanProperty)getProperty()).getValue()) {
            Draw.drawRectangle(x + maxWidth - 17, y + 4, x + maxWidth - 11, y + 10, theInterface.getColor(0, 215, 64));
        }

        Fonts.f14.drawString(getProperty().getId(), x + 6, y + maxHeight / 2 - 5.5, theInterface.getColor(255, 255, 255));

        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + 4,
                theInterface.getPositionX() + positionX + 8 + Fonts.f12.getStringWidth(getProperty().getId()),
                theInterface.getPositionY() + positionY + maxHeight / 2 - 7.5,
                theInterface.getPositionY() + positionY + maxHeight / 2 - 3.5 + Fonts.f12.getStringHeight(getProperty().getId()))) {

            String desc = getProperty().getDescription();
            ArrayList<String> list = new ArrayList<> (Fonts.f14.wrapWords(desc, 102));
            if (list.size() > 1) {
                double boxWidth = -1;
                for (String string : list) {
                    if (Fonts.f12.getStringWidth(string) > boxWidth) {
                        boxWidth = Fonts.f14.getStringWidth(string);
                    }
                }
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth, theInterface.getCurrentFrameMouseY() + 8 * list.size(), theInterface.getColor(40, 40, 40));
                for (String string : list) {
                    Fonts.f12.drawString(string, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2 + list.indexOf(string) * 8, theInterface.getColor(255, 255, 255));
                }
            } else {
                double boxWidth = Fonts.f12.getStringWidth(desc);
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth, theInterface.getCurrentFrameMouseY(), theInterface.getColor(40, 40, 40));
                Fonts.f12.drawString(desc, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2, theInterface.getColor(255, 255, 255));
            }
        }
    }

    @Override public boolean mouseButtonClicked(int button) {
        if (button == 0 && theInterface.isMouseInBounds(
                theInterface.getPositionX() + this.positionX + maxWidth - 18,
                theInterface.getPositionX() + this.positionX + maxWidth - 10,
                theInterface.getPositionY() + this.positionY + 3,
                theInterface.getPositionY() + this.positionY + 11)) {
            getProperty().setValue(!(((BooleanProperty)getProperty()).getValue()));
            return true;
        }
        return false;
    }
}
