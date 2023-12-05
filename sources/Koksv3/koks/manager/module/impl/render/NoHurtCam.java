package koks.manager.module.impl.render;

import koks.manager.event.Event;
import koks.manager.event.impl.EventHurtCamera;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author avox | lmao | kroko
 * @created on 17.09.2020 : 09:01
 */

@ModuleInfo(name = "NoHurtCam", description = "Less cancer while getting damage", category = Module.Category.RENDER)
public class NoHurtCam extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventHurtCamera) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}