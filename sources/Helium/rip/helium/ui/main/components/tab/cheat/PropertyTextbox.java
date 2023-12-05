package rip.helium.ui.main.components.tab.cheat;

import rip.helium.ui.main.Interface;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;
import rip.helium.utils.property.abs.Property;
import rip.helium.utils.property.impl.TextProperty;

import java.awt.*;
import java.util.ArrayList;

public class PropertyTextbox extends PropertyComponent {
    private boolean mouseDragging;
    private boolean typing;
    private String typedChars;
    private final double widthOfSlider;
    private final TextProperty property;
    private final double currentPosition;
    private final ArrayList<Double> possibleValues;

    public PropertyTextbox(final Interface theInterface, final Property value, final double x, final double y, final double width, final double height) {
        super(theInterface, value, x, y, width, height);
        this.possibleValues = new ArrayList<Double>();
        this.mouseDragging = false;
        this.typing = false;
        this.typedChars = "";
        this.widthOfSlider = x + this.maxWidth - 10.0 - (x + 80.0);
        this.property = (TextProperty) value;
        this.currentPosition = this.widthOfSlider;
    }

    @Override
    public void drawComponent(final double x, final double y) {
        this.positionX = x - this.theInterface.getPositionX();
        this.positionY = y - this.theInterface.getPositionY();
        Fonts.f14.drawString(this.getProperty().getId(), x + 6.0, y + this.maxHeight / 2.0 - 5.5, this.theInterface.getColor(255, 255, 255));
        Draw.drawRectangle(x + 80.0, y + this.maxHeight / 2.0 - 6.0, x + this.maxWidth - 10.0, y + this.maxHeight / 2.0 - 4.0, new Color(54, 56, 56).getRGB());
        Draw.drawRectangle(x + 80.0, y + this.maxHeight / 2.0 - 6.0, x + 80.0 + this.currentPosition, y + this.maxHeight / 2.0 - 4.0,
                new Color(147, 2, 0).getRGB());
        if (this.typing) {
            Fonts.f12.drawCenteredString(this.typedChars + "_", (float) (x + 80.0 + this.widthOfSlider / 2.0), (float) (y + this.maxHeight / 2.0 - 2.0), this.typing ? this.theInterface.getColor(255, 255, 255) : this.theInterface.getColor(255, 255, 255));
        } else {
            Fonts.f12.drawCenteredString(String.valueOf(this.property.getValue()), (float) (x + 80.0 + this.currentPosition), (float) (y + this.maxHeight / 2.0), this.typing ? this.theInterface.getColor(255, 255, 255) : this.theInterface.getColor(255, 255, 255));
        }
        if (this.theInterface.isClosing()) {
            this.mouseDragging = false;
            this.typing = false;
            return;
        }
        if (this.typing) {
            this.mouseDragging = false;
        }
        /*/if (this.mouseDragging) {
            final double cursorPosOnBar = this.theInterface.getCurrentFrameMouseX() - x - 80.0;
            this.currentPosition = Mafs.clamp(cursorPosOnBar, 0.0, this.widthOfSlider);
            //final double exactValue = Mafs.clamp(this.property.getMinimum() + (this.property.getMaximum() - this.property.getMinimum()) * (cursorPosOnBar / this.widthOfSlider), this.property.getMinimum(), this.property.getMaximum());
            if (this.possibleValues.isEmpty()) {
                double current = this.property.getMinimum();
                this.possibleValues.add(current);
                while (current < this.property.getMaximum()) {
                    current += this.property.getIncrement();
                    this.possibleValues.add(current);
                }
            }
            double bestValue = -1.0;
            for (final Double value : this.possibleValues) {
                if (bestValue == -1.0) {
                    bestValue = value;
                }
                else {
                    if (Mafs.getDifference(exactValue, value) >= Mafs.getDifference(exactValue, bestValue)) {
                        continue;
                    }
                    bestValue = value;
                }
            }
            this.property.setValue(bestValue);
        }
        else {
            this.setPositionBasedOnValue();
            this.possibleValues.clear();
        }/*/
        /*/if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + 4.0, this.theInterface.getPositionX() + this.positionX + 8.0 + Fonts.f12.getStringWidth(this.getProperty().getId()), this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 - 7.5, this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 - 3.5 + Fonts.f12.getStringHeight(this.getProperty().getId()))) {
            final String desc = this.getProperty().getDescription();
            final ArrayList<String> list = new ArrayList<String>(Fonts.f14.wrapWords(desc, 102.0));
            if (list.size() > 1) {
                double boxWidth = -1.0;
                for (final String string : list) {
                    if (Fonts.f12.getStringWidth(string) > boxWidth) {
                        boxWidth = Fonts.f12.getStringWidth(string);
                    }
                }
                Draw.drawRectangle(this.theInterface.getCurrentFrameMouseX() + 4.0, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getCurrentFrameMouseX() + 4.0 + boxWidth, this.theInterface.getCurrentFrameMouseY() + 8 * list.size(), this.theInterface.getColor(191, 191, 191));
                for (final String string : list) {
                    Fonts.f12.drawString(string, this.theInterface.getCurrentFrameMouseX() + 5.0, this.theInterface.getCurrentFrameMouseY() + 2.0 + list.indexOf(string) * 8, this.theInterface.getColor(255, 255, 255));
                }
            }
            else {
                final double boxWidth = Fonts.f12.getStringWidth(desc);
                Draw.drawRectangle(this.theInterface.getCurrentFrameMouseX() + 4.0, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getCurrentFrameMouseX() + 4.0 + boxWidth, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getColor(191, 191, 191));
                Fonts.f12.drawString(desc, this.theInterface.getCurrentFrameMouseX() + 5.0, this.theInterface.getCurrentFrameMouseY() + 2.0, this.theInterface.getColor(255, 255, 255));
            }
        }/*/
    }

