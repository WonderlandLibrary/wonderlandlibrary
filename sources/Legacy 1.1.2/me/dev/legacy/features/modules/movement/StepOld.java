package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;

public class StepOld extends Module
{
    public Setting<Integer> height;

    public StepOld() {
        super("StepOld", "Allows you to step up blocks", Category.MOVEMENT, true, false, false);
        this.height = (Setting<Integer>)this.register(new Setting("Height", 2, 0, 5));
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        StepOld.mc.player.stepHeight = 2.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        StepOld.mc.player.stepHeight = 0.6f;
    }
}

