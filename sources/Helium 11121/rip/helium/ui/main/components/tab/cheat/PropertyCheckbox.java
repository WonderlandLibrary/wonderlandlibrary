package rip.helium.ui.main.components.tab.cheat;

import net.minecraft.client.Minecraft;
import rip.helium.cheat.impl.visual.Hud;
import rip.helium.ui.main.Interface;
import rip.helium.utils.Draw;
import rip.helium.utils.font.Fonts;
import rip.helium.utils.property.abs.Property;
import rip.helium.utils.property.impl.BooleanProperty;

import java.awt.*;
import java.util.ArrayList;

public class PropertyCheckbox extends PropertyComponent {
    public PropertyCheckbox(final Interface theInterface, final Property value, final double x, final double y, final double width, final double height) {
        super(theInterface, value, x, y, width, height);
    }

    @Override
    public void drawComponent(final double x, final double y) {
        this.positionX = x - this.theInterface.getPositionX();
        this.positionY = y - this.theInterface.getPositionY();
        Draw.drawBorderedRectangle(x + this.maxWidth - 18.0, y + 3.0, x + this.maxWidth - 10.0, y + 11.0, 0.5, new Color(32, 31, 32).getRGB(), Hud.prop_color.getValue().getRGB(), true);
        if (((BooleanProperty) this.getProperty()).getValue()) {
            Draw.drawRectangle(x + this.maxWidth - 17.5, y + 3.5, x + this.maxWidth - 9.5, y + 10.5, Hud.prop_color.getValue().getRGB());
            Minecraft.getMinecraft().fontRendererObj.drawString("\u2713", (int) x + 133, (int) (y + 2), Hud.prop_color.getValue().getRGB());
        }
        Fonts.f14.drawString(this.getProperty().getId(), x + 6.0, y + this.maxHeight / 2.0 - 5.5, this.theInterface.getColor(255, 255, 255));
        if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + 4.0, this.theInterface.getPositionX() + this.positionX + 8.0 + Fonts.f12.getStringWidth(this.getProperty().getId()), this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 - 7.5, this.theInterface.getPositionY() + this.positionY + this.maxHeight / 2.0 - 3.5 + Fonts.f12.getStringHeight(this.getProperty().getId()))) {
            final String desc = this.getProperty().getDescription();
            final ArrayList<String> list = new ArrayList<String>(Fonts.f14.wrapWords(desc, 102.0));
            if (list.size() > 1) {
                double boxWidth = -1.0;
                for (final String string : list) {
                    if (Fonts.f12.getStringWidth(string) > boxWidth) {
                        boxWidth = Fonts.f14.getStringWidth(string);
                    }
                }
                Draw.drawRectangle(this.theInterface.getCurrentFrameMouseX() + 4.0, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getCurrentFrameMouseX() + 4.0 + boxWidth, this.theInterface.getCurrentFrameMouseY() + 8 * list.size(), this.theInterface.getColor(40, 40, 40));
                for (final String string : list) {
                    Fonts.f12.drawString(string, this.theInterface.getCurrentFrameMouseX() + 5.0, this.theInterface.getCurrentFrameMouseY() + 2.0 + list.indexOf(string) * 8, this.theInterface.getColor(255, 255, 255));
                }
            } else {
                final double boxWidth = Fonts.f12.getStringWidth(desc);
                Draw.drawRectangle(this.theInterface.getCurrentFrameMouseX() + 4.0, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getCurrentFrameMouseX() + 4.0 + boxWidth, this.theInterface.getCurrentFrameMouseY(), this.theInterface.getColor(40, 40, 40));
                Fonts.f12.drawString(desc, this.theInterface.getCurrentFrameMouseX() + 5.0, this.theInterface.getCurrentFrameMouseY() + 2.0, this.theInterface.getColor(255, 255, 255));
            }
        }
    }

    @Override
    public boolean mouseButtonClicked(final int button) {
        if (button == 0 && this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + this.maxWidth - 18.0, this.theInterface.getPositionX() + this.positionX + this.maxWidth - 10.0, this.theInterface.getPositionY() + this.positionY + 3.0, this.theInterface.getPositionY() + this.positionY + 11.0)) {
            this.getProperty().setValue(!((BooleanProperty) this.getProperty()).getValue());
            return true;
        }
        return false;
    }
}
