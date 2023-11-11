package me.nyan.flush.clickgui.sigma.component.components;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.sigma.SigmaClickGui;
import me.nyan.flush.clickgui.sigma.SigmaPanel;
import me.nyan.flush.clickgui.sigma.component.Component;
import me.nyan.flush.clickgui.sigma.component.components.settings.BooleanComponent;
import me.nyan.flush.clickgui.sigma.component.components.settings.ModeComponent;
import me.nyan.flush.clickgui.sigma.component.components.settings.NumberComponent;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.module.settings.Setting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleComponent extends Component {
    private final Module module;

    private final ArrayList<Component> components = new ArrayList<>();

    public ModuleComponent(Module module) {
        this.module = module;

        ArrayList<Setting> settings = new ArrayList<>();
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof ModeSetting)
                .collect(Collectors.toList()));
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof NumberSetting)
                .collect(Collectors.toList()));
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof BooleanSetting)
                .collect(Collectors.toList()));

        for (Setting setting : settings) {
            if (setting instanceof BooleanSetting) {
                components.add(new BooleanComponent((BooleanSetting) setting));
            }

            if (setting instanceof NumberSetting) {
                components.add(new NumberComponent((NumberSetting) setting));
            }

            if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting));
            }
        }
    }

    @Override
    public void init() {
        for (Component component : components) {
            component.init();
        }
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 20);

        boolean hovered = MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getHeight() - 0.5);

        boolean holding = Mouse.isButtonDown(0) || Mouse.isButtonDown(1);
        int targetColor = module.isEnabled() ? (hovered ? (holding ? 0xFF29C1FF : 0xFF29B8FF) : 0xff29A6FF) : (hovered ? (holding ? 0xFFE0E0E0 : 0xFFE7E7E7) : 0xFFFAFAFA);
        int targetFontColor = module.isEnabled() ? 0xFFFAFAFA : 0xFF000000;

        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), targetColor);

        font.drawString(
                module.getName(),
                x + 10 + (module.isEnabled() ? 3 : 0),
                y + getHeight() / 2F - font.getFontHeight() / 2F,
                targetFontColor
        );
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getHeight() - 0.5)) {
            if (button == 0) {
                module.toggle();
            }
            if (button == 1) {
                ((SigmaClickGui) Minecraft.getMinecraft().currentScreen).openSettings(this);
            }
        }
    }

    @Override
    public float getWidth() {
        return SigmaPanel.WIDTH;
    }

    @Override
    public float getHeight() {
        return 15;
    }

    public Module getModule() {
        return module;
    }

    public ArrayList<Component> getSettings() {
        return components;
    }
}
