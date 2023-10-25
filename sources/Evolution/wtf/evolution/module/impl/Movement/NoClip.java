package wtf.evolution.module.impl.Movement;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;


@ModuleInfo(name = "NoClip", type = Category.Movement)
public class NoClip extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "SunRise", "SunRise");

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mc.player.collidedHorizontally) {
            mc.player.onGround = true;
            if (!mc.gameSettings.keyBindSneak.isKeyDown())
                mc.player.motionY = 0;
        }
    }

}
