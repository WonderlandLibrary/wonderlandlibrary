package wtf.monsoon.impl.modules.misc;

import org.lwjgl.input.Keyboard;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.module.Category;
import wtf.monsoon.api.module.Module;
import wtf.monsoon.api.setting.impl.ModeSetting;
import wtf.monsoon.api.Wrapper;


public class ModToggleGUI extends Module {

    public ModeSetting theme = new ModeSetting("Theme", this, "Monsoon", "Monsoon", "Discord", "Modern", "CSGO");

    public ModToggleGUI() {
        super("ClickGUI", "Toggle and Manager your Modules", Keyboard.KEY_RSHIFT, Category.MISC);
        addSettings(theme);
    }

    public void onEnable() {
        super.onEnable();
        if(theme.is("CSGO")) {
            Wrapper.mc.displayGuiScreen(Monsoon.INSTANCE.getSkeetGui());
            this.toggle();
        } else if(theme.is("Monsoon") || theme.is("Discord")) {
        	Monsoon.INSTANCE.getClickGUI().display();
        } else if(theme.is("Modern")) {
            Monsoon.INSTANCE.getClickGUI().display();
        }
    }
}
