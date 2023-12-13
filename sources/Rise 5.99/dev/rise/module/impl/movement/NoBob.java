package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;

@ModuleInfo(name = "NoBob", description = "bobbing? NO!", category = Category.MOVEMENT)
public final class NoBob extends Module {

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        mc.thePlayer.distanceWalkedModified = 0.0F;
        mc.gameSettings.viewBobbing = true;
    }
}