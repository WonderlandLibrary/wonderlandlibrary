package koks.manager.module.impl.render;

import koks.manager.event.Event;
import koks.manager.event.impl.EventBobbing;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */

@ModuleInfo(name = "NoBob", description = "Your hand doesn't bobbing", category = Module.Category.RENDER)
public class NoBob extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventBobbing) {
            ((EventBobbing) event).setBobbing(0);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}