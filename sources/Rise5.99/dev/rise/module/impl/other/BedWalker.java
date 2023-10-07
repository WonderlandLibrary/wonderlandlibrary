/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;

@ModuleInfo(name = "BedWalker", description = "Allows you to walk while sleeping", category = Category.MOVEMENT)
public final class BedWalker extends Module {

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.isPlayerSleeping()) {
            mc.thePlayer.stopSleeping();
            mc.displayGuiScreen(null);
        }
    }

}
