package dev.tenacity.module.impl.render;

import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.ui.clickgui.dropdown.DropDownClickGUI;
import org.lwjgl.input.Keyboard;

public final class ClickGUIModule extends Module {
    private final DropDownClickGUI dropDownClickGUI = new DropDownClickGUI();
    private ModeSetting mode = new ModeSetting("Type", "Dropdown", "Modern");
    public static BooleanSetting cat = new BooleanSetting("Cat", true);

    public ClickGUIModule() {
        super("ClickGUI", "A panel rendering category's, modules, and settings.", ModuleCategory.RENDER);
        setKeyCode(Keyboard.KEY_RSHIFT);

        initializeSettings(cat);
    }

    @Override
    public void onEnable() {
        if (mode.isMode("Dropdown")) {
            mc.displayGuiScreen(dropDownClickGUI);
        }
        if (mode.isMode("Modern")) {
            mc.displayGuiScreen(dropDownClickGUI);
        }
        toggle();
    }
}
