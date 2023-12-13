package dev.monsoon.module.implementation.misc;

import dev.monsoon.Monsoon;
import dev.monsoon.module.setting.impl.ModeSetting;
import dev.monsoon.ui.clickgui.skeet.SkeetGUI;
import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;
import dev.monsoon.module.enums.Category;

public class ModToggleGUI extends Module {

    public ModeSetting theme = new ModeSetting("Theme", this, "Monsoon", "Monsoon", "Discord", "Skeet");

    public ModToggleGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.MISC);
        addSettings(theme);
    }

    public void onEnable() {
        super.onEnable();
        if(theme.is("Skeet")) {
            mc.displayGuiScreen(Monsoon.getSkeetGui());
            this.toggle();
        } else if(theme.is("Monsoon") || theme.is("Discord")) {
            Monsoon.getClickGUI().display();
        }
    }
}
