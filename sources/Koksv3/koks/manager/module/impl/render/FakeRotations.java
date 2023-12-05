package koks.manager.module.impl.render;

import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 26.09.2020 : 14:07
 */

@ModuleInfo(name = "FakeRotations", description = "You doesn't rotate in third person", category = Module.Category.RENDER)
public class FakeRotations extends Module {

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
