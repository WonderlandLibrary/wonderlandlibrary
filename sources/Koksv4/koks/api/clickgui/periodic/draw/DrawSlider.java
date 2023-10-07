package koks.api.clickgui.periodic.draw;

import koks.api.clickgui.Element;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.utils.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * @author kroko
 * @created on 13.02.2021 : 03:01
 */
public class DrawSlider extends Element {
    public boolean dragging;

    public DrawSlider(Value<?> value) {
        super(Fonts.arial18);
        this.value = value;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        height = 15;
        final RenderUtil renderUtil = RenderUtil.getInstance();
        final String text = value.getDisplayName() != null ? value.getDisplayName() : value.getName();

        fr.drawString(text, x, y - height / 2F - 3 - fr.getStringHeight(text) + height, new Color(0xFFFFFFFF), true);
        fr.drawString(value.castDouble() + "", x + width - fr.getStringWidth(value.castDouble() + "") - 3, y - 3 - height / 2F - fr.getStringHeight(text) + height, new Color(0xFFFFFFFF), true);

        resetButtonDistance = (int) (fr.getStringWidth(text)) + 2;
        resetButtonHeight = -6;
        hasResetButton = !value.getValue().equals(value.getDefaultValue());

        Gui.drawRect(x, y + height / 2 - 3, x + width, y + height / 2 - 1, Integer.MIN_VALUE);

        double currentValue = (value.castDouble() - value.getMinimum()) / (value.getMaximum() - value.getMinimum());

        renderUtil.drawRect(x, y + height / 2F - 3, (x + (currentValue * width)), y + height / 2F - 1, value.getModule().getCategory().getCategoryColor().getRGB());

        if (dragging)
            updateValue(mouseX);
    }

    public void updateValue(double mouseX) {
        float currentValue = (float) (Math.round((float) Math.max(Math.min((mouseX - x) / width * (value.getMaximum() - value.getMinimum()) + value.getMinimum(), value.getMaximum()), value.getMinimum()) * 100.0F) / 100.0D);
        value.castIfPossible(value.getValue() instanceof Integer ? Math.round(currentValue) + "" : currentValue + "");
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y + height / 2 - 3 && mouseY < y + height / 2 - 1;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }
}
