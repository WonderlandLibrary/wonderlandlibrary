package rip.helium.ui.main.components.tab.cheat;

import rip.helium.cheat.impl.visual.Hud;
import rip.helium.ui.main.Interface;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;
import rip.helium.utils.property.abs.Property;
import rip.helium.utils.property.impl.StringsProperty;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author antja03
 */
public class PropertyComboBox extends PropertyComponent {

    private StringsProperty stringsProperty;
    private boolean extended;

    public PropertyComboBox(Interface theInterface, Property property, double x, double y, double width, double height) {
        super(theInterface, property, x, y, width, height);
        if (property instanceof StringsProperty) {
            stringsProperty = (StringsProperty) property;
        }
    }

    @Override
    public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        int keyCount = stringsProperty.getValue().keySet().size();
        double boxPosX = x + 80;
        double boxPosY = y + 2;
        double boxWidth = 60;
        double boxHeight = 10;

        Fonts.f14.drawString(getProperty().getId(), x + 6, y + maxHeight / 2 - 5.5, theInterface.getColor(255, 255, 255));

        if (extended)
            boxHeight += 10 * (keyCount - 1);

        Draw.drawBorderedRectangle(boxPosX, boxPosY, boxPosX + boxWidth, boxPosY + boxHeight, 0.5, new Color(32, 31, 32).getRGB(), new Color(54, 56, 56, 175).getRGB(), true);

        if (extended) {
            ArrayList<String> options = getUnorderedList();
            for (String option : options) {
                double optionPosX = boxPosX;
                double optionPosY = boxPosY + options.indexOf(option) * 10;
                double optionWidth = boxWidth;
                double optionHeight = 10;

                if (stringsProperty.isSelected(option))
                    Draw.drawRectangle(optionPosX, optionPosY, optionPosX + optionWidth, optionPosY + optionHeight, Hud.prop_color.getValue().getRGB());

                Fonts.f12.drawCenteredString(option, optionPosX + optionWidth / 2, optionPosY + optionHeight / 2 - 1, new Color(230, 230, 230, 230).getRGB());
            }
        } else {
            Fonts.f12.drawCenteredString(getBoxLabel(), boxPosX + boxWidth / 2, boxPosY + boxHeight / 2 - 1, theInterface.getColor(255, 255, 255));
        }

        if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + positionX + 4,
                theInterface.getPositionX() + positionX + 8 + Fonts.f12.getStringWidth(getProperty().getId()),
                theInterface.getPositionY() + positionY + maxHeight / 2 - 7.5,
                theInterface.getPositionY() + positionY + maxHeight / 2 - 3.5 + Fonts.f12.getStringHeight(getProperty().getId()))) {

            String desc = getProperty().getDescription();
            ArrayList<String> list = new ArrayList<>(Fonts.f14.wrapWords(desc, 102));
            if (list.size() > 1) {
                double boxWidth2 = -1;
                for (String string : list) {
                    if (Fonts.f12.getStringWidth(string) > boxWidth2) {
                        boxWidth2 = Fonts.f14.getStringWidth(string);
                    }
                }
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth2, theInterface.getCurrentFrameMouseY() + 8 * list.size(), theInterface.getColor(40, 40, 40));
                for (String string : list) {
                    Fonts.f14.drawString(string, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2 + list.indexOf(string) * 8, theInterface.getColor(255, 255, 255));
                }
            } else {
                double boxWidth2 = Fonts.f12.getStringWidth(desc);
                Draw.drawRectangle(theInterface.getCurrentFrameMouseX() + 4, theInterface.getCurrentFrameMouseY(),
                        theInterface.getCurrentFrameMouseX() + 4 + boxWidth2, theInterface.getCurrentFrameMouseY(), theInterface.getColor(40, 40, 40));
                Fonts.f12.drawString(desc, theInterface.getCurrentFrameMouseX() + 5, theInterface.getCurrentFrameMouseY() + 2, theInterface.getColor(255, 255, 255));
            }
        }
    }

    @Override
    public boolean mouseButtonClicked(int button) {
        StringsProperty stringsProperty = (StringsProperty) getProperty();
        int keyCount = stringsProperty.getValue().keySet().size();
        double boxPosX = positionX + 80;
        double boxPosY = positionY + 2;
        double boxWidth = 60;
        double boxHeight = 10;
        if (!extended) {
            if (theInterface.isMouseInBounds(
                    theInterface.getPositionX() + boxPosX,
                    theInterface.getPositionX() + boxPosX + boxWidth,
                    theInterface.getPositionY() + boxPosY,
                    theInterface.getPositionY() + boxPosY + boxHeight)) {
                extended = true;
                return true;
            }
        } else {
            ArrayList<String> options = getUnorderedList();
            for (String string : options) {
                double optionPosY = boxPosY + options.indexOf(string) * 10;
                if (theInterface.isMouseInBounds(
                        theInterface.getPositionX() + boxPosX,
                        theInterface.getPositionX() + boxPosX + boxWidth,
                        theInterface.getPositionY() + optionPosY,
                        theInterface.getPositionY() + optionPosY + 10)) {
                    stringsProperty.setOption(string + ":" + !stringsProperty.getValue().get(string));
                    if (!stringsProperty.canMultiselect()) {
                        extended = false;
                    }
                    return true;
                }
            }
        }
        extended = false;
        return false;
    }

    public void onGuiClose() {
        extended = false;
    }

    public String getBoxLabel() {
        String finalString = "";
        if (stringsProperty.getSelectedStrings().isEmpty())
            return finalString;

        if (stringsProperty.canMultiselect()) {
            for (String string : stringsProperty.getSelectedStrings()) {
                finalString += string + ", ";
            }
            finalString = finalString.substring(0, finalString.length() - 2);
            ArrayList<String> strings = new ArrayList<>(Fonts.f14.wrapWords(finalString, 80));
            finalString = Fonts.f14.wrapWords(finalString, 80).get(0);
            if (strings.size() > 1) {
                finalString = finalString.substring(0, finalString.length() - 3);
                finalString += "...";
            }
        } else {
            finalString = stringsProperty.getSelectedStrings().get(0);
        }

        return finalString;
    }

    public ArrayList<String> getUnorderedList() {
        StringsProperty stringsProperty = (StringsProperty) getProperty();
        return new ArrayList<>(stringsProperty.getValue().keySet());
    }

    public ArrayList<String> getOrderedList() {
        StringsProperty stringsProperty = (StringsProperty) getProperty();
        ArrayList<String> orderedList = new ArrayList<>();
        if (!stringsProperty.canMultiselect()) {
            orderedList.add(stringsProperty.getSelectedStrings().get(0));
        }
        for (String string : stringsProperty.getValue().keySet()) {
            if ((!stringsProperty.canMultiselect() && !string.equalsIgnoreCase(orderedList.get(0))) || stringsProperty.canMultiselect()) {
                orderedList.add(string);
            }
        }
        return orderedList;
    }

}
