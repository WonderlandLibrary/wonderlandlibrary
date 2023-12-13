package koks.gui.clickgui.normal.elements;

import koks.Koks;
import koks.gui.clickgui.Element;
import koks.gui.clickgui.normal.elements.settings.*;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:10
 */
public class DrawModule {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    public final ArrayList<Element> elements = new ArrayList<>();
    public Module module;
    public int x, y, width, height;
    public boolean extended;

    public DrawModule(Module module) {
        this.module = module;

        for (Setting setting : Koks.getKoks().settingsManager.getSettings()) {
            if (setting.getModule() == module) {
                if (setting.getType() == Setting.Type.CHECKBOX)
                    elements.add(new DrawCheckBox(setting));
                if (setting.getType() == Setting.Type.COMBOBOX)
                    elements.add(new DrawComboBox(setting));
                if (setting.getType() == Setting.Type.SLIDER)
                    elements.add(new DrawSlider(setting));
                if (setting.getType() == Setting.Type.KEY)
                    elements.add(new DrawKey(setting));
                if (setting.getType() == Setting.Type.TYPE)
                    elements.add(new DrawType(setting));
            }
        }
    }

    public void updatePosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, 0xFF202020);
        if (isHovered(mouseX, mouseY))
            Gui.drawRect(x, y, x + width, y + height, 0xBB252525);
        if (module.isBypass()) {
            Gui.drawRect(x, y, x + 2, y + height, Koks.getKoks().clientColor.getRGB());
            Gui.drawRect(x + width - 2, y, x + width, y + height, Koks.getKoks().clientColor.getRGB());
        }
        Color textColor = module.isToggled() ? Koks.getKoks().clientColor : new Color(0xFFFFFFFF);
        if (!elements.isEmpty())
            fr.drawStringWithShadow(extended ? "-" : "+", x + width - 10, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, 0xFFFFFFFF);
        fr.drawStringWithShadow(module.getName(), x + width / 2 - fr.getStringWidth(module.getName()) / 2, y + height / 2 - fr.FONT_HEIGHT / 2 + 1, isHovered(mouseX, mouseY) ? textColor.darker().getRGB() : textColor.getRGB());

        int settingWidth = 0;
        for (Element element : elements) {
            String settingName = element.setting.getName();
            if (element.setting.getType() == Setting.Type.CHECKBOX) {
                String string = settingName;
                int offset = 10;
                if (settingWidth < fr.getStringWidth(string) + height - 4 + offset) {
                    settingWidth = fr.getStringWidth(string) + height - 4 + offset;
                }
            }
            if (element.setting.getType() == Setting.Type.COMBOBOX) {
                String string = settingName + (element.extended ? "-" : "+");
                if (settingWidth < fr.getStringWidth(string) + 22) {
                    settingWidth = fr.getStringWidth(string) + 22;
                }

                for (String mode : element.setting.getModes()) {
                    int offset = 10;
                    if (settingWidth < fr.getStringWidth(mode) + offset) {
                        settingWidth = fr.getStringWidth(mode) + offset;
                    }
                }
            }

            if (element.setting.getType() == Setting.Type.KEY) {
                String typed = element.setting.getTyped();
                DrawKey key = (DrawKey) element;
                int offset = fr.getStringWidth(element.setting.getName()) + (key.isKeyTyped ? fr.getStringWidth("type...") : fr.getStringWidth(Keyboard.getKeyName(element.setting.getKey()))) + 12;
                if (settingWidth < fr.getStringWidth(typed) + offset) {
                    settingWidth = fr.getStringWidth(typed) + offset;
                }
            }

            if (element.setting.getType() == Setting.Type.TYPE) {
                String typed = element.setting.getTyped();
                int offset = fr.getStringWidth(element.setting.getName()) + 12;
                if (settingWidth < fr.getStringWidth(typed) + offset) {
                    settingWidth = fr.getStringWidth(typed) + offset;
                }
            }

            if (element.setting.getType() == Setting.Type.SLIDER) {
                String string = settingName + "00.00";
                int offset = 15;
                if (settingWidth < fr.getStringWidth(string) + offset) {
                    settingWidth = fr.getStringWidth(string) + offset;
                }
            }
        }

        int y = this.y;
        for (Element element : elements) {
            if (element.setting.isVisible() && extended) {
                element.updatePosition(x + width, y, settingWidth, height);
                element.drawScreen(mouseX, mouseY, partialTicks);
                y += this.height;

                if (element.setting.getType() == Setting.Type.COMBOBOX && element.extended) {
                    y += this.height * element.setting.getModes().length;
                }
            }
        }

    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Element element : elements) {
            if (element.setting.isVisible() && extended) {
                element.keyTyped(typedChar, keyCode);
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    module.toggle();
                }else{
                    module.setBypass(!module.isBypass());
                }
            }

            if (mouseButton == 1) {
                extended = !extended;
            }
        }

        for (Element element : elements) {
            if (element.setting.isVisible() && extended) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Element element : elements) {
            if (element.setting.isVisible() && extended) {
                element.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

}