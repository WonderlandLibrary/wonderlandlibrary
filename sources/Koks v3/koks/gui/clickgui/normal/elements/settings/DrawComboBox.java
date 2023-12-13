package koks.gui.clickgui.normal.elements.settings;

import koks.Koks;
import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import net.minecraft.client.gui.Gui;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public class DrawComboBox extends Element {

    public DrawComboBox(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
        fr.drawStringWithShadow(extended ? "-" : "+", x + width - 10, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        fr.drawStringWithShadow(setting.getName(), x + width / 2 - fr.getStringWidth(setting.getName()) / 2, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);

        if (extended) {
            int y = this.y + this.height;
            for (String mode : setting.getModes()) {
                Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
                fr.drawStringWithShadow(mode, x + width / 2 - fr.getStringWidth(mode) / 2, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, setting.getCurrentMode().equals(mode) ? Koks.getKoks().clientColor.getRGB() : 0xFFFFFFFF);
                y += this.height;
            }
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0 || mouseButton == 1) {
                extended = !extended;
            }
        }

        if (extended) {
            int y = this.y + this.height;
            for (String mode : setting.getModes()) {
                if (mouseButton == 0 && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
                    setting.setCurrentMode(mode);
                }
                y += this.height;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}