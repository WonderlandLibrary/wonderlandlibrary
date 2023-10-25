package wtf.evolution.module.impl.Movement;

import org.lwjgl.input.Keyboard;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;

@ModuleInfo(name = "LongJump", type = Category.Movement)
public class LongJump extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Air Jump").call(this);

    @EventTarget
    public void onUpdate(EventMotion e) {
        if (mode.is("Air Jump")) {
            if ((!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0, -1, 0).expand(-1f, 0, -1f).expand(1, 0, 1)).isEmpty())) {
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    mc.player.jumpTicks = 0;
                    mc.player.fallDistance = 0;
                    e.setOnGround(true);
                    mc.player.onGround = true;
                }
            }
        }
        else if (mode.is("Vanilla")) {
            if (!mc.player.onGround) {
                MovementUtil.setSpeed((float) (MovementUtil.getPlayerMotion() + 0.03f));
            }
        }
    }

}
