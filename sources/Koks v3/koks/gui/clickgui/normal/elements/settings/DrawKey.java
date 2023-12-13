package koks.gui.clickgui.normal.elements.settings;

import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:34
 */
public class DrawKey extends Element {

    public DrawKey(Setting setting) {
        this.setting = setting;
    }

    public boolean isKeyTyped = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
        fr.drawStringWithShadow(setting.getName(), x + 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        String s = isKeyTyped ? "type...." : Keyboard.getKeyName(setting.getKey());
        fr.drawStringWithShadow(s, x + 3 + width - fr.getStringWidth(s) - 5, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + 2 + width - 4 && mouseY > y + 2 && mouseY < y + height - 2;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(isKeyTyped) {
            setting.setKey(keyCode);
            isKeyTyped = false;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            isKeyTyped = !isKeyTyped;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

}