package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.player.KeyEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AutoSprint extends Module {
    public static BooleanValue keepSprint = new BooleanValue("Keep Sprint", false);
    public static BooleanValue keepSprint2 = new BooleanValue("NoHurt Keep Sprint", false);
    public static BooleanValue noJumpDelay = new BooleanValue("No Jump Delay", false);
    public AutoSprint() {
        super("AutoSprint", Category.Movement, "Auto sprint for yo");
     registerValue(keepSprint);
     registerValue(keepSprint2);
     registerValue(noJumpDelay);
    }
    public static boolean sprint = true;
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        mc.gameSettings.keyBindSprint.pressed = sprint;
        sprint = true;
        super.onUpdateEvent(event);
    }

    @Override
    public void onKeyEvent(KeyEvent event) {
        super.onKeyEvent(event);
    }
}
