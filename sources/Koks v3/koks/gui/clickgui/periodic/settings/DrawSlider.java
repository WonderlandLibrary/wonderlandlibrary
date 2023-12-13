package koks.gui.clickgui.periodic.settings;

import koks.Koks;
import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import net.minecraft.client.gui.Gui;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public class DrawSlider extends Element {

    public boolean dragging;

    public DrawSlider(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        fr.drawStringWithShadow(setting.getName(), x - 3, y - 2 - fr.FONT_HEIGHT, 0xFFFFFFFF);
        fr.drawStringWithShadow(setting.getCurrentValue() + "", x - 3 + width - fr.getStringWidth(setting.getCurrentValue() + "") - 3, y - 2 - fr.FONT_HEIGHT, 0xFFFFFFFF);

        Gui.drawRect(x - 3, y + height - 3, x + width - 3, y + height - 1, 0xFF101010);

        double currentValue = (setting.getCurrentValue() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue());
        Gui.drawRect(x - 3, y + height - 3, (int) (x - 3 + (currentValue * width)), y + height - 1, setting.getModule().getCategory().getCategoryColor().getRGB());

        if (dragging)
            updateValue(mouseX);
    }

    public void updateValue(double mouseX) {
        float currentValue = (float) (Math.round((float) Math.max(Math.min((mouseX - x) / width * (setting.getMaxValue() - setting.getMinValue()) + setting.getMinValue(), setting.getMaxValue()), setting.getMinValue()) * 100.0F) / 100.0D);
        setting.setCurrentValue(setting.isOnlyInt() ? Math.round(currentValue) : currentValue);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x - 3 && mouseX < x - 3 + width && mouseY > y + height - 4 && mouseY < y + height;
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