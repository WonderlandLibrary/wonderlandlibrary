package me.nyan.flush.clickgui.flush.component.components;

import me.nyan.flush.Flush;
import me.nyan.flush.clickgui.flush.FlushPanel;
import me.nyan.flush.clickgui.flush.component.Component;
import me.nyan.flush.clickgui.flush.component.components.settings.BooleanComponent;
import me.nyan.flush.clickgui.flush.component.components.settings.ColorComponent;
import me.nyan.flush.clickgui.flush.component.components.settings.ModeComponent;
import me.nyan.flush.clickgui.flush.component.components.settings.NumberComponent;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.render.ModuleClickGui;
import me.nyan.flush.module.settings.*;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import me.nyan.flush.module.settings.*;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleComponent extends Component {
    private final Module module;
    private boolean expanded;
    private float expand;
    private boolean listening;
    private final Timer keybindTimer;
    private final int i;

    private int color = 0xFF121212;
    private int fontColor = 0xFFF2F2F2;

    private final ArrayList<Component> components = new ArrayList<>();

    public ModuleComponent(Module module, int i) {
        this.module = module;
        keybindTimer = new Timer();
        keybindTimer.lastMS = 0;

        ArrayList<Setting> settings = new ArrayList<>();
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof ColorSetting)
                .collect(Collectors.toList()));
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof ModeSetting)
                .collect(Collectors.toList()));
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof NumberSetting)
                .collect(Collectors.toList()));
        settings.addAll(module.getSettings().stream().filter(setting -> setting instanceof BooleanSetting)
                .collect(Collectors.toList()));

        int j = i;
        for (Setting setting : settings) {
            if (setting instanceof ColorSetting) {
                components.add(new ColorComponent((ColorSetting) setting));
            }

            if (setting instanceof BooleanSetting) {
                components.add(new BooleanComponent((BooleanSetting) setting, j));
            }

            if (setting instanceof NumberSetting) {
                components.add(new NumberComponent((NumberSetting) setting, j));
            }

            if (setting instanceof ModeSetting) {
                components.add(new ModeComponent((ModeSetting) setting));
            }
            j++;
        }

        this.i = i;
    }

    @Override
    public void init() {
        for (Component component : components) {
            component.init();
        }
    }

    public void update() {
        if (!expanded) {
            expand -= expand / 100F * Flush.getFrameTime();
            if (expand < 0.01) {
                expand = 0;
            }
        } else {
            expand += ((1 - expand) / 100F) * Flush.getFrameTime();
            if (expand > 0.99) {
                expand = 1;
            }
        }
        expand = Math.max(Math.min(expand, 1), 0);

        for (Component component : components) {
            component.update();
        }
    }

    @Override
    public void draw(float x, float y, int mouseX, int mouseY, float partialTicks) {
        GlyphPageFontRenderer font = Flush.getFont(module.isEnabled() ? "GoogleSansDisplay Medium" : "GoogleSansDisplay", 18);

        int targetColor = module.isEnabled() ? Flush.getInstance().getModuleManager()
                .getModule(ModuleClickGui.class).getClickGUIColor(i) : 0xFF121212;
        color = ColorUtils.animateColor(color, targetColor, 16);

        int targetFontColor = module.isEnabled() ? 0xFF121212 : 0xFFF2F2F2;
        fontColor = ColorUtils.animateColor(fontColor, targetFontColor, 16);
        fontColor = ColorUtils.contrast(color);

        Gui.drawRect(x, y, x + getWidth(), y + getHeight(), color);

        String string = module.getName() + (listening ? "..." : "");
        if (!keybindTimer.hasTimeElapsed(1000, false)) {
            if (module.getKeys().isEmpty()) {
                string = "Unbound";
            } else {
                string = "Bound to " + module.getKeysString();
            }
        }
        font.drawXYCenteredString(
                string,
                x + getWidth() / 2F,
                y + getHeight() / 2F,
                fontColor
        );

        if (expand == 0) {
            return;
        }

        float settingY = y + getHeight();
        for (Component component : components) {
            if (!component.show()) {
                continue;
            }
            component.draw(x, settingY, mouseX, mouseY, partialTicks);
            settingY += component.getFullHeight();
        }
        RenderUtils.drawGradientRect(x, settingY - 10, x + getWidth(), settingY, 0x00000000, 0x88000000);
    }

    @Override
    public void mouseClicked(float x, float y, int mouseX, int mouseY, int button) {
        if (MouseUtils.hovered(mouseX, mouseY, x, y + 0.5, x + getWidth(), y + getHeight() - 0.5)) {
            if (button == 0) {
                module.toggle();
            }
            if (button == 1 && !components.isEmpty()) {
                expanded = !expanded;
            }
            if (button == 2) {
                listening = !listening;
            }
        }

        if (expand != 1) {
            return;
        }

        float settingY = y + getHeight();
        for (Component component : components) {
            if (!component.show()) {
                continue;
            }
            component.mouseClicked(x, settingY, mouseX, mouseY, button);
            settingY += component.getFullHeight();
        }
    }

    @Override
    public void mouseReleased(float x, float y, int mouseX, int mouseY, int button) {
        if (expand != 1) {
            return;
        }

        float settingY = y + getHeight();
        for (Component component : components) {
            if (!component.show()) {
                continue;
            }
            component.mouseReleased(x, settingY, mouseX, mouseY, button);
            settingY += component.getFullHeight();
        }
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if (listening) {
            module.clearKeys();
            listening = false;
            keybindTimer.reset();
            if (keyCode == Keyboard.KEY_ESCAPE) {
                return true;
            }
            module.addKey(keyCode);
            return true;
        }
        return false;
    }

    @Override
    public float getHeight() {
        return FlushPanel.TITLE_HEIGHT - 4;
    }

    @Override
    public float getFullHeight() {
        float height = 0;
        for (Component component : components) {
            if (!component.show()) {
                continue;
            }
            height += component.getFullHeight();
        }
        return getHeight() + height * expand;
    }

    public Module getModule() {
        return module;
    }
}
