package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 17.11.2020 : 18:22
 */

@ModuleInfo(name = "FastBreak", category = Module.Category.PLAYER, description = "You can break fast blocks")
public class FastBreak extends Module {

    public Setting percent = new Setting("Percent", 100F, 0F, 100F, true, this);

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
