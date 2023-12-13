package koks.gui.clickgui.periodic.settings;

import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

/**
 * @author kroko
 * @created on 13.11.2020 : 16:06
 */
public class DrawType extends Element {

    public DrawType(Setting setting) {
        this.setting = setting;
    }

    public boolean isKeyTyped = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        fr.drawStringWithShadow(setting.getName(), x - 3, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        String s = setting.getTyped();
        int rnd = (int) (System.currentTimeMillis() / 1000);
        fr.drawStringWithShadow(s.replace('§', '&') + (isKeyTyped ? rnd % 2 == 0 ? "§7_" : "" : ""), x + 3 + width - fr.getStringWidth(s.replace('§', '&')) - 5 - (isKeyTyped ? fr.getStringWidth("§7_") - 1 : 0), y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x - 3 && mouseX < x + 3 + width - fr.getStringWidth(setting.getTyped().replace('§', '&')) - 5 - (isKeyTyped ? fr.getStringWidth("§7_") - 1 : 0) && mouseY >= y && mouseY < y + fr.FONT_HEIGHT;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        Keyboard.enableRepeatEvents(true);
        if(isKeyTyped) {
            int key = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
            if(key == Keyboard.KEY_RETURN) {
                isKeyTyped = false;
            }else if(key == 14) {
                if(setting.getTyped().length() <= 0) return;
                setting.setTyped(setting.getTyped().substring(0, setting.getTyped().length() -1));
                return;
            }
            if(ChatAllowedCharacters.isAllowedCharacter(typedChar) || typedChar == '§') {
                setting.setTyped(setting.getTyped() + typedChar);
            }
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
