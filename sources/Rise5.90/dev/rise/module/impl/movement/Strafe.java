package dev.rise.module.impl.movement;

import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.player.MoveUtil;

@ModuleInfo(name = "Strafe", description = "Makes you always strafe", category = Category.MOVEMENT)
public class Strafe extends Module {

    @Override
    public void onStrafe(final StrafeEvent event) {
        MoveUtil.strafe();
    }
}
