package koks.manager.module.impl.combat;

import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 14.10.2020 : 17:49
 */

@ModuleInfo(name = "Friends", description = "You doesn't attack your Friends", category = Module.Category.COMBAT)
public class Friends extends Module {

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
