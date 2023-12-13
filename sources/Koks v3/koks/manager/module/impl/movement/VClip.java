package koks.manager.module.impl.movement;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventKeyPress;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

/**
 * @author deleteboys | lmao | kroko
 * @created on 17.09.2020 : 18:39
 */

@ModuleInfo(name = "VClip", description = "You can teleport through grounds", category = Module.Category.MOVEMENT)
public class VClip extends Module {

    Setting upKey = new Setting("Up Key", Keyboard.KEY_UP, this);
    Setting upHeight = new Setting("Up Height", 5, 1, 5, true, this);

    Setting downKey = new Setting("Down Key", Keyboard.KEY_DOWN, this);
    Setting downHeight = new Setting("Down Height", -5, -5, -1, true, this);
    Setting stuck = new Setting("Stuck", true, this);

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventUpdate) {
            if (stuck.isToggled())
                mc.thePlayer.motionY = 0;
        }
        if (event instanceof EventKeyPress) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;
            if (((EventKeyPress) event).getKey() == upKey.getKey()) {
                setPosition(x, y + upHeight.getCurrentValue(), z, getPlayer().onGround);
            } else if (((EventKeyPress) event).getKey() == downKey.getKey()) {
                setPosition(x, y + downHeight.getCurrentValue(),z, getPlayer().onGround);
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
