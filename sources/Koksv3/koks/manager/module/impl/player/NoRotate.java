package koks.manager.module.impl.player;

import koks.manager.event.Event;
import koks.manager.event.impl.EventHeadLook;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author kroko
 * @created on 01.11.2020 : 18:50
 */

@ModuleInfo(category = Module.Category.PLAYER, description = "You doesn't rotate to the block after flagging", name = "NoRotate")
public class NoRotate extends Module {

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if(event instanceof EventHeadLook) {
            ((EventHeadLook) event).setF1(getPlayer().rotationYaw);
            ((EventHeadLook) event).setF2(getPlayer().rotationPitch);
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
