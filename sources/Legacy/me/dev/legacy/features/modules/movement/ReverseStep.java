package me.dev.legacy.features.modules.movement;

import me.dev.legacy.features.modules.Module;

public class ReverseStep extends Module {
    private static ReverseStep INSTANCE = new ReverseStep();

    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Module.Category.MOVEMENT, true, false, false);
        setInstance();
    }

    public static ReverseStep getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReverseStep();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null || mc.player.isInWater() || mc.player.isInLava()) {
            return;
        }
        if (mc.player.onGround) {
            --mc.player.motionY;
        }
    }
}