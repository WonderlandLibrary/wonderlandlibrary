package org.dreamcore.client.ui.newclickgui.settings;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.StringSetting;
import org.dreamcore.client.ui.newclickgui.FeaturePanel;

import java.awt.*;
import java.util.List;

public class StringComponent extends Component {

    private boolean active;

    public StringComponent(FeaturePanel featurePanel, StringSetting setting) {
        this.featurePanel = featurePanel;
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RectHelper.drawSmoothRect(x - 15, y + 1, x, y + height - 3, this.active ? new Color(36, 36, 36).getRGB() : new Color(26, 26, 26).getRGB());
        mc.fontRendererObj.drawStringWithShadow(setting.getName(), x,  y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2F + 1, new Color(255, 255, 255).getRGB());
        mc.fontRendererObj.drawStringWithShadow(((StringSetting) setting).currentText, x + width - 143,  y + height / 2 - mc.fontRendererObj.FONT_HEIGHT / 2F + 1, new Color(255, 255, 255).getRGB());
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0 && featurePanel.usingSettings && dreamcore.instance.newClickGui.usingSetting) {
            this.active = !this.active;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char chars, int key) {
        if (key == 1) {
            return;
        }
        if (28 == key && this.active) {
            this.enterString();
        } else if (key == 14 && this.active) {
            if (!((StringSetting)setting).currentText.isEmpty()) {
                ((StringSetting)setting).currentText = ((StringSetting)setting).currentText.substring(0, ((StringSetting)setting).currentText.length() - 1);
            }
        } else if (ChatAllowedCharacters.isAllowedCharacter(chars) && this.active) {
            ((StringSetting)setting).setCurrentText(((StringSetting)setting).currentText + chars);
        }
        super.keyTyped(chars, key);
    }

    private void enterString() {
        this.active = false;
        ((StringSetting)setting).setCurrentText(((StringSetting)setting).currentText);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
}
