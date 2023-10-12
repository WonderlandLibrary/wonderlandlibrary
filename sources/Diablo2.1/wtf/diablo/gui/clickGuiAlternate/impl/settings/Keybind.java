package wtf.diablo.gui.clickGuiAlternate.impl.settings;

import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import wtf.diablo.gui.clickGuiAlternate.impl.Button;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.KeybindSetting;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;

public class Keybind extends SetBase {
    public wtf.diablo.gui.clickGuiAlternate.impl.Button parent;
    public double y;

    public Keybind(Setting set, Button parent) {
        this.setting = set;
        this.parent = parent;
    }

    double x;
    double width;
    public KeybindSetting mode;
    int height;
    private boolean isTyping = false;

    private double otherWidth, otherX, otherX2, otherY2;

    /*
    Shut the fuck up this code is bad -vince
     */
    public double drawScreen(int mouseX, int mouseY, float partialTicks, double settingHeight) {
        height = 15;
        mode = (KeybindSetting) setting;
        x = parent.parent.x;
        width = parent.parent.width;
        y = settingHeight + parent.y + parent.height;
        Gui.drawRect(x, y, x + width, y + height, 0x933F3F3F);
        Fonts.SFReg18.drawStringWithShadow("Bind", x + 3, y + ((height - Fonts.SFReg18.getHeight()) / 2) + 0.5, -1);

        otherWidth = 40;
        otherX = x + width - otherWidth - 4;
        otherX2 = x + width - 4;
        otherY2 = y + ((height - Fonts.SFReg18.getHeight()) / 2);

        String keyString = this.isTyping ? "..." : mode.getKeybindString();
        RenderUtil.drawRoundedRectangle(otherX, y + 2, otherX2, y + height - 2, 8, new Color(23, 23, 23, 200).getRGB());
        Fonts.SFReg18.drawStringWithShadow(keyString, otherX + (otherWidth / 2) - (Fonts.SFReg18.getStringWidth(keyString) / 2), otherY2 + 0.5, -1);
        return height;
    }

    public double getHeight() {
        return height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        switch (mouseButton) {
            case 0:
                if (RenderUtil.isHovered(mouseX, mouseY, otherX, y + 2, otherX2, y + height - 2)) {
                    this.isTyping = true;
                }
                break;
            case 1:
                this.isTyping = false;
                break;
        }
    }

    public boolean isHidden() {
        return setting.isHidden();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(this.isTyping && (keyCode == Keyboard.KEY_ESCAPE)){
            this.isTyping = false;
            return;
        }
        if(isTyping && keyCode == Keyboard.KEY_BACK){
            this.isTyping = false;
            mode.setKeybind(0);
        }
        if (this.isTyping) {
            mode.setKeybind(keyCode);
            this.isTyping = false;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
