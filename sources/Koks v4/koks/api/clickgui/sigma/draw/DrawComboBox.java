package koks.api.clickgui.sigma.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.RenderUtil;
import koks.api.utils.TimeHelper;

import java.awt.*;

/**
 * @author kroko
 * @created on 13.02.2021 : 02:54
 */
public class DrawComboBox extends Element {

    public TimeHelper timeHelper = new TimeHelper();

    public DrawComboBox(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final RenderUtil renderUtil = RenderUtil.getInstance();
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        height = (int) (20 + (extended ? (fr.getStringHeight(name) + 1) * value.getModes().size() : fr.getStringHeight(name) + 1));

        fr.drawString(name, x + 40, y, Color.white, true);

        int maxWidth = 0;
        for (String mode : value.getModes())
            if (fr.getStringWidth(mode) + 5 > maxWidth)
                maxWidth = (int) (fr.getStringWidth(mode) + 5);

        if (extended) {
            renderUtil.drawRoundedRect(x + width - maxWidth, y, maxWidth + 5, (fr.getStringHeight(name) + 1) * value.getModes().size(), 5, new Color(30, 32, 34, 255));
            if(!value.hasValidMode())
                value.setValidMode();
            fr.drawString(value.castString(), x + width - fr.getStringWidth(value.castString()) + 5, y + 1, Color.white, true);
            int indexY = 1;
            for(String mode : value.getModes()) {
                if (!mode.equalsIgnoreCase(value.castString())) {
                    fr.drawString(mode, x + width - fr.getStringWidth(mode) + 5, y + (fr.getStringHeight(name) + 1) * indexY + fr.getStringHeight(name) / 2, Color.white, true);
                    indexY++;
                }
            }

        } else {
            renderUtil.drawRoundedRect(x + width - maxWidth, y, maxWidth + 5, fr.getStringHeight(name), 5, new Color(30, 32, 34, 255));
            fr.drawString(value.castString(), x + width - fr.getStringWidth(value.castString()) - 3, y + 1, Color.white, true);
            fr.drawString("<", x + width - fr.getStringWidth("<") + 5, y + 1, Color.white, true);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    public boolean isHovered(int mouseX, int mouseY) {
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        int maxWidth = 0;
        for (String mode : value.getModes())
            if (fr.getStringWidth(mode) + 5 > maxWidth)
                maxWidth = (int) (fr.getStringWidth(mode) + 5);
        if(extended) {
            return mouseX >= x + width - maxWidth && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + (fr.getStringHeight(name) + 1) * value.getModes().size();
        } else {
            return mouseX >= x + width - maxWidth && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + fr.getStringHeight(name);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        final String name = value.getDisplayName() != null ? value.getDisplayName() : value.getName();
        if(isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                if (extended) {
                    int maxWidth = 0;
                    for (String mode : value.getModes())
                        if (fr.getStringWidth(mode) + 5 > maxWidth)
                            maxWidth = (int) (fr.getStringWidth(mode) + 5);
                    int indexY = 1;
                    if(mouseX >= x + width - maxWidth && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + fr.getStringHeight(name)) {
                        extended = false;
                        return;
                    }
                    for(String mode : value.getModes()) {
                        if (!mode.equalsIgnoreCase(value.castString())) {
                            if(mouseX >= x + width - maxWidth && mouseX <= x + width + 5 && mouseY >= y + (fr.getStringHeight(name) + 1) * indexY && mouseY <= y + (fr.getStringHeight(name) + 1) * indexY + fr.getStringHeight(name)) {
                                extended = false;
                                value.castIfPossible(mode);
                                return;
                            }
                            indexY++;
                        }
                    }
                }
            }
            if (mouseButton == 1 || mouseButton == 0 && !extended) {
                extended = !extended;
            }
        }

}

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}
