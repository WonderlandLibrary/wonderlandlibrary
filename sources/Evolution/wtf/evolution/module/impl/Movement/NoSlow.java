package wtf.evolution.module.impl.Movement;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventNoSlow;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "NoSlow", type = Category.Movement)
public class NoSlow extends Module {

    @EventTarget
    public void onEating(EventNoSlow e) {
        if(mc.player.onGround) {
            if (mc.player.ticksExisted % 2 == 0) {
                mc.player.motionX *= 0.4;
                mc.player.motionZ *= 0.4;
            }
            if (mc.player.ticksExisted % 4 == 0) {
                mc.player.motionX *= 1.2;
                mc.player.motionZ *= 1.2;
            }
        } else {
            mc.player.motionX *= 0.97;
            mc.player.motionZ *= 0.97;
        }
        e.cancel();
    }
}
