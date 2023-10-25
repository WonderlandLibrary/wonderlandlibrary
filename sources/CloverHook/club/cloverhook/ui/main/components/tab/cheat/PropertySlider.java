package club.cloverhook.ui.main.components.tab.cheat;

import org.lwjgl.input.Keyboard;

import club.cloverhook.ui.main.Interface;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.font.Fonts;
import club.cloverhook.utils.property.abs.Property;
import club.cloverhook.utils.property.impl.DoubleProperty;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class PropertySlider extends PropertyComponent {

    private boolean mouseDragging;
    private boolean typing;
    private String typedChars;
    private double widthOfSlider;
    private DoubleProperty property;
    private double currentPosition;
    private ArrayList<Double> possibleValues = new ArrayList<>();

    public PropertySlider(Interface theInterface, Property value, double x, double y, double width, double height) {
        super(theInterface, value, x, y, width, height);
        mouseDragging = false;
        typing = false;
        typedChars = "";
        widthOfSlider = (x + maxWidth - 10) - (x + 80);
        property = (DoubleProperty) value;
        currentPosition = (widthOfSlider / (property.getMaximum() - property.getMinimum())) * (property.getValue()- property.getMinimum());
    }

    @Override public void drawComponent(double x, double y) {
        this.positionX = x - theInterface.getPositionX();
        this.positionY = y - theInterface.getPositionY();

        /*
         * Drawing the component
         */
        {
            //Identifier
            Fonts.f14.drawString(getProperty().getId(), x + 6, y + maxHeight / 2 - 5.5, theInterface.getColor(255, 255, 255));
            //PropertySlider BG
            Draw.drawRectangle(x + 80, y + maxHeight / 2 - 6, x + maxWidth - 10, y + maxHeight / 2 + - 4, theInterface.getColor(50, 50, 50));
            //PropertySlider Fill
            Draw.drawRectangle(x + 80, y + maxHeight / 2 - 6, x + 80 + currentPosition, y + maxHeight / 2 - 4, theInterface.getColor(0, 215, 64));
            //Current
            if (typing) {
                Fonts.f12.drawCenteredString(typedChars + "_" + property.getNumType(), (float) (x + 80 + (widthOfSlider / 2)), (float) (y + maxHeight / 2 - 2), typing ? theInterface.getColor(0, 215, 64) : theInterface.getColor(220, 220, 220));
            } else {
                Fonts.f12.drawCenteredString(Mafs.round(property.getValue(), 2) + property.getNumType(), (float) (x + 80 + currentPosition), (float) (y + maxHeight / 2), typing ? theInterface.getColor(0, 215, 64) : theInterface.getColor(220, 220, 220));
            }
        }

        /*
         * Handling the property value
         */
        {
            if (theInterface.isClosing()) {
                mouseDragging = false;
                typing = false;
                return;
            }

            if (typing) {
                mouseDragging = false;
            }

            if (mouseDragging) {
                double cursorPosOnBar = theInterface.getCurrentFrameMouseX() - x - 80;
                currentPosition = Mafs.clamp(cursorPosOnBar, 0, widthOfSlider);
                double exactValue = Mafs.clamp(property.getMinimum() + ((property.getMaximum() - property.getMinimum()) * (cursorPosOnBar / widthOfSlider)), property.getMinimum(), property.getMaximum());

                if (possibleValues.isEmpty()) {
                    double current = property.getMinimum();
                    possibleValues.add(current);
                    while (current < property.getMaximum()) {
                        current += property.getIncrement();
                        possibleValues.add(current);
                    }
                }

                double bestValue = -1;
                for (Double value : possibleValues) {
                    if (bestValue == -1) {
                        bestValue = value;
                        continue;
                    } else {
                        if (Mafs.getDifference(exactValue, value) < Mafs.getDifference(exactValue, bestValue))
                            bestValue = value;
                    }
                }

                property.setValue(bestValue);
            } else {
                setPositionBasedOnValue();
                possibleValues.clear();
            }
        }

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
                        boxWidth = Fonts.f12.getStringWidth(string);
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

    @Override
    public boolean mouseButtonClicked(int button) {
        if (button == 0 && theInterface.isMouseInBounds(
                theInterface.getPositionX() + this.positionX + 80,
                theInterface.getPositionX() + this.positionX + this.maxWidth - 10,
                theInterface.getPositionY() + this.positionY + this.maxHeight / 2 - 12,
                theInterface.getPositionY() + this.positionY + this.maxHeight / 2 + 2)) {
            mouseDragging = true;
            return true;
        } else if (theInterface.isMouseInBounds(
                theInterface.getPositionX() + this.positionX + 80,
                theInterface.getPositionX() + this.positionX + this.maxWidth - 10,
                theInterface.getPositionY() + this.positionY,
                theInterface.getPositionY() + this.positionY + this.maxHeight)) {
            typing = button == 1;
            return true;
        } else {
            mouseDragging = false;
            typing = false;
            typedChars = "";
            return false;
        }
    }

    @Override
    public void mouseReleased() {
        mouseDragging = false;
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (typing) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                typing = false;
                return true;
            }

            String allowedChars = "0123456789.";
            if (keyCode == Keyboard.KEY_BACK) {
                if (typedChars.length() > 1) {
                    typedChars = typedChars.substring(0, typedChars.length() - 1);
                } else if (typedChars.length() == 1) {
                    typedChars = "";
                }
            } else if (keyCode == Keyboard.KEY_RETURN) {
                typedChars = "";
                typing = false;
            } else if (allowedChars.contains(Character.toString(typedChar))) {
                if (Fonts.f14.getStringWidth(typedChars) < maxWidth - 1) {
                    typedChars += Character.toString(typedChar);
                }
            }
            property.setValue(typedChars);
            return true;
        }
        return false;
    }

    public void onGuiClose() {
        mouseDragging = false;
        typing = false;
    }

    public void setPositionBasedOnValue() {
        currentPosition = (widthOfSlider / (property.getMaximum() - property.getMinimum())) * (property.getValue()- property.getMinimum());
    }

}
