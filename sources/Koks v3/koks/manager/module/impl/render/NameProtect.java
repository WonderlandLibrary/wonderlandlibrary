package koks.manager.module.impl.render;

import koks.manager.event.Event;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 04.10.2020 : 21:08
 */

@ModuleInfo(name = "NameProtect", description = "Your name is hidden", category = Module.Category.RENDER)
public class NameProtect extends Module {

    public NameProtect() {
        setToggled(true);
    }

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
