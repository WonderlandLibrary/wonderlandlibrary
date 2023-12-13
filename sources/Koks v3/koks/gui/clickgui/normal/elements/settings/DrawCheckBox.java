package koks.gui.clickgui.normal.elements.settings;

import koks.Koks;
import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import net.minecraft.client.gui.Gui;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public class DrawCheckBox extends Element {

    public DrawCheckBox(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
        Gui.drawRect(x + 2, y + 2, x + 2 + height - 4, y + height - 2, setting.isToggled() ? Koks.getKoks().clientColor.getRGB() : 0xFF101010);
        fr.drawStringWithShadow(setting.getName(), x + width - fr.getStringWidth(setting.getName()) - 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x + 2 && mouseX < x + 2 + height - 4 && mouseY > y + 2 && mouseY < y + height - 2;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            setting.setToggled(!setting.isToggled());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}