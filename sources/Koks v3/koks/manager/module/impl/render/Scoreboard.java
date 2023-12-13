package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 25.10.2020 : 04:17
 */

@ModuleInfo(name = "Scoreboard", description = "Enable the Scoreboard", category = Module.Category.RENDER)
public class Scoreboard extends Module {

    public Setting numbers = new Setting("Numbers", false ,this);

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
