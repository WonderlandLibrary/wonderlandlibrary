package vestige.module.impl.movement;

import vestige.util.network.ServerUtil;
import vestige.event.Listener;
import vestige.event.Priority;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        this.setEnabledSilently(true);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSprint.pressed = false;
    }

    @Listener(Priority.HIGH)
    public void onTick(TickEvent event) {
        mc.gameSettings.keyBindSprint.pressed = true;

        // It's needed on hypixel for some reason with spoof autoblock only (otherwise you can sprint sideaways for some reason), if you find the issue please let me know.
        // It doesn't do that on other servers btw
        if (mc.thePlayer.moveForward <= 0F && ServerUtil.isOnHypixel()) {
            mc.thePlayer.setSprinting(false); // Sprint ticks are over 0 even if mc.thePlayer.isSprinting() returns false
        }
    }
}