package net.caffeinemc.phosphor.module.modules.client;

import net.caffeinemc.phosphor.api.event.events.KeyPressEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.font.JColor;
import net.caffeinemc.phosphor.gui.AsteriaMenu;
import net.caffeinemc.phosphor.gui.AsteriaNewMenu;
import net.caffeinemc.phosphor.gui.ImguiLoader;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.ColorSetting;
import net.caffeinemc.phosphor.module.setting.settings.ModeSetting;
import org.lwjgl.glfw.GLFW;

public class AsteriaSettingsModule extends Module {
    public final ModeSetting mode = new ModeSetting("Mode", this, "ClickGUI", "ClickGUI", "Menu");
    public final ColorSetting color = new ColorSetting("Color", this, new JColor(0.90f, 0.27f, 0.33f), false);

    public AsteriaSettingsModule() {
        super("Asteria", "Asteria's settings", GLFW.GLFW_KEY_F8, Category.CLIENT);
    }

    @Override
    public void onEnable() {
        if (mode.getMode().equals("ClickGUI")) {
            if (!ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
                AsteriaMenu.toggleVisibility();
            }
            if (ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
                AsteriaNewMenu.toggleVisibility();
            }
        } else {
            if (ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
                AsteriaMenu.toggleVisibility();
            }
            if (!ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
                AsteriaNewMenu.toggleVisibility();
            }
        }
        mc.mouse.unlockCursor();
    }

    public void updateMode() {
        if (this.isEnabled()) {
            if (mode.getMode().equals("ClickGUI")) {
                if (!ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
                    AsteriaMenu.toggleVisibility();
                }
                if (ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
                    AsteriaNewMenu.toggleVisibility();
                }
            } else {
                if (ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
                    AsteriaMenu.toggleVisibility();
                }
                if (!ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
                    AsteriaNewMenu.toggleVisibility();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (ImguiLoader.isRendered(AsteriaMenu.getInstance())) {
            AsteriaMenu.toggleVisibility();
        }
        if (ImguiLoader.isRendered(AsteriaNewMenu.getInstance())) {
            AsteriaNewMenu.toggleVisibility();
        }
        if (mc.currentScreen == null) mc.mouse.lockCursor();
    }

    @EventHandler
    private void onKeyPress(KeyPressEvent event) {
        if (event.action == GLFW.GLFW_PRESS && event.key == GLFW.GLFW_KEY_ESCAPE) disable();
    }
}
