package koks.api.clickgui.periodic;

import koks.Koks;
import koks.api.Methods;
import koks.api.clickgui.Element;
import koks.api.clickgui.periodic.draw.DrawCheckBox;
import koks.api.clickgui.periodic.draw.DrawColorPicker;
import koks.api.clickgui.periodic.draw.DrawComboBox;
import koks.api.clickgui.periodic.draw.DrawSlider;
import koks.api.font.DirtyFontRenderer;
import koks.api.font.Fonts;
import koks.api.manager.value.Value;
import koks.api.manager.value.ValueManager;
import koks.api.registry.module.Module;
import koks.api.utils.RandomUtil;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author kroko
 * @created on 13.02.2021 : 02:37
 */
public class DrawModule implements Methods {
    int x, y, size, outline;
    Module module;
    Color color;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final DirtyFontRenderer fr = Fonts.arial18;

    public ArrayList<Element> elements = new ArrayList<>();

    public DrawModule(Module module, int x, int y, int size, int outline, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.outline = outline;
        this.module = module;

        for (Value<?> setting : ValueManager.getInstance().getValues()) {
            if (setting.getObject() == module) {
                if (setting.getValue() instanceof Boolean)
                    elements.add(new DrawCheckBox(setting));
                if (setting.getValue() instanceof String)
                    elements.add(new DrawComboBox(setting));
                if (setting.getValue() instanceof Integer)
                    if (setting.isColorPicker())
                        elements.add(new DrawColorPicker(setting));
                    else
                        elements.add(new DrawSlider(setting));
                if (setting.getValue() instanceof Double)
                    elements.add(new DrawSlider(setting));
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        final RandomUtil randomUtil = RandomUtil.getInstance();
        Color color = this.color;

        if (module.isBypass())
            color = getRainbow(100 * randomUtil.getRandomInteger(1, 3), 3000, 0.8F, Koks.getKoks().periodicClickGUI.category == null || Koks.getKoks().periodicClickGUI.category == module.getCategory() || Koks.getKoks().periodicClickGUI.hoverBypassed ? 1F : 0.4F);

        final RenderUtil renderUtil = RenderUtil.getInstance();
        renderUtil.drawOutline(x - (isHover(mouseX, mouseY) ? 1 : 0), y - (isHover(mouseX, mouseY) ? 1 : 0), x + size + (isHover(mouseX, mouseY) ? 1 : 0), y + size + (isHover(mouseX, mouseY) ? 1 : 0), outline, isHover(mouseX, mouseY) ? color.darker().getRGB() : color.getRGB());

        final int width = Math.abs((x + size) - x);

        final float sizeName = Math.min(0.8F, (float) size / fr.getStringWidth(module.getName()) * 0.7F);
        final float sizeToggle = 0.8F;

        final Color toggleColor = module.isToggled() ? color : color.darker();
        fr.drawString(module.getName().substring(0, 2), x + width / 2 - fr.getStringWidth(module.getName().substring(0, 2)) / 2, y + size / 2F - fr.getStringHeight(module.getName().substring(0, 2)), isHover(mouseX, mouseY) ? toggleColor.darker() : toggleColor, true);
        fr.drawString(module.getName(), (int) (x + width / 2 - (fr.getStringWidth(module.getName()) * sizeName) / 2), y + size / 2, sizeName, isHover(mouseX, mouseY) ? color.darker() : color, true);
        fr.drawString(module.isToggled() ? "Enabled" : "Disabled", (int) (x + width / 2 - fr.getStringWidth(module.isToggled() ? "Enabled" : "Disabled") * sizeToggle / 2), (int) (y + size / 2F + fr.getStringHeight(module.isToggled() ? "Enabled" : "Disabled")), sizeToggle, isHover(mouseX, mouseY) ? color.darker() : color, true);
        fr.drawString(Koks.getKoks().periodicClickGUI.bindMod != null && Koks.getKoks().periodicClickGUI.bindMod.equals(module) ? "..." : module.getKey() == 0 ? "" : Keyboard.getKeyName(module.getKey()), x + 1, y + 1, 0.6F, isHover(mouseX, mouseY) ? color.darker() : color, true);

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
        final PeriodicClickGUI periodicClickGUI = Koks.getKoks().periodicClickGUI;

        if (periodicClickGUI.settingMenu && periodicClickGUI.currentModule == module) {
            int settingsSize = periodicClickGUI.settingsSize;
            final Resolution resolution = Resolution.getResolution();
            int x = resolution.getWidth() / 2;
            int y = resolution.getHeight() / 2;
            if (mouseX < x - settingsSize / 2 || mouseX > x + settingsSize / 2 || mouseY < y - settingsSize / 2 || mouseY > y + settingsSize / 2) {
                periodicClickGUI.currentModule = null;
                periodicClickGUI.settingMenu = false;
                periodicClickGUI.settingScroll = 0;
            }
        }

        if (periodicClickGUI.settingMenu) {
            for (Element element : elements) {
                if (element.value.isVisible()) {
                    element.handleResetButton(mouseX, mouseY, mouseButton);
                    element.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }

        if (isHover(mouseX, mouseY)) {
            if (!periodicClickGUI.settingMenu) {
                switch (mouseButton) {
                    case 0:
                        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                            module.setBypass(!module.isBypass());
                        else
                            module.toggle();
                        break;
                    case 1:
                        if(!ValueManager.getInstance().getValues(module.getClass()).isEmpty()) {
                            Koks.getKoks().periodicClickGUI.currentModule = module;
                            periodicClickGUI.settingMenu = true;
                            periodicClickGUI.settingScroll = 0;
                        }
                        break;
                    case 2:
                        boolean bindModule = Koks.getKoks().periodicClickGUI.bindModule;
                        if (!bindModule) {
                            periodicClickGUI.bindModule = true;
                            periodicClickGUI.bindMod = module;
                        }
                        break;
                }
            }
        }
    }
}