    @Override
    public boolean mouseButtonClicked(final int button) {
        if (button == 0 && this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + 80.0, this.theInterface.getPositionX() + this.positionX + this.maxWidth - 10.0, this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 - 12.0, this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 + 2.0)) {
            return this.mouseDragging = true;
        }
        if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + 80.0, this.theInterface.getPositionX() + this.positionX + this.maxWidth - 10.0, this.theInterface.getPositionY() + this.positionY, this.theInterface.getPositionY() + this.positionY + this.maxHeight)) {
            this.typing = (button == 1);
            return true;
        }
        this.mouseDragging = false;
        this.typing = false;
        this.typedChars = "";
        return false;
    }

    @Override
    public void mouseReleased() {
        this.mouseDragging = false;
    }

    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) {
        if (!this.typing) {
            return false;
        }
        if (keyCode == 1) {
            this.typing = false;
            return true;
        }
        final String allowedChars = "0123456789.";
        if (keyCode == 14) {
            if (this.typedChars.length() > 1) {
                this.typedChars = this.typedChars.substring(0, this.typedChars.length() - 1);
            } else if (this.typedChars.length() == 1) {
                this.typedChars = "";
            }
        } else if (keyCode == 28) {
            this.typedChars = "";
            this.typing = false;
        } else if (allowedChars.contains(Character.toString(typedChar)) && Fonts.f14.getStringWidth(this.typedChars) < this.maxWidth - 1.0) {
            this.typedChars = String.valueOf(this.typedChars) + typedChar;
        }
        this.property.setValue(this.typedChars);
        return true;
    }

    @Override
    public void onGuiClose() {
        this.mouseDragging = false;
        this.typing = false;
    }

    public void setPositionBasedOnValue() {
        //this.currentPosition = this.widthOfSlider / (this.property.getMaximum() - this.property.getMinimum()) * (this.property.getValue() - this.property.getMinimum());
    }
}
