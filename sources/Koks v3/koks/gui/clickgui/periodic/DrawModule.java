package koks.gui.clickgui.periodic;

import koks.Koks;
import koks.api.settings.Setting;
import koks.gui.clickgui.Element;
import koks.gui.clickgui.periodic.settings.*;
import koks.manager.module.Module;
import koks.api.interfaces.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 12.11.2020 : 15:47
 */
public class DrawModule implements Wrapper {

    int x, y, size, outline;
    Module module;
    Color color;
    private Minecraft mc = Minecraft.getMinecraft();
    private FontRenderer fr = mc.fontRendererObj;

    public ArrayList<Element> elements = new ArrayList<>();

    public DrawModule(Module module, int x, int y, int size, int outline, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.outline = outline;
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

    public void drawScreen(int mouseX, int mouseY) {

        renderUtil.drawOutline(x - (isHover(mouseX, mouseY) ? 1 : 0), y - (isHover(mouseX, mouseY) ? 1 : 0), x + size + (isHover(mouseX, mouseY) ? 1 : 0), y + size + (isHover(mouseX, mouseY) ? 1 : 0), outline, isHover(mouseX, mouseY) ? color.darker().getRGB() : color.getRGB());
        int width = Math.abs((x + size) - x);

        float sizeName = Math.min(0.8F, (float) size / fr.getStringWidth(module.getName()) * 0.7F);
        float sizeToggle = 0.8F;

        Color toggleColor = module.isToggled() ? color : color.darker();
        fr.drawString(module.getName().substring(0, 2), x + width / 2 - fr.getStringWidth(module.getName().substring(0, 2)) / 2, y + size / 2 - fr.FONT_HEIGHT, isHover(mouseX, mouseY) ? toggleColor.darker().getRGB() : toggleColor.getRGB());
        fr.drawString(module.getName(), (int) (x + width / 2 - (fr.getStringWidth(module.getName()) * sizeName) / 2), y + size / 2, sizeName, isHover(mouseX, mouseY) ? color.darker().getRGB() : color.getRGB());
        fr.drawString(module.isToggled() ? "Enabled" : "Disabled", (int) (x + width / 2 - fr.getStringWidth(module.isToggled() ? "Enabled" : "Disabled") * sizeToggle / 2), y + size / 2 + fr.FONT_HEIGHT, sizeToggle, isHover(mouseX, mouseY) ? color.darker().getRGB() : color.getRGB());
        fr.drawString(Koks.getKoks().clickGUIPE.bindMod != null && Koks.getKoks().clickGUIPE.bindMod.equals(module) ? "..." : module.getKey() == 0 ? "" : Keyboard.getKeyName(module.getKey()), x + 1, y + 1, 0.6F, isHover(mouseX, mouseY) ? color.darker().getRGB() : color.getRGB());

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Element element : elements) {
            element.mouseReleased(mouseX, mouseY, state);
        }
    }

    public boolean isHover(int mouseX, int mouseY) {
        return mouseX >= x - outline && mouseX <= x + size + outline && mouseY >= y - outline && mouseY <= y + size + outline;
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Element element : elements) {
            element.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (Koks.getKoks().clickGUIPE.settingMenu) {
            for (Element element : elements) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        if (isHover(mouseX, mouseY)) {
            if (!Koks.getKoks().clickGUIPE.settingMenu) {
                switch (mouseButton) {
                    case 0:
                        module.toggle();
                        break;
                    case 1:

                        Koks.getKoks().clickGUIPE.curMod = module;
                        Koks.getKoks().clickGUIPE.settingMenu = true;
                        Koks.getKoks().clickGUIPE.settingScroll = 0;
                        break;
                    case 2:
                        boolean bindModule = Koks.getKoks().clickGUIPE.bindModule;
                        if (!bindModule) {
                            Koks.getKoks().clickGUIPE.bindModule = true;
                            Koks.getKoks().clickGUIPE.bindMod = module;
                        }
                        break;
                }
            }
        } else {
            if(Koks.getKoks().clickGUIPE.settingMenu) {
                int settingsSize = Koks.getKoks().clickGUIPE.settingsSize;
                int x = Koks.getKoks().clickGUIPE.x;
                int y = Koks.getKoks().clickGUIPE.y;
                if (!(mouseX >= x - settingsSize / 2 && mouseX <= x + settingsSize / 2 && mouseY >= y - settingsSize / 2 && mouseY <= y + settingsSize / 2)) {
                    Koks.getKoks().clickGUIPE.curMod = null;
                    Koks.getKoks().clickGUIPE.settingMenu = false;
                    Koks.getKoks().clickGUIPE.settingScroll = 0;
                }
            }
        }
    }
}
