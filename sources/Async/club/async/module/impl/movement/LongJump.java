package club.async.module.impl.movement;

import club.async.event.impl.EventUpdate;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import club.async.module.setting.impl.BooleanSetting;
import club.async.module.setting.impl.ModeSetting;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "LongJump", description = "Move in inventory", category = Category.MOVEMENT)
public class LongJump extends Module {

    public ModeSetting mode = new ModeSetting("Mode", this, new String[]{"RedeSky"});
    public BooleanSetting autoDisable = new BooleanSetting("AutoDisable", this, true);

    private boolean shouldDisable;

    @Handler
    public void update(EventUpdate eventUpdate) {
        setExtraTag(mode.getCurrMode());
        if (mc.thePlayer.onGround) {
            if (!shouldDisable || !autoDisable.get()) {
                mc.thePlayer.jump();
//                for (int i = 0; i < 5; i++) {
                    mc.thePlayer.motionY += 0.5f;
//                }
                shouldDisable = true;
            } else {
                toggle();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        shouldDisable = false;
    }

}
