package cc.novoline.gui.screen.dropdown.config;

import cc.novoline.utils.Timer;
import cc.novoline.utils.fonts.impl.Fonts;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_16.SF_16;

public class ConfigTextField extends Config {

    private String value;
    private final Timer backspace = new Timer();

    public ConfigTextField(String name, ConfigTab parent) {
        super(name, parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        final String s = getValue();

        if (getParent().getSelectedConfig() == this
                && Keyboard.isKeyDown(Keyboard.KEY_BACK)
                && backspace.delay(100) && s.length() >= 1
        ) {
            setValue(s.substring(0, s.length() - 1));
            backspace.reset();
        }

        y = (int) (getParent().getPosY() + 15);

        for (Config config : getParent().getConfigs()) {
            if (config == this) {
                break;
            } else {
                y += config.getYPerConfig();
            }
        }
        
        Gui.drawRect(getParent().getPosX(), y,getParent().getPosX() + 100, y + getYPerConfig(), new Color(40, 40, 40, 255).getRGB());

        Gui.drawRect(getParent().getPosX() + 2, y + 16, getParent().getPosX() + 101 - 6, y + 16.5,
                new Color(195, 195, 195, 220).getRGB());

        Fonts.SF.SF_17.SF_17.drawString(getName(), getParent().getPosX() + 2f, y + 2f,
                new Color(227, 227, 227, 255).getRGB());

        if (Fonts.SF.SF_17.SF_17.stringWidth(s) > 65) {
            Fonts.SF.SF_17.SF_17.drawString(Fonts.SF.SF_17.SF_17.trimStringToWidth(s, 78, true), getParent().getPosX() + 2, y + 10,
                            0xFFFFFFFF);
        } else {
            Fonts.SF.SF_17.SF_17.drawString(s, getParent().getPosX() + 2, y + 10, 0xFFFFFFFF);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY) && mouseButton == 0) {
            getParent().setSelectedConfig(this);
        } else if(getParent().getSelectedConfig() == this) {
            getParent().setSelectedConfig(null);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(getParent().getSelectedConfig() == this) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
                getParent().setSelectedConfig(null);
            } else if (!(keyCode == Keyboard.KEY_BACK)
                    && keyCode != Keyboard.KEY_RCONTROL
                    && keyCode != Keyboard.KEY_LCONTROL
                    && keyCode != Keyboard.KEY_RSHIFT
                    && keyCode != Keyboard.KEY_LSHIFT
                    && keyCode != Keyboard.KEY_TAB
                    && keyCode != Keyboard.KEY_CAPITAL
                    && keyCode != Keyboard.KEY_DELETE
                    && keyCode != Keyboard.KEY_HOME
                    && keyCode != Keyboard.KEY_INSERT
                    && keyCode != Keyboard.KEY_UP
                    && keyCode != Keyboard.KEY_DOWN
                    && keyCode != Keyboard.KEY_RIGHT
                    && keyCode != Keyboard.KEY_LEFT
                    && keyCode != Keyboard.KEY_LMENU
                    && keyCode != Keyboard.KEY_RMENU
                    && keyCode != Keyboard.KEY_PAUSE
                    && keyCode != Keyboard.KEY_SCROLL
                    && keyCode != Keyboard.KEY_END
                    && keyCode != Keyboard.KEY_PRIOR
                    && keyCode != Keyboard.KEY_NEXT
                    && keyCode != Keyboard.KEY_APPS
                    && keyCode != Keyboard.KEY_F1
                    && keyCode != Keyboard.KEY_F2
                    && keyCode != Keyboard.KEY_F4
                    && keyCode != Keyboard.KEY_F5
                    && keyCode != Keyboard.KEY_F6
                    && keyCode != Keyboard.KEY_F7
                    && keyCode != Keyboard.KEY_F8
                    && keyCode != Keyboard.KEY_F9
                    && keyCode != Keyboard.KEY_F10
                    && keyCode != Keyboard.KEY_F11
                    && keyCode != Keyboard.KEY_F12
            ) {
                setValue(getValue() + typedChar);
            }
        }
    }

    public String getValue() {
    	return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getYPerConfig() {
        return 19;
    }
}
