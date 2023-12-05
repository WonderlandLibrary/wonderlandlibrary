package koks.manager.module.impl.gui;

import koks.Koks;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import koks.manager.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 00:18
 */

@ModuleInfo(name = "ClickGUI", description = "Opens the ClickGUI", category = Module.Category.GUI, key = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Panel", "PSE"}, "Panel", this);
    public Setting fakeCredits = new Setting("Fake Credits", true, this);
    public Setting fakeAuthor = new Setting("Fake Author", "Herokot", this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (mc.currentScreen == null) {
            switch (mode.getCurrentMode()) {
                case "PSE":
                    mc.displayGuiScreen(Koks.getKoks().clickGUIPE);
                    break;
                case "Panel":
                    mc.displayGuiScreen(Koks.getKoks().clickGUI);
                    break;
            }
        }
        toggle();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}