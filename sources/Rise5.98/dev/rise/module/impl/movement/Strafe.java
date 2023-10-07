package dev.rise.module.impl.movement;

import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;

@ModuleInfo(name = "Strafe", description = "Makes you always strafe", category = Category.MOVEMENT)
public class Strafe extends Module {
    private final BooleanSetting only_on_ground = new BooleanSetting("Only On Ground", this, false);

    @Override
    public void onStrafe(final StrafeEvent event) {
        if(only_on_ground.enabled){
            if(mc.thePlayer.onGround){
                MoveUtil.strafe();
            }
        }else{
            MoveUtil.strafe();
        }
    }
}
