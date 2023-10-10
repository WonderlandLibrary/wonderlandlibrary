package today.flux.module.implement.Movement;

import com.darkmagician6.eventapi.EventTarget;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Misc.disabler.Hypixel;
import today.flux.utility.MoveUtils;

public class Strafe extends Module {
    public Strafe() {
        super("Strafe", Category.Movement, false);
    }

    @EventTarget
    private void onPre(PreUpdateEvent event) {
        if (ModuleManager.speedMod.isEnabled() || ModuleManager.longJumpMod.isEnabled())
            return;

		if (!mc.thePlayer.onGround) {
			if (mc.gameSettings.keyBindJump.pressed) {
                if ((mc.gameSettings.keyBindBack.pressed || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindLeft.pressed) && Math.abs(Hypixel.yawDiff) > 20) {
                    MoveUtils.strafe(MoveUtils.getSpeed() * 0.85);
                } else {
                    MoveUtils.strafe();
                }
			}
		}
    }
}
