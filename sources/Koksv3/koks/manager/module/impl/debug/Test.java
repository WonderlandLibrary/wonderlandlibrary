package koks.manager.module.impl.debug;

import god.buddy.aot.BCompiler;
import koks.api.settings.SettingInfo;
import koks.manager.event.Event;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 11:56
 */

@ModuleInfo(name = "Test", description = "A Test Module", category = Module.Category.DEBUG)
public class Test extends Module {

    @SettingInfo(name = "testFloat", max = 100, min = 0, onlyInt = true)
    public float testFloat = 1;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            /* if(getPlayer().isEating()) {
                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }*/

            getPlayer().motionY = 0;

            /*getPlayer().isInWeb = false;*/
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

}